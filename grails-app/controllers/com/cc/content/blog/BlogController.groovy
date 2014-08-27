/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog

import grails.plugin.springsecurity.annotation.Secured

import java.text.DateFormatSymbols
import java.util.regex.Matcher
import java.util.regex.Pattern

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
@Secured(["ROLE_CONTENT_MANAGER"])
@ControllerShorthand(value = "blog")
class BlogController {

    static allowedMethods = [save: "POST", update: "POST"]

    def beforeInterceptor = [action: this.&validate]

    def contentService
    def springSecurityService
    def simpleCaptchaService

    private Blog blogInstance

    private static String HTML_P_TAG_PATTERN = "(?s)<p(.*?)>(.*?)<\\/p>"

    private boolean validate() {
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
    def list(Integer max, Integer offset, String tag, String monthFilter) {
        long blogInstanceTotal
        boolean publish = false
        int defaultMax = grailsApplication.config.cc.plugins.content.blog.list.max ?: 10
        List tagList = []
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

        if(contentService.contentManager) {
            blogInstanceTotal = tag ? Blog.countByTag(tag) : Blog.count()
        } else if(tag ) {
            query.append("AND b.publish = true")
            blogInstanceTotal = Blog.findAllByTagWithCriteria(tag) {
                eq("publish", true)
            }.size()
        } else {
            (monthFilter) ? query.append(" AND ") : query.append(" where ")
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

        blogList.each {
            monthFilterList.add( new DateFormatSymbols().months[it.publishedDate[Calendar.MONTH]] + "-" + it.publishedDate[Calendar.YEAR] )
        }

        Blog.allTags.each { tagName ->
            def tagListInstance = TagLink.withCriteria(uniqueResult: true) {
                createAlias('tag', 'tagInstance')
                projections {
                    countDistinct 'id'
                    property("tagInstance.name")
                }
                eq('type','blog')
                eq('tagInstance.name', tagName)
            }
            tagList.add(tagListInstance)
        }

        [blogInstanceList: blogList, blogInstanceTotal: blogInstanceTotal, monthFilterList: monthFilterList.unique(),
            tagList: tagList]
    }

    def create() {
        [blogInstance: new Blog(params)]
    }

    /**
     * Create Blog instance and also sets tags for blog.
     */
    def save() {
        Blog.withTransaction { status ->
            blogInstance = contentService.create(params, params.meta.list("type"), params.meta.list("value"), Blog.class)
            if (blogInstance.hasErrors()) {
                status.setRollbackOnly()
                render(view: "create", model: [blogInstance: blogInstance])
                return
            }
            blogInstance.setTags(params.tags.tokenize(",")*.trim())
            blogInstance.save(flush: true)
            flash.message = message(code: 'default.created.message', args: [message(code: 'blog.label'), blogInstance.title])
            redirect uri: blogInstance.searchLink()
        }
    }

    @Secured(["permitAll"])
    def show(Long id) {
        List blogComments = BlogComment.findAllByBlog(blogInstance)*.comment
        List tagNameList = []
        List tagFrequesncyList = []

        blogInstance.tags.each { tagName ->
            tagNameList.add(tagName)
            def result = TagLink.withCriteria {
                eq('type','blog')
                tag {
                    eq('name', tagName)
                }
            }
            tagFrequesncyList.add( result.size() ?:1 )
        }
        [blogInstance: blogInstance, comments: blogComments, tagNameList: tagNameList, tagFrequesncyList: tagFrequesncyList]
    }

    def edit(Long id) {
        [blogInstance: blogInstance]
    }

    /**
     * Update blog instance also sets tags for blog instance.
     */
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
    @Secured(["permitAll"])
    def comment(Long commentId) {
        Comment.withTransaction { status ->
            boolean captchaValid = simpleCaptchaService.validateCaptcha(params.captcha)
            if(!captchaValid) {
                flash.message = message(code: 'default.captcha.invalid.message', args: [message(code: 'captcha.label')])
                redirect uri: blogInstance.searchLink()
                return
            }
            Comment commentInstance = new Comment()
            bindData(commentInstance, params, [include: ['subject', 'name', 'email', 'commentText']])
            if(!commentInstance.save(flush: true)) {
                status.setRollbackOnly()
                redirect uri: blogInstance.searchLink()
                return
            }
            if(commentId) {
                commentInstance.replyTo = Comment.get(commentId)
                commentInstance.save()
            } else {
                BlogComment blogCommentInstance = new BlogComment()
                blogCommentInstance.blog = blogInstance
                blogCommentInstance.comment = commentInstance
                blogCommentInstance.save()
            }
            redirect uri: blogInstance.searchLink()
        }
    }

}
