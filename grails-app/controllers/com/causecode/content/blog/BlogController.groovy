/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.causecode.content.blog

import com.causecode.annotation.shorthand.ControllerShorthand
import com.causecode.content.blog.comment.BlogComment
import com.causecode.content.blog.comment.Comment
import com.causecode.content.meta.Meta
import com.lucastex.grails.fileuploader.FileUploaderService
import com.lucastex.grails.fileuploader.FileUploaderServiceException
import com.lucastex.grails.fileuploader.UFile
import com.naleid.grails.MarkdownService
import grails.converters.JSON
import grails.core.GrailsApplication
import grails.databinding.SimpleMapDataBindingSource
import grails.plugin.springsecurity.annotation.Secured
import grails.plugins.taggable.TagLink
import grails.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus

import java.text.DateFormatSymbols
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Provides default CRUD end point for Content Manager.
 *
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 */
@Secured(["ROLE_CONTENT_MANAGER"])
@Transactional(readOnly = true)
@ControllerShorthand(value = "blog")
class BlogController {

    static allowedMethods = [save: "POST", update: "PUT"]

    static responseFormats = ["json"]

    def contentService
    def springSecurityService
    def simpleCaptchaService
    def commentService
    def blogService
    def grailsWebDataBinder
    FileUploaderService fileUploaderService
    MarkdownService markdownService
    GrailsApplication grailsApplication

    private static String HTML_P_TAG_PATTERN = "(?s)<p(.*?)>(.*?)<\\/p>"

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
    def index(Integer max, Integer offset, String tag, String monthFilter, String queryFilter) {
        if (tag == 'undefined') tag = ''
        if (monthFilter == 'undefined') monthFilter = ''
        if (queryFilter == 'undefined') queryFilter = ''

        log.info "Parameters received to filter blogs : $params"
        long blogInstanceTotal
        int defaultMax = grailsApplication.config.cc.plugins.content.blog.list.max ?: 10
        List<String> monthFilterList = []
        String month, year

        if (monthFilter) {
            List blogFilter = monthFilter.split("-")
            month = blogFilter[0]
            year = blogFilter[1]
        }

        params.offset = offset ? offset : 0
        params.max = Math.min(max ?: defaultMax, 100)

        // TODO Improve blog string query to support GORM/Hibernate criteria query
        StringBuilder query = new StringBuilder("""SELECT distinct new Map(b.id as id, b.body as body, b.title as title,
                            b.subTitle as subTitle, b.author as author, b.lastUpdated as lastUpdated, b.publishedDate as publishedDate) FROM Blog b """)

        if (tag) {
            query.append(", ${TagLink.class.name} tagLink WHERE b.id = tagLink.tagRef ")
            query.append("AND tagLink.type = 'blog' AND tagLink.tag.name = '$tag' ")
        }
        if (monthFilter) {
            tag ? query.append(" AND ") : query.append(" WHERE ")
            query.append(" monthname(b.publishedDate) = '$month' AND year(b.publishedDate) = '$year'")
        }
        if (queryFilter) {
            tag ? query.append(" AND ") : (monthFilter ? query.append(" AND ") : query.append(" WHERE "))
            query.append(" b.title LIKE '%$queryFilter%' OR b.subTitle LIKE '%$queryFilter%' ")
            query.append(" OR b.body LIKE '%$queryFilter%' OR b.author LIKE '%$queryFilter%'")
        }

        if (contentService.contentManager) {
            blogInstanceTotal = tag ? Blog.countByTag(tag) : Blog.count()
        } else if (tag) {
            query.append("AND b.publish = true")
            blogInstanceTotal = Blog.findAllByTagWithCriteria(tag) {
                eq("publish", true)
            }.size()
        } else if (monthFilter) {
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
            // Convert markdown content into html format so that first paragraph could be extracted from it
            it.body = it.body?.markdownToHtml()
            // Extracting content from first <p> tag of body to display
            Matcher matcherTag = patternTag.matcher(it.body)
            it.body = matcherTag.find() ? matcherTag.group(2) : ""
        }

        if (monthFilter) {
            blogInstanceTotal = Blog.executeQuery(query.toString()).size()
        }

        List<Blog> blogInstanceList = []
        blogList.each {
            Blog blogInstance = Blog.get(it.id as long)
            if (!blogInstance) {
                log.debug("No blog found")
            }
            it.author = contentService.resolveAuthor(blogInstance)
            it.numberOfComments = BlogComment.countByBlog(blogInstance)
            it.blogImgSrc = blogInstance.blogImg?.path
            blogInstanceList.add(it)
        }

        List<Blog> publishDateList = Blog.createCriteria().list {
            projections {
                property("publishedDate")
            }
            eq("publish", true)
            isNotNull("publishedDate")
        }
        publishDateList.each { publishedDate ->
            monthFilterList.add(new DateFormatSymbols().months[publishedDate[Calendar.MONTH]] + "-" +
                    publishedDate[Calendar.YEAR])
        }

        Map result = [instanceList: blogInstanceList, totalCount: blogInstanceTotal, monthFilterList: monthFilterList.unique(),
            tagList: blogService.getAllTags()]

        /*
         * URL that contains '_escaped_fragment_' parameter, represents a request from a crawler and
         * any change in data model must be updated in the GSP.
         * Render GSP content in JSON format.
         */
        if (params._escaped_fragment_) {
            render(view: "list", model: result, contentType: "application/json")
            return
        }
        if (request.xhr) {
            render text: (result as JSON)
            return
        }
        String blogListUrl = grailsApplication.config.app.defaultURL + "/blog/list"
        redirect(url: blogListUrl, permanent: true)
    }

    def create() {
        [blogInstance: new Blog(params)]
    }

    /**
     * Create Blog instance and also sets tags for blog.
     */
    @Transactional
    def save() {
        Map requestData = request.JSON
        log.info "Parameters received to save blog: ${requestData}"
        List metaTypeList = requestData.metaList ? requestData.metaList.getAt("type") : []
        List metaValueList = requestData.metaList ? requestData.metaList.getAt("value") : []

        Blog.withTransaction { status ->
            Blog blogInstance = contentService.create(requestData, metaTypeList, metaValueList, Blog.class)
            UFile blogUfileInstance
            String blogImgFilePath = requestData['blogImgFilePath']
            blogInstance.contentType = blogService.findBlogContentTypeByValue(requestData.type.toString())

            try {
                if (blogImgFilePath) {
                    blogUfileInstance = fileUploaderService.saveFile(Blog.UFILE_GROUP, new File(blogImgFilePath))
                }
                blogInstance.blogImg = blogUfileInstance
                blogInstance.validate()
                if (blogInstance.hasErrors()) {
                    status.setRollbackOnly()
                    respond(blogInstance.errors)
                    return false
                }
                blogInstance.setTags(requestData.tags?.tokenize(",")*.trim())
                blogInstance.save(flush: true)
                respond([success: true])
            } catch (FileUploaderServiceException e) {
                log.debug "Unable to upload file", e
                blogInstance.errors.reject("Image couldn't be uploaded " + e.message)
                respond(blogInstance.errors)
                return false
            }
        }
    }

    @Secured(["permitAll"])
    def show() {
        Blog blogInstance = Blog.get(params.id)

        List tagList = blogService.getAllTags()
        def blogInstanceTags = blogInstance.tags

        // Convert markdown content into html format
        blogInstance.body = markdownService.markdown(blogInstance.body)

        List<Blog> blogInstanceList = Blog.findAllByPublish(true, [max: 5, sort: 'publishedDate', order: 'desc'])
        List<Meta> metaInstanceList = blogInstance.getMetaTags()

        Map result = [blogInstance: blogInstance, comments: null, tagList: tagList,
            blogInstanceList: blogInstanceList, blogInstanceTags: blogInstanceTags, metaList: metaInstanceList]

        /*
         * URL that contains '_escaped_fragment_' parameter, represents a request from a crawler and
         * any change in data model must be updated in the GSP.
         * Render GSP content in JSON format.
         */
        if (params._escaped_fragment_) {
            render(view: "show", model: result, contentType: "application/json")
            return
        }
        if (request.xhr) {
            render text: (result as JSON)
            return
        }
        String blogShowUrl = grailsApplication.config.app.defaultURL + "/blog/show/${blogInstance.id}"
        redirect(url: blogShowUrl, permanent: true)
    }

    /**
     * Update blog instance also sets tags for blog instance.
     */
    @Secured(['ROLE_CONTENT_MANAGER', 'ROLE_EMPLOYEE'])
    @Transactional
    def update() {
        Map requestData = request.JSON
        Blog blogInstance = Blog.get(requestData['id'] as long)
        bindData(blogInstance, requestData)
        String version = requestData['version']

        if (version != null) {
            if (blogInstance.version > version) {
                blogInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'blog.label')] as Object[],
                        "Another user has updated this Blog while you were editing")
                respond(blogInstance.errors)
                return false
            }
        }
        Blog.withTransaction { status ->
            String tags = requestData.remove("tags")
            List metaTypeList = requestData.metaList ? requestData.metaList.getAt("type") : []
            List metaValueList = requestData.metaList ? requestData.metaList.getAt("value") : []
            contentService.update(requestData, blogInstance, metaTypeList, metaValueList)
            blogInstance.contentType = blogService.findBlogContentTypeByValue(requestData.type.toString())

            String blogImgFilePath = requestData['blogImgFilePath']
            UFile blogUfileInstance
            try {
                if (blogImgFilePath != blogInstance.blogImg?.path) {
                    blogInstance.blogImg = blogImgFilePath ? fileUploaderService.saveFile(Blog.UFILE_GROUP, new File(blogImgFilePath)) : null
                }

                if (blogInstance.hasErrors()) {
                    status.setRollbackOnly()
                    respond(blogInstance.errors)
                    return false
                }

                blogInstance.setTags(tags?.tokenize(",")*.trim())
                blogInstance.save(flush: true)

                respond([success: true])
            } catch (FileUploaderServiceException e) {
                log.debug "Unable to upload file", e
                response.setStatus(HttpStatus.NOT_ACCEPTABLE.value())
                respond([message: e.message])
            }

        }
    }

    @Transactional
    def delete(Blog blogInstance) {
        try {
            contentService.delete(blogInstance)
            render status: HttpStatus.OK
        } catch (DataIntegrityViolationException e) {
            log.warn "Error deleting blog.", e
            render status: HttpStatus.NOT_MODIFIED
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
    def comment(Blog blogInstance, Long commentId) {
        Map requestData = request.JSON
        String errorMessage
        params.putAll(requestData)
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
            if (!captchaValid) {
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
            if (!commentInstance.save()) {
                status.setRollbackOnly()
                if (request.xhr) {
                    Map result = [message: "Something went wrong, Please try again later."]
                    render status: 403, text: result as JSON
                    return
                }
                redirect uri: blogInstance.searchLink()
                return
            }
            commentId = commentId ?: (params.commentId ? params.commentId as long : 0l)
            if (commentId) {
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
