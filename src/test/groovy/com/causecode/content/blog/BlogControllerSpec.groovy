/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.blog

import com.causecode.content.BaseTestSetup
import com.causecode.content.Content
import com.causecode.content.ContentMeta
import com.causecode.content.ContentRevision
import com.causecode.content.ContentService
import com.causecode.content.blog.comment.BlogComment
import com.causecode.content.blog.comment.Comment
import com.causecode.fileuploader.FileUploaderService
import com.causecode.fileuploader.UFileType
import com.causecode.fileuploader.UploadFailureException
import com.naleid.grails.MarkdownService
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import com.causecode.fileuploader.UFile
import grails.plugins.taggable.Tag
import grails.plugins.taggable.TagLink
import grails.plugins.taggable.TaggableService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.util.mop.ConfineMetaClassChanges

/**
 * This is Unit test file for BlogController class.
 * Note: Unit test left for index action because of StringBuilder query implementation.
 */
@TestFor(BlogController)
@Mock([Blog, Content, Tag, TaggableService, TagLink, SpringSecurityService, ContentMeta, ContentRevision,
        BlogComment, Comment, UFile])
class BlogControllerSpec extends Specification implements BaseTestSetup {

    // Utility method
    @ConfineMetaClassChanges([BlogController])
    void mockedServices(Blog blogInstance, boolean exceptionBool = false, boolean saveBool = false) {
        // For save action call
        if (saveBool) {
            controller.metaClass.contentService = [create: { Map args, List metaTypes, List metaValues, Class clazz ->
                return blogInstance
            } ]
        } else {
            controller.metaClass.contentService = [update: { Map args, Content contentInstance, List metaTypes,
                List metaValues ->

                return blogInstance
            } ] as ContentService
        }

        // For fileUploaderException
        if (exceptionBool) {
            controller.metaClass.fileUploaderService = [saveFile: { String group, def file ->
                throw new UploadFailureException('Unable to upload file', new Throwable())
            } ]
        } else {
            controller.metaClass.fileUploaderService = [saveFile: { String group, def file ->
                return new UFile()
            } ]
        }

        // For blogService
        controller.metaClass.blogService = [findBlogContentTypeByValue: { String requestContentType ->
            return blogInstance.contentType
        } ]
    }

    // Create Action
    void "test create action for valid response when parameters are passed"() {
        given: 'Parameters for new Blog'
        controller.request.method = 'POST'
        Map contentParamsInstanceMap = getContentParams(1)
        controller.params.title = contentParamsInstanceMap.title
        controller.params.subTitle = contentParamsInstanceMap.subTitle
        controller.params.author = contentParamsInstanceMap.author
        controller.params.body = contentParamsInstanceMap.body

        when: 'Create action is hit'
        controller.create()

        then: 'A valid JSON response should be received with HttpStatus OK'
        controller.response.json.blogInstance.subTitle == 'To execute the JUnit integration test 1'
        controller.response.json.blogInstance.body.contains('Grails organises tests by phase and by type.')
        controller.response.status == HttpStatus.OK.value()
    }

    // Show Action
    void "test show action for selected blog id"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)
        assert blogInstance.toString() == 'Blog(MARKDOWN(2))'

        and: 'Mocked BlogService'
        controller.blogService = [getAllTags: { ->
            return []
        } ] as BlogService

        when: 'Action show is hit'
        controller.request.method = 'GET'
        controller.params.id = blogInstance.id
        controller.show()

        then: 'redirected to blog show angular based URL.'
        controller.response.redirectedUrl.contains('/blog/show/' + blogInstance.id)
    }

    void "test show action for selected blog id and convertToMarkDown as 'true'"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocked Blog & MarkdownService'
        controller.blogService = [getAllTags: { ->
            return []
        } ] as BlogService

        controller.markdownService = [markdown: { String body ->
            return
        } ] as MarkdownService

        when: 'blog id and convertToMarkdown parameter is passed'
        controller.request.method = 'GET'
        controller.params.id = blogInstance.id
        controller.params.convertToMarkdown = 'true'
        controller.show()

        then: 'redirected to blog show angular based URL.'
        controller.response.redirectedUrl.contains('/blog/show/' + blogInstance.id)
    }

    // Delete Action
    void "test delete action when blog instance is passed"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocked Content Service'
        controller.contentService = Mock(ContentService)
        1 * controller.contentService.delete(_) >> true

        when: 'Action delete is hit'
        controller.params.id = blogInstance.id
        controller.delete()

        then: 'A valid JSON response should be received with HttpStatus OK'
        controller.response.json.status.name == 'OK'
        controller.response.status == HttpStatus.OK.value()
    }

    void "test delete action when wrong blog instance is passed"() {
        given: 'Mocked ContentService'
        controller.contentService = [delete: { Blog blogInstance1 ->
            throw new DataIntegrityViolationException('Invalid blogInstance')
        } ] as ContentService

        when: 'Action delete is hit'
        controller.params.id = 1L
        controller.delete()

        then: 'Json stat' +
                '' +
                'us NOT_MODIFIED should be received'
        controller.response.json.status.name == 'NOT_MODIFIED'
    }

    // TODO can be removed once we write the test case for index action
    @ConfineMetaClassChanges([BlogController])
    void "test renderGSPContentAndBlogCustomURLRedirect method when viewType list is passed"() {
        given: 'Result map and view type'
        Map result = [content1: 'Content 1', content2: 'Content 2']
        String viewType = 'list'

        when: 'Method renderGSPContentAndBlogCustomURLRedirect is called with _escaped_fragment_ as true'
        controller.params._escaped_fragment_ = true
        boolean boolResult = controller.renderGSPContentAndBlogCustomURLRedirect(result, viewType)

        then: 'boolResult should be TRUE'
        boolResult == true
        controller.response.status == HttpStatus.OK.value()

        when: 'Method renderGSPContentAndBlogCustomURLRedirect is called with _escaped_fragment_ as false and ' +
                'Mocked Ajax request'
        controller.metaClass.getRequest = { ->
            [xhr: true]
        }
        controller.params._escaped_fragment_ = false
        boolean boolResult1 = controller.renderGSPContentAndBlogCustomURLRedirect(result, viewType)

        then: 'HttpStatus OK should be received with result map as response'
        boolResult1 == true
        controller.response.json.content2 == 'Content 2'
        controller.response.json.content1 == 'Content 1'
        controller.response.status == HttpStatus.OK.value()
    }

    // Index action
    void "test index action when user is not content manager"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocked service'

        controller.blogService = new BlogService()
        controller.contentService = Mock(ContentService)
        (1.._) * controller.contentService.isContentManager() >> false
        controller.blogService = [executeQuery: { String query, Map args ->
            return [blogInstance]
        }, getBlogSummaries:{List<Map> blogList, def patternTag ->
            return [blogInstance]
        }, getAllTags: {-> []},
        getCountByMonthFilter:  {Map monthYearFilterMapInstance, boolean publish -> 1
        }, getCountByQueryFilter: {String updateQueryFilter, boolean publish -> 1
        }] as BlogService

        when: 'No filter is passed'
        controller.request.makeAjaxRequest()
        controller.params._escaped_fragment_ = false
        controller.request.method = 'GET'
        controller.params.max = 10
        controller.params.offset = 0
        controller.index()

        then:
        response.json.totalCount == 1

        when: 'Search Query is specified'
        controller.request.makeAjaxRequest()
        controller.params._escaped_fragment_ = false
        controller.request.makeAjaxRequest()
        controller.params._escaped_fragment_ = false
        controller.params.queryFilter = 'Author'
        controller.index()

        then:
        response.json.totalCount == 1

        when: 'Month Filter is specified'
        controller.request.makeAjaxRequest()
        controller.params.queryFilter = ''
        controller.params._escaped_fragment_ = false
        controller.request.makeAjaxRequest()
        controller.params._escaped_fragment_ = false
        controller.params.monthFilter = new Date()
        controller.index()

        then:
        response.json.totalCount == 1

    }

    //Index action
    void "test index action when user is content manager"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocked service'

        controller.blogService = new BlogService()
        controller.contentService = Mock(ContentService)
        (1.._) * controller.contentService.isContentManager() >> true
        controller.blogService = [executeQuery: { String query, Map args ->
            return [blogInstance]
        }, getBlogSummaries:{List<Map> blogList, def patternTag ->
            return [blogInstance]
        }, getAllTags: {-> []},
                                  getCountByMonthFilter:  {Map monthYearFilterMapInstance, boolean publish -> 1
                                  }, getCountByQueryFilter: {String updateQueryFilter, boolean publish -> 1
        }] as BlogService

        when: 'No filter is passed'
        controller.request.makeAjaxRequest()
        controller.params._escaped_fragment_ = false
        controller.request.method = 'GET'
        controller.params.max = 10
        controller.params.offset = 0
        controller.index()

        then:
        response.json.totalCount == 1

        when: 'Search Query is specified'
        controller.request.makeAjaxRequest()
        controller.params._escaped_fragment_ = false
        controller.request.makeAjaxRequest()
        controller.params._escaped_fragment_ = false
        controller.params.queryFilter = 'Author'
        controller.index()

        then:
        response.json.totalCount == 1

        when: 'Month Filter is specified'
        controller.request.makeAjaxRequest()
        controller.params.queryFilter = ''
        controller.params._escaped_fragment_ = false
        controller.request.makeAjaxRequest()
        controller.params._escaped_fragment_ = false
        controller.params.monthFilter = new Date()
        controller.index()

        then:
        response.json.totalCount == 1

    }

    // Save Action
    void "test save action when blog instance is passed"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocked Services method call'
        mockedServices(blogInstance, false, true)

        when: 'Action save is hit'
        controller.request.method = 'POST'
        controller.request.json = [metalist: [:]]
        controller.save()

        then: 'Saved instance should be returned as response'
        response.json.id == 1
        response.json.title == 'Targeting Test 1 Types and/or Phases'
        controller.response.status == HttpStatus.OK.value()
    }

    @ConfineMetaClassChanges([ContentService, FileUploaderService, BlogService])
    void "test save action when blogImgFilePath is passed"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocked Services method call'
        mockedServices(blogInstance, false, true)

        when: 'Action save is hit'
        controller.request.method = 'POST'
        controller.request.json = [metalist: [:], blogImgFilePath: 'blog/img/file/path']
        controller.save()

        then: 'Saved instance should be returned as response'
        response.json.id == 1
        response.json.title == 'Targeting Test 1 Types and/or Phases'
        controller.response.status == HttpStatus.OK.value()
    }

    @ConfineMetaClassChanges([ContentService, FileUploaderService, BlogService])
    void "test save action when blog instance has errors"() {
        given: 'Blog instance'
        Blog blogInstance = new Blog()

        and: 'Mocked Services method call'
        mockedServices(blogInstance, false, true)

        when: 'Action save is hit'
        controller.request.method = 'POST'
        controller.request.json = [metalist: [:], blogImgFilePath: 'blog/img/file/path']
        controller.save()

        then: 'Error JSON response should be received'
        String responseJSONError = "${response.json.errors.message}"
        responseJSONError.contains('[Property [body] of class [class com.causecode.content.blog.Blog] cannot be null')
        controller.response.status == HttpStatus.UNPROCESSABLE_ENTITY.value()
    }

    // Custom method
    void "test createBlogCustomURLAndRedirect method when viewType is passed as 'list'"() {
        when: 'Method createBlogCustomURLAndRedirect is called with argument list'
        controller.createBlogCustomURLAndRedirect('list')

        then: 'redirected to blog show angular based URL.'
        controller.response.redirectedUrl.contains('/blog/list')
    }

    // Update Action
    @ConfineMetaClassChanges([ContentService, FileUploaderService, BlogService])
    void "test update action when blog instance is passed"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)
        assert blogInstance.subTitle == 'To execute the JUnit integration test 1'

        and: 'Mocked Services method call'
        mockedServices(blogInstance)

        when: 'Action update is hit'
        controller.request.method = 'PUT'
        controller.request.json = [id: blogInstance.id, metalist: [:], subTitle: 'To execute the JUnit integration']
        controller.update()

        then: 'JSON response success TRUE should be received with updated instance'
        blogInstance.subTitle == 'To execute the JUnit integration'
        response.json.success == true
        controller.response.status == HttpStatus.OK.value()
    }

    @ConfineMetaClassChanges([ContentService, FileUploaderService, BlogService])
    void "test update action when blog instance is passed with blogImgFilePath"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)
        UFile ufileInstance = new UFile([name: 'name', path: '/path', size: 1L, fileGroup: 'FileGroup',
                extension: 'TXT', type: UFileType.LOCAL])
        ufileInstance.save()

        assert ufileInstance.id

        blogInstance.blogImg = ufileInstance
        blogInstance.save()

        assert blogInstance.id

        and: 'Mocking Services'
        mockedServices(blogInstance)

        when: 'Action update is hit'
        controller.request.method = 'PUT'
        controller.request.json = [id: blogInstance.id, metalist: [:],
                blogImgFilePath: 'blog/img/file/path']
        controller.update()

        then: 'JSON response success TRUE should be received'
        controller.response.json.success == true
        controller.response.contentType == 'application/json;charset=UTF-8'
        controller.response.status == HttpStatus.OK.value()
    }

    @ConfineMetaClassChanges([ContentService, FileUploaderService, BlogService])
    void "test update action when blog instance is passed with invalidBlogInstance"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocked Services method call'
        mockedServices(blogInstance, true)

        when: 'Action update is hit'
        controller.request.method = 'PUT'
        controller.request.json = [id: blogInstance.id, metalist: [:],
                blogImgFilePath: 'blog/img/file/path']
        controller.update()

        then: 'Exception message should be received as JSON response'
        controller.response.json.message == 'Unable to upload file'
        controller.response.status == HttpStatus.NOT_ACCEPTABLE.value()
    }

    // Comment Action
    void "test comment action when id parameter is passed as null"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)
        Comment commentInstance = getCommentInstance(1)

        when: 'Action comment is hit'
        controller.request.method = 'POST'
        controller.request.makeAjaxRequest()
        controller.params.blogInstance = blogInstance.id
        controller.params.commentId = commentInstance.id
        controller.comment()

        then: 'Response status FORBIDDEN should be received'
        controller.response.json.message == 'Not enough parameters received to add comment.'
        controller.response.status == HttpStatus.FORBIDDEN.value()
    }

    void "test comment action when id parameter is passed"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)
        Comment commentInstance = getCommentInstance(1)

        and: 'Mocked Services method call'
        // Only possible way to mock as import is not available
        controller.simpleCaptchaService = [validateCaptcha: { String captcha ->
            return false
        } ]

        when: 'Action comment is hit with AJAX request'
        controller.request.method = 'POST'
        controller.request.makeAjaxRequest()
        controller.params.id = 1L
        controller.params.commentText = 'commentText'
        controller.params.email = 'joe@abc.com'
        controller.params.blogInstance = blogInstance.id
        controller.params.commentId = commentInstance.id
        controller.comment()

        then: 'Response status FORBIDDEN should be received'
        controller.response.json.message == 'Invalid captcha entered.'
        controller.response.status == HttpStatus.FORBIDDEN.value()
    }

    void "test comment action when id parameter is passed without AJAX request"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)
        Comment commentInstance = getCommentInstance(1)

        and: 'Mocked Services method call'
        // Only possible way to mock as import is not available
        controller.simpleCaptchaService = [validateCaptcha: { String captcha ->
            return false
        } ]

        blogInstance.contentService = Mock(ContentService)
        1 * blogInstance.contentService.createLink(_) >> '/blog/list'

        when: 'Action comment is hit without AJAX request'
        controller.request.method = 'POST'
        controller.params.id = 1L
        controller.params.commentText = 'commentText'
        controller.params.email = 'joe@abc.com'
        controller.params.blogInstance = blogInstance.id
        controller.params.commentId = commentInstance.id
        controller.comment()

        then: 'Redirect URL /blog/list should be received'
        controller.response.redirectedUrl.contains('/blog/list')
    }

    void "test comment action when id parameter is passed and validateCaptcha true"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)
        Comment commentInstance = getCommentInstance(1)

        and: 'Mocking SimpleCaptcha Service'
        // Only possible way to mock as import is not available
        controller.simpleCaptchaService = [validateCaptcha: { String captcha ->
            return true
        } ]

        blogInstance.contentService = Mock(ContentService)
        1 * blogInstance.contentService.createLink(_) >> '/blog/list'

        when: 'Action comment is hit without AJAX request'
        controller.request.method = 'POST'
        controller.params.id = 1L
        controller.params.commentText = 'commentText'
        controller.params.email = 'joe@abc.com'
        controller.params.blogInstance = blogInstance.id
        controller.params.commentId = commentInstance.id
        controller.comment()

        then: 'Redirect URL /blog/list should be received'
        controller.response.redirectedUrl.contains('/blog/list')
    }

    void "test comment action when id parameter is passed with validateCaptcha true and AJAX request"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)
        Comment commentInstance = getCommentInstance(1)

        and: 'Mocking SimpleCaptcha Service'
        // Only possible way to mock as import is not available
        controller.simpleCaptchaService = [validateCaptcha: { String captcha ->
            return true
        } ]

        when: 'Action comment is hit without AJAX request'
        controller.request.method = 'POST'
        controller.request.makeAjaxRequest()
        controller.params.id = 1L
        controller.params.blogInstance = blogInstance.id
        controller.params.commentId = commentInstance.id
        controller.comment()

        then: 'Response status FORBIDDEN should be received'
        controller.response.status == HttpStatus.FORBIDDEN.value()
    }

    void "test comment action when id parameter is passed with validateCaptcha true and without an AJAX request"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)
        Comment commentInstance = getCommentInstance(1)

        and: 'Mocking SimpleCaptcha Service'
        // Only possible way to mock as import is not available
        controller.simpleCaptchaService = [validateCaptcha: { String captcha ->
            return true
        } ]

        blogInstance.contentService = Mock(ContentService)
        1 * blogInstance.contentService.createLink(_) >> '/blog/list'

        when: 'Action comment is hit without AJAX request'
        controller.request.method = 'POST'
        controller.params.id = 1L
        controller.params.blogInstance = blogInstance.id
        controller.params.commentId = commentInstance.id
        controller.comment()

        then: 'Redirect URL /blog/list should be received'
        controller.response.redirectedUrl.contains('/blog/list')
    }

    void "test comment action when id is passed with validateCaptcha true, without an AJAX request and commentId 0"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocking SimpleCaptcha Service'
        // Only possible way to mock as import is not available
        controller.simpleCaptchaService = [validateCaptcha: { String captcha ->
            return true
        } ]

        blogInstance.contentService = Mock(ContentService)
        1 * blogInstance.contentService.createLink(_) >> '/blog/list'

        when: 'Action comment is hit without AJAX request'
        controller.request.method = 'POST'
        controller.params.id = 1L
        controller.params.commentText = 'commentText'
        controller.params.email = 'joe@abc.com'
        controller.params.blogInstance = blogInstance.id
        controller.params.commentId = 0
        controller.comment()

        then: 'Redirect URL should contains /blog/list'
        BlogComment.count() == 1
        controller.response.redirectedUrl.contains('/blog/list')
    }

    void "test comment action when id parameter is passed with validateCaptcha true, AJAX request and commentId 0"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocking Services'
        // Only possible way to mock as import is not available
        controller.simpleCaptchaService = [validateCaptcha: { String captcha ->
            return true
        } ]

        when: 'Action comment is hit without AJAX request'
        controller.request.method = 'POST'
        controller.request.makeAjaxRequest()
        controller.params.id = 1L
        controller.params.commentText = 'commentText'
        controller.params.email = 'joe@abc.com'
        controller.params.blogInstance = blogInstance.id
        controller.params.commentId = 0
        controller.comment()

        then: 'JSON response success TRUE should be received'
        controller.response.json.success == true
        BlogComment.count() == 1
        controller.response.status == HttpStatus.OK.value()
    }
}
