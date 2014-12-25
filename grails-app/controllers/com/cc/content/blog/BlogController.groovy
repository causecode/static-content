/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog

import grails.plugin.springsecurity.annotation.Secured
import org.grails.databinding.SimpleMapDataBindingSource
import grails.converters.JSON
import java.text.DateFormatSymbols
import java.util.regex.Matcher
import java.util.regex.Pattern
import grails.transaction.Transactional
import org.grails.taggable.TagLink
import org.springframework.dao.DataIntegrityViolationException

import com.cc.annotation.shorthand.ControllerShorthand
import com.cc.content.blog.comment.BlogComment
import com.cc.content.blog.comment.Comment

/**
 * Provides default CRUD end point for Content Manager.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 *
 */
@Secured(["permitAll"])
@Transactional(readOnly = true)
@ControllerShorthand(value = "blog")
class BlogController {

    static allowedMethods = [save: "POST", update: "POST"]
    
    static responseFormats = ["json"]

    def beforeInterceptor = [action: this.&validate]

    def contentService
    def springSecurityService
    def simpleCaptchaService
    def commentService
    def blogService
    def grailsWebDataBinder

    private Blog blogInstance

    private static String HTML_P_TAG_PATTERN = "(?s)<p(.*?)>(.*?)<\\/p>"

    private boolean validate() {
        println("<<<<<<<< in validate")
        if(!params.id) {
            return true
        }
        blogInstance = Blog.get(params.id)
        if (!blogInstance) {
            flash.message = "Sorry, no blog found with Id $params.id"
            redirect(uri: "/blog")
            return false
        }
        return true
    }

    /**
     * Action list filters blog list with tags and returns Blog list and total matched result count.
     * If current user has role content manager then all Blog list will be returned otherwise blog with publish field 
     * set to true will be returned.
     * @param max Pagination parameters used to specify maximum number of list items to be returned.
     * @param offset Pagination parameter 
     * @param tag String Used to filter Blogs with tag.
     * @return Map containing blog list and total count.
     */
    @Secured(["permitAll"])
    def index() {
        redirect( action: 'list', params: params)
    }

    @Secured(["permitAll"])
    def list(Integer max, Integer offset, String tag, String monthFilter, String queryFilter) {
        if (tag == 'undefined') tag = ''
        if (monthFilter == 'undefined') monthFilter = ''
        if (queryFilter == 'undefined') queryFilter = ''

        log.info "Parameters received to filter blogs : $params"
        long blogInstanceTotal
        boolean publish = false
        int defaultMax = grailsApplication.config.cc.plugins.content.blog.list.max ?: 10
        List<String> monthFilterList = []
        String month, year

        if(monthFilter) {
            List blogFilter =  monthFilter.split("-")
            month = blogFilter[0]
            year = blogFilter[1]
        }

        params.offset = offset ? offset: 0
        params.max = Math.min(max ?: defaultMax, 100)

        StringBuilder query = new StringBuilder("""SELECT new Map(b.id as id, b.body as body, b.title as title,
                            b.subTitle as subTitle, b.author as author, b.publishedDate as publishedDate) FROM Blog b """)

        if(tag) {
            query.append(", ${TagLink.class.name} tagLink WHERE b.id = tagLink.tagRef ")
            query.append("AND tagLink.type = 'blog' AND tagLink.tag.name = '$tag' ")
        }
        if(monthFilter) {
            tag ? query.append(" AND ") : query.append(" WHERE ")
            query.append(" monthname(b.publishedDate) = '$month' AND year(b.publishedDate) = '$year'")
        }
        if(queryFilter) {
            tag ? query.append(" AND ") : ( monthFilter ? query.append(" AND ") : query.append(" WHERE "))
            query.append(" b.title LIKE '%$queryFilter%' OR b.subTitle LIKE '%$queryFilter%' ")
            query.append(" OR b.body LIKE '%$queryFilter%' OR b.author LIKE '%$queryFilter%'")
        }

        if(contentService.contentManager) {
            blogInstanceTotal = tag ? Blog.countByTag(tag) : Blog.count()
        } else if(tag) {
            query.append("AND b.publish = true")
            blogInstanceTotal = Blog.findAllByTagWithCriteria(tag) {
                eq("publish", true)
            }.size()
        } else if(monthFilter) {
            query.append("AND b.publish = true")
            blogInstanceTotal = Blog.countByPublish(true)
        } else {
            (queryFilter) ? query.append(" AND ") : query.append(" where ")
            query.append(" b.publish = true")
            blogInstanceTotal = Blog.countByPublish(true)
        }
        query.append(" order by b.dateCreated desc")

        List<Map> blogList = Blog.executeQuery(query.toString(), [max: params.max, offset: params.offset])
        Pattern patternTag = Pattern.compile(HTML_P_TAG_PATTERN)

        blogList.each {
            // Extracting content from first <p> tag of body to display
            Matcher matcherTag = patternTag.matcher(it.body)
            it.body = matcherTag.find() ? matcherTag.group(2) : ""
        }

        if(monthFilter) {
            blogInstanceTotal = Blog.executeQuery(query.toString()).size()
        }

        List<Blog> blogInstanceList = []
        blogList.each {
            Blog blogInstance = Blog.get(it.id as long)
            it.author = contentService.resolveAuthor(blogInstance)
            it.numberOfComments = BlogComment.countByBlog(blogInstance)
            blogInstanceList.add(it)
        }
		println("Now check my size.. "+Bolg.list().size())
        Blog.list().each {
            monthFilterList.add( new DateFormatSymbols().months[it.publishedDate[Calendar.MONTH]] + "-" + it.publishedDate[Calendar.YEAR] )
        }

        Map result = [blogInstanceList: blogInstanceList, blogInstanceTotal: blogInstanceTotal, monthFilterList: monthFilterList.unique(),
            tagList: blogService.getAllTags()]
        if(request.xhr) {
            render text:(result as JSON)
            return
        }
        result
    }

    def create() {
		println("in create ..  :-)")
        [blogInstance: new Blog(params)]
    }

    /**
     * Create Blog instance and also sets tags for blog.
     */
    @Transactional
    def save() {
        params.putAll(request.JSON)
        Blog.withTransaction { status ->
            blogInstance = contentService.create(params, params.metaList.type, params.metaList.value, Blog.class)
            if (blogInstance.hasErrors()) {
                status.setRollbackOnly()
                respond(blogInstance.errors)
                return
            }
            blogInstance.setTags(params.tags.tokenize(",")*.trim())
            blogInstance.save(flush: true)
            println("here now")
            flash.message = message(code: 'default.created.message', args: [message(code: 'blog.label'), blogInstance.title])
            redirect uri: blogInstance.searchLink()
        }
    }
    
    @Transactional
    @Secured(["permitAll"])
    def show(Long id) {
        println(">>>>>>>>>>>in blog show")
        List blogComments = commentService.getComments(blogInstance)
        List tagList = []

        tagList = blogService.getAllTags()
        def blogInstanceTags = blogInstance.tags
        List<Blog> blogInstanceList = Blog.findAllByPublish(true, [max: 5, sort: 'publishedDate', order: 'desc'])
        Map result = [blogInstance: blogInstance, comments: blogComments, tagList: tagList, 
            blogInstanceList: blogInstanceList, blogInstanceTags: blogInstanceTags]

//        if(request.xhr) {
//            render text:(result as JSON)
//            return
//        }
        respond(result)
    }

    @Transactional
    def edit(Long id) {
        [blogInstance: blogInstance]
    }

    /**
     * Update blog instance also sets tags for blog instance.
     */
    @Transactional
    def update(Long id, Long version) {
        if (version != null) {
            if (blogInstance.version > version) {
                blogInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'blog.label')] as Object[],
                        "Another user has updated this Blog while you were editing")
                render(view: "edit", model: [blogInstance: blogInstance])
                return
            }
        }

        Blog.withTransaction { status ->
            String tags = params.remove("tags")
            contentService.update(params, blogInstance, params.meta.list("type"), params.meta.list("value"))

            if (blogInstance.hasErrors()) {
                status.setRollbackOnly()
                render(view: "edit", model: [blogInstance: blogInstance])
                return
            }

            blogInstance.setTags(tags.tokenize(",")*.trim())
            blogInstance.save(flush: true)

            flash.message = message(code: 'default.updated.message', args: [message(code: 'blog.label'), blogInstance.id])
            redirect uri: blogInstance.searchLink()
        }
    }

    def delete(Long id) {
        try {
            contentService.delete(blogInstance)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'blog.label'), id])
            redirect(uri: "/blog")
        } catch (DataIntegrityViolationException e) {
            log.warn "Error deleting blog.", e
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'blog.label'), id])
            redirect uri: blogInstance.searchLink()
        }
    }

    /**
     * This action adds comments for blog with verified Captcha and redirects to blog show page.
     * If captcha is invalid comments will not be added for blog.
     * @param commentId Identity of Comment domain.If these parameter received then newly created comment will be added 
     * as reply to given comment instance otherwise comment will be added as reference to blog instance instance.
     */
    @Transactional
    @Secured(["permitAll"])
    def comment(Long commentId) {
        Map requestMap = request.JSON
        String errorMessage
        params.putAll(requestMap)
        log.info "Parameters received to comment on blog: $params"
        if (!params.id) {
            errorMessage = "Not enough parameters recived to add comment."
            log.info errorMessage
            Map result = [message: errorMessage]
            render status: 403, text: result as JSON
            return
        }
        Comment.withTransaction { status ->
            boolean captchaValid = simpleCaptchaService.validateCaptcha(params.captcha)
            if(!captchaValid) {
                if (request.xhr) {
                    Map result = [message: "Invalid capthca entered."]
                    render status: 403, text: result as JSON
                    return
                }
                flash.message = message(code: 'default.captcha.invalid.message', args: [message(code: 'captcha.label')])
                redirect uri: blogInstance.searchLink()
                return
            }
            Comment commentInstance = new Comment()
            grailsWebDataBinder.bind(commentInstance, params as SimpleMapDataBindingSource, ['subject', 'name', 'email', 'commentText'])
            if(!commentInstance.save()) {
                status.setRollbackOnly()
                if (request.xhr) {
                    Map result = [message: "Something went wrong, Please try again later."]
                    render status: 403, text: result as JSON
                    return
                }
                redirect uri: blogInstance.searchLink()
                return
            }
            commentId = commentId ?: (params.commentId ?  params.commentId as long : 0l)
            if(commentId) {
                commentInstance.replyTo = Comment.get(commentId)
                commentInstance.save()
            } else {
                BlogComment blogCommentInstance = new BlogComment()
                blogCommentInstance.blog = blogInstance
                blogCommentInstance.comment = commentInstance
                blogCommentInstance.save()
            }
            log.info "Comment Added successfully."
            if (request.xhr) {
                render text: ([success: true] as JSON)
                return
            }
            redirect uri: blogInstance.searchLink()
        }
    }

}
