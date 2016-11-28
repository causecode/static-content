/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.blog

import com.causecode.annotation.shorthand.ControllerShorthand
import com.causecode.content.ContentService
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
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import grails.web.databinding.GrailsWebDataBinder
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus

import java.util.regex.Pattern

/**
 * Provides default CRUD end point for Content Manager.
 *
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 */
@Secured(['ROLE_CONTENT_MANAGER'])
@Transactional(readOnly = true)
@ControllerShorthand(value = 'blog')
class BlogController {

    static allowedMethods = [save: 'POST', update: 'PUT']

    static responseFormats = ['json']

    ContentService contentService
    SpringSecurityService springSecurityService
    def simpleCaptchaService
    BlogService blogService
    GrailsWebDataBinder grailsWebDataBinder
    FileUploaderService fileUploaderService
    MarkdownService markdownService
    GrailsApplication grailsApplication

    /**
     * Action list filters blog list with tags and returns Blog list and total matched result count.
     * If current user has role content manager then all Blog list will be returned otherwise blog with publish field
     * set to true will be returned.
     *
     * Note: Action refactored for implementing modularity and improvement of code. New variables and methods added.
     * Kindly refer previous commits for reference.
     *
     * New methods added: 1. blogService.queryModifierBasedOnFilter(...)
     *                    2. blogService.getBlogInstanceList(...)
     *                    3. blogService.updatedMonthFilterListBasedOnPublishedDate(...)
     *                    4. renderGSPContentAndBlogCustomURLRedirect(...)
     *
     * @param max Pagination parameters used to specify maximum number of list items to be returned.
     * @param offset Pagination parameter
     * @param tag String Used to filter Blogs with tag.
     * @return Map containing blog list and total count.
     */
    @SuppressWarnings(['ElseBlockBraces'])
    @Secured(['PERMIT_ALL'])
    def index(Integer max, Integer offset, String tag, String monthFilter, String queryFilter) {
        // To avoid Parameter Reassignment
        def (updateTag, updateMonthFilter, updateQueryFilter) = [tag, monthFilter, queryFilter]

        updateTag = (tag == 'undefined') ? '' : tag
        updateMonthFilter = (monthFilter == 'undefined') ? '' : monthFilter
        updateQueryFilter = (queryFilter == 'undefined') ? '' : queryFilter

        log.info "Parameters received to filter blogs : $params"
        long blogInstanceTotal
        int defaultMax = grailsApplication.config.cc.plugins.content.blog.list.max ?: 10
        List<String> monthFilterList = []

        Map monthYearFilterMapInstance = updateMonthFilter ? getMonthYearFilterMapInstance(updateMonthFilter) :
                [month: '', year: '']

        params.offset =  offset ?: 0
        params.max = Math.min(max ?: defaultMax, 100)

        // TODO Improve blog string query to support GORM/Hibernate criteria query
        StringBuilder query = new StringBuilder('SELECT distinct new Map(b.id as id, b.body as body,' +
                ' b.title as title, b.subTitle as subTitle, b.author as author,' +
                ' b.lastUpdated as lastUpdated, b.publishedDate as publishedDate) FROM Blog b')

        // Modifying query based on filters
        query = blogService.queryModifierBasedOnFilter(query, updateTag, monthYearFilterMapInstance, updateQueryFilter,
                updateMonthFilter)

        // Modifying query and blogInstance Total based on Role @here : ROLE_CONTENT_MANAGER
        if (contentService.contentManager) {
            blogInstanceTotal = updateTag ? Blog.countByTag(updateTag) : Blog.count()
        } else if (updateTag) {
            query.append('AND b.publish = true')
            blogInstanceTotal = Blog.findAllByTagWithCriteria(updateTag) {
                eq('publish', true)
            }.size()
        } else if (updateMonthFilter) {
            query.append('AND b.publish = true')
            blogInstanceTotal = Blog.countByPublish(true)
        } else {
            (updateQueryFilter) ? query.append(' AND ') : query.append(' where ')
            query.append(' b.publish = true')
            blogInstanceTotal = Blog.countByPublish(true)
        }
        query.append(' order by b.dateCreated desc')

        List<Map> blogList = Blog.executeQuery(query.toString(), [max: params.max, offset: params.offset])
        Pattern patternTag = Pattern.compile('(?s)<p(.*?)>(.*?)<\\/p>')

        if (updateMonthFilter) {
            blogInstanceTotal = Blog.executeQuery(query.toString()).size()
        }

        // Get blogInstanceList
        List<Blog> blogInstanceList = blogService.getBlogInstanceList(blogList, patternTag)

        // Updated monthFilterList based on Blog published date
        monthFilterList = blogService.updatedMonthFilterListBasedOnPublishedDate(monthFilterList)

        Map result = [instanceList: blogInstanceList, totalCount: blogInstanceTotal,
                      monthFilterList: monthFilterList.unique(),
                      tagList: blogService.allTags]

        // Render GSP content and redirection
        return renderGSPContentAndBlogCustomURLRedirect(result, 'list')
    }

    Map getMonthYearFilterMapInstance(String updateMonthFilter) {
        Map monthYearFilterMapInstance = [month: '', year: '']
        List blogFilter = updateMonthFilter.split('-')
        monthYearFilterMapInstance.month = blogFilter[0]
        monthYearFilterMapInstance.year = blogFilter[1]

        return monthYearFilterMapInstance
    }

    def create(Blog blogInstance) {
        respond([blogInstance: blogInstance])

        return
    }

    /**
     * Create Blog instance and also sets tags for blog.
     */
    @SuppressWarnings('JavaIoPackageAccess')
    @Transactional
    def save() {
        Map requestData = request.JSON
        log.info "Parameters received to save blog: ${requestData}"

        List metaTypeList = requestData.metaList ? requestData.metaList[('type')] : []
        List metaValueList = requestData.metaList ? requestData.metaList[('content')] : []

        Blog.withTransaction { status ->
            Blog blogInstance = contentService.create(requestData, metaTypeList, metaValueList, Blog)
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
                blogInstance.setTags(requestData.tags?.tokenize(',')*.trim())
                blogInstance.save([flush: true])
                respond([success: true])
            } catch (FileUploaderServiceException e) {
                log.debug 'Unable to upload file', e
                blogInstance.errors.reject("Image couldn't be uploaded " + e.message)
                respond(blogInstance.errors)

                return false
            }
        }

        return true
    }

    @Secured(['PERMIT_ALL'])
    def show() {
        Blog blogInstance = Blog.get(params.id)

        if (!blogInstance.publish && !springSecurityService.currentUser) {
            // Creating blogShowURL and redirect to the URL
            createBlogCustomURLAndRedirect(blogInstance)
        }

        List tagList = blogService.allTags
        def blogInstanceTags = blogInstance.tags

        // Convert markdown content into html format
        if (params.convertToMarkdown == 'true') {
           blogInstance.body = markdownService.markdown(blogInstance.body)
        }

        List<Blog> blogInstanceList = Blog.findAllByPublish(true, [max: 5, sort: 'publishedDate', order: 'desc'])
        List<Meta> metaInstanceList = blogInstance.metaTags

        Map result = [blogInstance: blogInstance, comments: null,
            tagList: tagList, blogInstanceList: blogInstanceList,
            blogInstanceTags: blogInstanceTags, metaList: metaInstanceList
        ]

        renderGSPContentAndBlogCustomURLRedirect(blogInstance, result, 'show')

        return
    }

    /*
     * URL that contains '_escaped_fragment_' parameter, represents a request from a crawler and
     * any change in data model must be updated in the GSP.
     * Render GSP content in JSON format.
     */
    private boolean renderGSPContentAndBlogCustomURLRedirect(Blog blogInstance = null, Map result, String viewType) {
        if (params._escaped_fragment_) {
            render(view: viewType, model: result, contentType: 'application/json')
            return true
        }
        if (request.xhr) {
            render text: (result as JSON)
            return true
        }

        return createBlogCustomURLAndRedirect(blogInstance, viewType)
    }

    // Creating blogListURL and redirect to the URL
    private boolean createBlogCustomURLAndRedirect(Blog blogInstance = null, String viewType) {
        String blogCustomURL

        if (viewType.isCase('list')) {
            blogCustomURL = grailsApplication.config.app.defaultURL + '/blog/list'
        } else {
            blogCustomURL = grailsApplication.config.app.defaultURL + "/blog/show/${blogInstance.id}"
        }

        redirect(url: blogCustomURL, permanent: true)

        return true
    }

    /**
     * Update blog instance also sets tags for blog instance.
     */
    @Secured(['ROLE_CONTENT_MANAGER', 'ROLE_EMPLOYEE'])
    @SuppressWarnings('JavaIoPackageAccess')
    @Transactional
    def update() {
        Map requestData = request.JSON
        Blog blogInstance = Blog.get(requestData['id'] as long)
        bindData(blogInstance, requestData)

        if (requestData.tags != blogInstance.tags) {
            blogInstance.setTags(requestData.tags?.tokenize(',')*.trim())
        }

        Blog.withTransaction { status ->
            List metaTypeList = requestData.metaList ? requestData.metaList[('type')] : []
            List metaValueList = requestData.metaList ? requestData.metaList[('content')] : []

            contentService.update(requestData, blogInstance, metaTypeList, metaValueList)
            blogInstance.contentType = blogService.findBlogContentTypeByValue(requestData.type.toString())

            String blogImgFilePath = requestData['blogImgFilePath']

            try {
                if (blogImgFilePath != blogInstance.blogImg?.path) {
                    blogInstance.blogImg = blogImgFilePath ? fileUploaderService.saveFile(Blog.UFILE_GROUP,
                            new File(blogImgFilePath)) : null
                }

                if (blogInstance.hasErrors()) {
                    status.setRollbackOnly()
                    respond(blogInstance.errors)
                    return false
                }

                blogInstance.save([flush: true])

                respond([success: true])
            } catch (FileUploaderServiceException e) {
                log.debug 'Unable to upload file', e
                response.setStatus(HttpStatus.NOT_ACCEPTABLE.value())
                respond([message: e.message])
            }
        }

        return true
    }

    @Transactional
    def delete(Blog blogInstance) {
        try {
            contentService.delete(blogInstance)
            respond([status: HttpStatus.OK])
        } catch (DataIntegrityViolationException e) {
            log.warn 'Error deleting blog.', e
            respond([status: HttpStatus.NOT_MODIFIED])
        }

        return
    }

    /**
     * This action adds comments for blog with verified Captcha and redirects to blog show page.
     * If captcha is invalid comments will not be added for blog.
     * @param commentId Identity of Comment domain.If these parameter received then newly created comment will be added
     * as reply to given comment instance otherwise comment will be added as reference to blog instance instance.
     */
    @Transactional
    @Secured(['PERMIT_ALL'])
    def comment(Blog blogInstance, Long commentId) {
        Long updateCommentId = commentId
        String errorMessage

        log.info "Parameters received to comment on blog: $params"

        if (!params.id) {
            errorMessage = 'Not enough parameters received to add comment.'
            log.info errorMessage
            Map result = [message: errorMessage]
            render status: 403, text: result as JSON

            return
        }

        Comment.withTransaction { status ->
            boolean captchaValid = simpleCaptchaService.validateCaptcha(params.captcha)
            if (!captchaValid) {
                if (request.xhr) {
                    Map result = [message: 'Invalid captcha entered.']
                    render status: 403, text: result as JSON
                    return
                }
                flash.message = message(code: 'default.captcha.invalid.message', args: [message(code: 'captcha.label')])
                redirect uri: blogInstance.searchLink()
                return
            }

            Comment commentInstance = new Comment()
            grailsWebDataBinder.bind(commentInstance, params as SimpleMapDataBindingSource,
                    ['subject', 'name', 'email', 'commentText'])
            if (!commentInstance.save()) {
                status.setRollbackOnly()
                if (request.xhr) {
                    Map result = [message: 'Something went wrong, Please try again later.']
                    render status: 403, text: result as JSON
                    return
                }
                redirect uri: blogInstance.searchLink()
                return
            }

            if (updateCommentId) {
                commentInstance.replyTo = Comment.get(updateCommentId)
                commentInstance.save([flush: true])
            } else {
                BlogComment blogCommentInstance = new BlogComment()
                blogCommentInstance.blog = blogInstance
                blogCommentInstance.comment = commentInstance
                blogCommentInstance.save([flush: true])
            }
            log.info 'Comment Added successfully.'
            if (request.xhr) {
                render text: ([success: true] as JSON)
                return
            }
            redirect uri: blogInstance.searchLink()
        }

        return
    }
}
