/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.blog

import com.causecode.BaseTestSetup
import com.causecode.content.Content
import com.causecode.content.ContentMeta
import com.causecode.content.ContentRevision
import com.causecode.content.ContentService
import com.causecode.content.blog.comment.BlogComment
import com.causecode.content.blog.comment.Comment
import com.lucastex.grails.fileuploader.FileUploaderServiceException
import com.lucastex.grails.fileuploader.UFileType
import com.naleid.grails.MarkdownService
import grails.plugin.springsecurity.SpringSecurityService
import com.lucastex.grails.fileuploader.UFile
import grails.plugins.taggable.Tag
import grails.plugins.taggable.TagLink
import grails.plugins.taggable.TaggableService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import spock.lang.Specification

@TestFor(BlogController)
@Mock([Blog, Content, Tag, TaggableService, TagLink, SpringSecurityService, ContentMeta, ContentRevision,
        BlogComment, Comment, UFile])
class BlogControllerSpec extends Specification implements BaseTestSetup {

    // Index Action
    /*void "test index action for valid response"() {
    // TODO for future reference Blocked because of StringQuery builder

        given: 'Instance'
        Blog blogInstance = getBlogInstance(0)

        and: 'Mocking ContentService'
        controller.contentService = [isContentManager: { ->
            return true
        }] as ContentService

        Blog.metaClass.executeQuery = {
            return
        }

        when: 'Index action is hit'
        controller.index()

        then: 'A valid JSON response should be received'
    }*/

    // Create Action
    void "test create action for valid response when parameters are passed"() {
        given: 'Parameters for new Blog'
        controller.request.method = "POST"
        Map contentParamsInstanceMap = getContentParams(1)
        controller.params.title = contentParamsInstanceMap.title
        controller.params.subTitle = contentParamsInstanceMap.subTitle
        controller.params.author = contentParamsInstanceMap.author
        controller.params.body = contentParamsInstanceMap.body

        when: 'Create action is hit'
        controller.create()

        then: 'A valid JSON response should be received'
        controller.response.status == HttpStatus.OK.value()
    }

    // Show Action
    void "test show action for selected blog id"() {
        given: "Blog instance"
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocked BlogService'
        controller.blogService = [getAllTags: { ->
            return []
        }] as BlogService

        when: "Action show is hit"
        controller.request.method = "GET"
        controller.params.id = blogInstance.id
        controller.show()

        then: "redirected to blog show angular based URL."
        controller.response.redirectedUrl.contains('/blog/show/' + blogInstance.id)
    }

    void "test show action for selected blog id and convertToMarkDown as 'true'"() {
        given: "Blog instance"
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocked Blog & MarkdownService'
        controller.blogService = [getAllTags: { ->
            return []
        }] as BlogService

        controller.markdownService = [markdown: { String body ->
            return
        }] as MarkdownService

        when: "blog id and convertToMarkdown parameter is passed"
        controller.request.method = "GET"
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

        and: 'Mocked Service'
        controller.contentService = Mock(ContentService)
        1 * controller.contentService.delete(_) >> true

        when: 'Action delete is hit'
        controller.delete(blogInstance)

        then: 'A valid JSON response should be received'
        controller.response.status == HttpStatus.OK.value()
    }

    void "test delete action when wrong blog instance is passed"() {
        given: 'Invalid Blog instance'
        Blog blogInstance = new Blog()

        and: 'Mock ContentService'
        controller.contentService = [delete: { Blog blogInstance1 ->
            throw new DataIntegrityViolationException('Invalid blogInstance')
        }] as ContentService

        when: 'Action delete is hit'
        controller.delete(blogInstance)

        then: 'A valid JSON response should be received'
        controller.response.status == HttpStatus.NOT_MODIFIED.value()
    }

    // TODO can be removed once we write the test case for index action
    void "test renderGSPContentAndBlogCustomURLRedirect method when viewType list is passed"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)
        Map result = [content1: 'Content 1', content2: 'Content 2']
        String viewType = 'list'

        when: 'Method renderGSPContentAndBlogCustomURLRedirect is called with _escaped_fragment_ as true'
        controller.params._escaped_fragment_ = true
        controller.renderGSPContentAndBlogCustomURLRedirect(result, viewType)

        then: 'A valid JSON response should be received'
        controller.response.status == HttpStatus.OK.value()

        when: 'Method renderGSPContentAndBlogCustomURLRedirect is called with _escaped_fragment_ as false and ' +
                'Mocked Ajax request'
        controller.metaClass.getRequest = { ->
            [xhr: true]
        }
        controller.params._escaped_fragment_ = false
        controller.renderGSPContentAndBlogCustomURLRedirect(result, viewType)

        then: 'A valid JSON response should be received'
        controller.response.status == HttpStatus.OK.value()
    }

    // Save Action
    void "test save action when blog instance is passed"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocking Services'
        controller.metaClass.contentService = [create: { Map args, List metaTypes, List metaValues, Class clazz ->
            return blogInstance
        }]

        controller.metaClass.blogService = [findBlogContentTypeByValue: { String requestContentType ->
            return blogInstance.contentType
        }]

        when: "Action save is hit"
        controller.request.method = "POST"
        controller.request.json = [metalist: [:]]
        controller.save()

        then: 'A valid JSON response should be received'
        response.json.success == true
        controller.response.status == HttpStatus.OK.value()
    }

    void "test save action when blogImgFilePath is passed"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocking Services'
        controller.metaClass.contentService = [create: { Map args, List metaTypes, List metaValues, Class clazz ->
            return blogInstance
        }]

        controller.metaClass.blogService = [findBlogContentTypeByValue: { String requestContentType ->
            return blogInstance.contentType
        }]

        controller.metaClass.fileUploaderService = [saveFile: { String group, def file ->
            return new UFile()
        }]

        when: "Action save is hit"
        controller.request.method = "POST"
        controller.request.json = [metalist: [:], blogImgFilePath: 'blog/img/file/path']
        controller.save()

        then: 'A valid JSON response should be received'
        response.json.success == true
        controller.response.status == HttpStatus.OK.value()
    }

    void "test save action when blog instance has errors"() {
        given: 'Blog instance'
        Blog blogInstance = new Blog()

        and: 'Mocking Services'
        controller.metaClass.contentService = [create: { Map args, List metaTypes, List metaValues, Class clazz ->
            return blogInstance
        }]

        controller.metaClass.blogService = [findBlogContentTypeByValue: { String requestContentType ->
            return blogInstance.contentType
        }]

        controller.metaClass.fileUploaderService = [saveFile: { String group, def file ->
            return new UFile()
        }]

        when: "Action save is hit"
        controller.request.method = "POST"
        controller.request.json = [metalist: [:], blogImgFilePath: 'blog/img/file/path']
        controller.save()

        then: 'A valid JSON response should be received'
        String responseJSONError = (response.json.errors.message).toString()
        responseJSONError.contains('[Property [body] of class [class com.causecode.content.blog.Blog] cannot be null')
        controller.response.status == HttpStatus.UNPROCESSABLE_ENTITY.value()
    }

    // Custom method
    void "test createBlogCustomURLAndRedirect method when viewType is passed as 'list'"() {
        when: 'Method createBlogCustomURLAndRedirect is called with argument list'
        controller.createBlogCustomURLAndRedirect('list')

        then: "redirected to blog show angular based URL."
        String defaultURL = grailsApplication.config.app.defaultURL
        controller.response.redirectedUrl.contains('/blog/list')
    }

    // Update Action
    void "test update action when blog instance is passed"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocking Services'
        controller.metaClass.contentService = [update: { Map args, Content contentInstance, List metaTypes,
                                                         List metaValues ->
            return blogInstance
        }] as ContentService

        controller.metaClass.blogService = [findBlogContentTypeByValue: { String requestContentType ->
            return blogInstance.contentType
        }]

        controller.metaClass.fileUploaderService = [saveFile: { String group, def file ->
            return new UFile()
        }]

        when: "Action update is hit"
        controller.request.method = "PUT"
        controller.request.json = [id: blogInstance.id, version: blogInstance.version, metalist: [:]]
        controller.update()

        then: 'A valid JSON response should be received'
        response.json.success == true
        controller.response.status == HttpStatus.OK.value()
    }

    void "test update action when blog instance is passed with version"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)
        blogInstance.version = 2
        blogInstance.save(com_causecode_BaseTestSetup__FLUSH_TRUE)

        assert blogInstance.id

        and: 'Mocking Services'
        controller.metaClass.contentService = [update: { Map args, Content contentInstance, List metaTypes,
                                                         List metaValues ->
            return blogInstance
        }] as ContentService

        controller.metaClass.blogService = [findBlogContentTypeByValue: { String requestContentType ->
            return blogInstance.contentType
        }]

        controller.metaClass.fileUploaderService = [saveFile: { String group, def file ->
            return new UFile()
        }]

        when: "Action update is hit"
        controller.request.method = "PUT"
        controller.request.json = [id: blogInstance.id, version: 0, metalist: [:]]
        controller.update()

        then: 'A valid JSON response should be received'
        controller.response.json.errors.field[0] == 'version'
        controller.response.json.errors.message[0] == 'Another user has updated this Blog while you were editing'
        response.status == HttpStatus.UNPROCESSABLE_ENTITY.value()
    }

    void "test update action when blog instance is passed with blogImgFilePath"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)
        UFile ufileInstance = new UFile([name: 'name', path: '/path', size: 1L, fileGroup: 'FileGroup'
                                         , extension: 'TXT', type: UFileType.LOCAL])
        ufileInstance.save(com_causecode_BaseTestSetup__FLUSH_TRUE)

        assert ufileInstance.id

        blogInstance.blogImg = ufileInstance
        blogInstance.save(com_causecode_BaseTestSetup__FLUSH_TRUE)

        assert blogInstance.id

        and: 'Mocking Services'
        controller.metaClass.contentService = [update: { Map args, Content contentInstance, List metaTypes,
                                                         List metaValues ->
            return blogInstance
        }] as ContentService

        controller.metaClass.blogService = [findBlogContentTypeByValue: { String requestContentType ->
            return blogInstance.contentType
        }]

        controller.metaClass.fileUploaderService = [saveFile: { String group, def file ->
            return new UFile()
        }]

        when: "Action update is hit"
        controller.request.method = "PUT"
        controller.request.json = [id: blogInstance.id, version: blogInstance.version, metalist: [:],
                                   blogImgFilePath: 'blog/img/file/path']
        controller.update()

        then: 'A valid JSON response should be received'
        controller.response.contentType == 'application/json;charset=UTF-8'
        controller.response.status == HttpStatus.OK.value()
    }

    void "test update action when blog instance is passed with invalidBlogInstance"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocking Services'
        controller.metaClass.contentService = [update: { Map args, Content contentInstance, List metaTypes,
                                                         List metaValues ->
            return blogInstance
        }] as ContentService

        controller.metaClass.blogService = [findBlogContentTypeByValue: { String requestContentType ->
            return blogInstance.contentType
        }]

        controller.metaClass.fileUploaderService = [saveFile: { String group, def file ->
            throw new FileUploaderServiceException('Unable to upload file')
        }]

        when: "Action update is hit"
        controller.request.method = "PUT"
        controller.request.json = [id: blogInstance.id, version: blogInstance.version, metalist: [:],
                                   blogImgFilePath: 'blog/img/file/path']
        controller.update()

        then: 'A valid JSON response should be received'
        controller.response.json.message == 'Unable to upload file'
        controller.response.status == HttpStatus.NOT_ACCEPTABLE.value()
    }

    // Comment Action
    void "test comment action when id parameter is passed as null"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        when: "Action comment is hit"
        controller.request.method = "POST"
        controller.request.makeAjaxRequest()
        controller.comment(blogInstance, (Long)getCommentInstance(1).id)

        then: 'A valid JSON response should be received'
        controller.response.json.message == 'Not enough parameters received to add comment.'
        controller.response.status == HttpStatus.FORBIDDEN.value()
    }

    void "test comment action when id parameter is passed"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocking Services'
        // Only possible way to mock as import is not available
        controller.simpleCaptchaService = [validateCaptcha: { String captcha ->
            return false
        }]

        when: "Action comment is hit with AJAX request"
        controller.request.method = "POST"
        controller.request.makeAjaxRequest()
        controller.params.id = 1L
        controller.params.commentText = 'commentText'
        controller.params.email = 'joe@abc.com'
        controller.comment(blogInstance, (Long)getCommentInstance(1).id)

        then: 'A valid JSON response should be received'
        controller.response.json.message == 'Invalid captcha entered.'
        controller.response.status == HttpStatus.FORBIDDEN.value()
    }

    void "test comment action when id parameter is passed without AJAX request"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocking Services'
        // Only possible way to mock as import is not available
        controller.simpleCaptchaService = [validateCaptcha: { String captcha ->
            return false
        }]

        blogInstance.contentService = Mock(ContentService)
        1 * blogInstance.contentService.createLink(_) >> '/blog/list'

        when: 'Action comment is hit without AJAX request'
        controller.request.method = "POST"
        controller.params.id = 1L
        controller.params.commentText = 'commentText'
        controller.params.email = 'joe@abc.com'
        controller.comment(blogInstance, (Long)getCommentInstance(1).id)

        then: 'Valid response should be received'
        controller.response.redirectedUrl.contains('/blog/list')
    }

    void "test comment action when id parameter is passed and validateCaptcha true"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocking Services'
        // Only possible way to mock as import is not available
        controller.simpleCaptchaService = [validateCaptcha: { String captcha ->
            return true
        }]

        blogInstance.contentService = Mock(ContentService)
        1 * blogInstance.contentService.createLink(_) >> '/blog/list'

        when: 'Action comment is hit without AJAX request'
        controller.request.method = "POST"
        controller.params.id = 1L
        controller.params.commentText = 'commentText'
        controller.params.email = 'joe@abc.com'
        controller.comment(blogInstance, (Long)getCommentInstance(1).id)

        then: 'Valid response should be received'
        controller.response.redirectedUrl.contains('/blog/list')
    }

    void "test comment action when id parameter is passed with validateCaptcha true and AJAX request"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocking Services'
        // Only possible way to mock as import is not available
        controller.simpleCaptchaService = [validateCaptcha: { String captcha ->
            return true
        }]

        when: 'Action comment is hit without AJAX request'
        controller.request.method = "POST"
        controller.request.makeAjaxRequest()
        controller.params.id = 1L
        controller.comment(blogInstance, (Long)getCommentInstance(1).id)

        then: 'Valid response should be received'
        controller.response.status == HttpStatus.FORBIDDEN.value()
    }

    void "test comment action when id parameter is passed with validateCaptcha true and without an AJAX request"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocking Services'
        // Only possible way to mock as import is not available
        controller.simpleCaptchaService = [validateCaptcha: { String captcha ->
            return true
        }]

        blogInstance.contentService = Mock(ContentService)
        1 * blogInstance.contentService.createLink(_) >> '/blog/list'

        when: 'Action comment is hit without AJAX request'
        controller.request.method = "POST"
        controller.params.id = 1L
        controller.comment(blogInstance, (Long)getCommentInstance(1).id)

        then: 'Valid response should be received'
        controller.response.redirectedUrl.contains('/blog/list')
    }

    void "test comment action when id is passed with validateCaptcha true, without an AJAX request and commentId 0"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        and: 'Mocking Services'
        // Only possible way to mock as import is not available
        controller.simpleCaptchaService = [validateCaptcha: { String captcha ->
            return true
        }]

        blogInstance.contentService = Mock(ContentService)
        1 * blogInstance.contentService.createLink(_) >> '/blog/list'

        when: 'Action comment is hit without AJAX request'
        controller.request.method = "POST"
        controller.params.id = 1L
        controller.params.commentText = 'commentText'
        controller.params.email = 'joe@abc.com'
        controller.comment(blogInstance, 0)

        then: 'Valid response should be received'
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
        }]

        when: 'Action comment is hit without AJAX request'
        controller.request.method = "POST"
        controller.request.makeAjaxRequest()
        controller.params.id = 1L
        controller.params.commentText = 'commentText'
        controller.params.email = 'joe@abc.com'
        controller.comment(blogInstance, 0)

        then: 'Valid response should be received'
        BlogComment.count() == 1
        controller.response.status == HttpStatus.OK.value()
    }

}
