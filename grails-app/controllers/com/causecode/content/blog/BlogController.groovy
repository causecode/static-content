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
@Secured([BlogController.ROLE_CONTENT_MANAGER])
@Transactional(readOnly = true)
@ControllerShorthand(value = 'blog')
class BlogController {

    static allowedMethods = [save: 'POST', update: 'PUT']

    static responseFormats = ['json']

    ContentService contentService
    SpringSecurityService springSecurityService
    def simpleCaptchaService
    //def commentService
    BlogService blogService
    GrailsWebDataBinder grailsWebDataBinder
    FileUploaderService fileUploaderService
    MarkdownService markdownService
    GrailsApplication grailsApplication

    private static final String HTML_TAG_PATTERN = '(?s)<p(.*?)>(.*?)<\\/p>'
    private static final String PERMIT_ALL = 'permitAll'
    private static final String UNDEFINED = 'undefined'
    private static final String BLANK = ''
    private static final String COMMA = ','
    private static final String LIST = 'list'
    private static final String TYPE = 'type'
    private static final String CONTENT = 'content'
    private static final String BLOG_IMG_FILE_PATH = 'blogImgFilePath'
    private static final String UNABLE_TO_UPLOAD = 'Unable to upload file'
    private static final String TRUE = 'true'
    private static final String VERSION = 'version'
    private static final String SHOW_STRING = 'show'
    private static final String AND_PUBLISH_TRUE = 'AND b.publish = true'
    private static final String ROLE_CONTENT_MANAGER = 'ROLE_CONTENT_MANAGER'

    private static final int STATUS_403 = 403

    private static final Map FLUSH_TRUE = [flush: true]
    private static final Map SUCCESS_TRUE = [success: true]

    /**
     * Action list filters blog list with tags and returns Blog list and total matched result count.
     * If current user has role content manager then all Blog list will be returned otherwise blog with publish field
     * set to true will be returned.
     * @param max Pagination parameters used to specify maximum number of list items to be returned.
     * @param offset Pagination parameter
     * @param tag String Used to filter Blogs with tag.
     * @return Map containing blog list and total count.
     */
    @SuppressWarnings('ElseBlockBraces')
    @Secured([BlogController.PERMIT_ALL])
    def index(Integer max, Integer offset, String tag, String monthFilter, String queryFilter) {
        // To avoid ParameterReassignment
        def (updateTag, updateMonthFilter, updateQueryFilter) = [tag, monthFilter, queryFilter]

        if (tag == UNDEFINED) { updateTag = BLANK }
        if (monthFilter == UNDEFINED) { updateMonthFilter = BLANK }
        if (queryFilter == UNDEFINED) { updateQueryFilter = BLANK }

        log.info "Parameters received to filter blogs : $params"
        long blogInstanceTotal
        int defaultMax = grailsApplication.config.cc.plugins.content.blog.list.max ?: 10
        List<String> monthFilterList = []
        //String month, year
        Map monthYearFilterMapInstance =  [month: BLANK, year: BLANK]

        if (updateMonthFilter) {
            List blogFilter = updateMonthFilter.split('-')
            monthYearFilterMapInstance.month = blogFilter[0]
            monthYearFilterMapInstance.year = blogFilter[1]
        }

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
            query.append(AND_PUBLISH_TRUE)
            blogInstanceTotal = Blog.findAllByTagWithCriteria(updateTag) {
                eq('publish', true)
            }.size()
        } else if (updateMonthFilter) {
            query.append(AND_PUBLISH_TRUE)
            blogInstanceTotal = Blog.countByPublish(true)
        } else {
            (updateQueryFilter) ? query.append(' AND ') : query.append(' where ')
            query.append(' b.publish = true')
            blogInstanceTotal = Blog.countByPublish(true)
        }
        query.append(' order by b.dateCreated desc')

        List<Map> blogList = Blog.executeQuery(query.toString(), [max: params.max, offset: params.offset])
        Pattern patternTag = Pattern.compile(HTML_TAG_PATTERN)

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

        renderGSPContentAndBlogCustomURLRedirect(result, LIST)
    }

    def create() {
        def blogInstance = new Blog()
        bindData(blogInstance, params)

        [blogInstance: blogInstance]
    }

    /**
     * Create Blog instance and also sets tags for blog.
     */
    @SuppressWarnings('JavaIoPackageAccess')
    @Transactional
    def save() {
        Map requestData = request.JSON
        log.info "Parameters received to save blog: ${requestData}"

        List metaTypeList = requestData.metaList ? requestData.metaList[(TYPE)] : []
        List metaValueList = requestData.metaList ? requestData.metaList[(CONTENT)] : []

        Blog.withTransaction { status ->
            Blog blogInstance = contentService.create(requestData, metaTypeList, metaValueList, Blog)
            UFile blogUfileInstance
            String blogImgFilePath = requestData[BLOG_IMG_FILE_PATH]
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
                blogInstance.setTags(requestData.tags?.tokenize(COMMA)*.trim())
                blogInstance.save(FLUSH_TRUE)
                respond(SUCCESS_TRUE)
            } catch (FileUploaderServiceException e) {
                log.debug UNABLE_TO_UPLOAD, e
                blogInstance.errors.reject("Image couldn't be uploaded " + e.message)
                respond(blogInstance.errors)

                return false
            }
        }
    }

    @Secured([BlogController.PERMIT_ALL])
    def show() {
        Blog blogInstance = Blog.get(params.id)

        if (!blogInstance.publish && !springSecurityService.currentUser) {
            // Creating blogShowURL and redirect to the URL
            createBlogCustomURLAndRedirect(blogInstance)
        }

        List tagList = blogService.allTags
        def blogInstanceTags = blogInstance.tags

       // Convert markdown content into html format
       if (params.convertToMarkdown == TRUE) {
           blogInstance.body = markdownService.markdown(blogInstance.body)
       }

        List<Blog> blogInstanceList = Blog.findAllByPublish(true, [max: 5, sort: 'publishedDate', order: 'desc'])
        List<Meta> metaInstanceList = blogInstance.metaTags

        Map result = [blogInstance: blogInstance, comments: null,
                      tagList: tagList, blogInstanceList: blogInstanceList,
                      blogInstanceTags: blogInstanceTags, metaList: metaInstanceList]

        renderGSPContentAndBlogCustomURLRedirect(blogInstance, result, SHOW_STRING)

        return
    }

    private boolean renderGSPContentAndBlogCustomURLRedirect(Blog blogInstance = null, Map result, String viewType) {
    /*
     * URL that contains '_escaped_fragment_' parameter, represents a request from a crawler and
     * any change in data model must be updated in the GSP.
     * Render GSP content in JSON format.
     */
        if (params._escaped_fragment_) {
            render(view: viewType, model: result, contentType: 'application/json')
            return true
        }
        if (request.xhr) {
            render text: (result as JSON)
            return true
        }

        createBlogCustomURLAndRedirect(blogInstance, viewType)
    }

    // Creating blogListURL and redirect to the URL
    private boolean createBlogCustomURLAndRedirect(Blog blogInstance = null, String viewType) {
        String blogCustomURL

        if (viewType.isCase(LIST)) {
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
    @Secured([BlogController.ROLE_CONTENT_MANAGER, 'ROLE_EMPLOYEE'])
    @SuppressWarnings('JavaIoPackageAccess')
    @Transactional
    def update() {
        Map requestData = request.JSON
        Blog blogInstance = Blog.get(requestData['id'] as long)
        bindData(blogInstance, requestData)
        String version = requestData[VERSION]
        if (requestData.tags != blogInstance.tags) {
            blogInstance.setTags(requestData.tags?.tokenize(COMMA)*.trim())
        }

        if (version != null) {
            if ((blogInstance.version).toString() > version) {
                blogInstance.errors.rejectValue(VERSION, 'default.optimistic.locking.failure',
                        [message(code: 'blog.label')] as Object[],
                        'Another user has updated this Blog while you were editing')
                respond(blogInstance.errors)
                return false
            }
        }

        Blog.withTransaction { status ->
            List metaTypeList = requestData.metaList ? requestData.metaList[(TYPE)] : []
            List metaValueList = requestData.metaList ? requestData.metaList[(CONTENT)] : []

            contentService.update(requestData, blogInstance, metaTypeList, metaValueList)
            blogInstance.contentType = blogService.findBlogContentTypeByValue(requestData.type.toString())

            String blogImgFilePath = requestData[BLOG_IMG_FILE_PATH]
            // UFile blogUfileInstance
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

                blogInstance.save(FLUSH_TRUE)

                respond(SUCCESS_TRUE)
            } catch (FileUploaderServiceException e) {
                log.debug UNABLE_TO_UPLOAD, e
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
            log.warn 'Error deleting blog.', e
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
    @Secured([BlogController.PERMIT_ALL])
    def comment(Blog blogInstance, Long commentId) {
        Long updateCommentId = commentId
        Map requestData = request.JSON
        String errorMessage
        params.putAll(requestData)
        log.info "Parameters received to comment on blog: $params"
        if (!params.id) {
            errorMessage = 'Not enough parameters received to add comment.'
            log.info errorMessage
            Map result = [message: errorMessage]
            render status: STATUS_403, text: result as JSON
            return
        }

        Comment.withTransaction { status ->
            boolean captchaValid = simpleCaptchaService.validateCaptcha(params.captcha)
            if (!captchaValid) {
                if (request.xhr) {
                    Map result = [message: 'Invalid captcha entered.']
                    render status: STATUS_403, text: result as JSON
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
                    render status: STATUS_403, text: result as JSON
                    return
                }
                redirect uri: blogInstance.searchLink()
                return
            }

            updateCommentId = updateCommentId ?: (params.commentId ? params.commentId as long : 0L)
            if (updateCommentId) {
                commentInstance.replyTo = Comment.get(updateCommentId)
                commentInstance.save()
            } else {
                BlogComment blogCommentInstance = new BlogComment()
                blogCommentInstance.blog = blogInstance
                blogCommentInstance.comment = commentInstance
                blogCommentInstance.save()
            }
            log.info "Comment Added successfully."
            if (request.xhr) {
                render text: (SUCCESS_TRUE as JSON)
                return
            }
            redirect uri: blogInstance.searchLink()
        }
    }
}
