/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.blog.comment

import com.causecode.BaseTestSetup
import com.causecode.content.ContentService
import com.causecode.content.blog.Blog
import com.causecode.seo.friendlyurl.FriendlyUrlService
import grails.plugins.taggable.Tag
import grails.plugins.taggable.TagLink
import grails.plugins.taggable.TaggableService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.http.HttpStatus
import spock.lang.Specification

@Mock([Comment, Blog, TaggableService, Tag, TagLink, BlogComment, ContentService, CommentService, FriendlyUrlService])
@TestFor(CommentController)
class CommentControllerSpec extends Specification implements BaseTestSetup {

    // Delete action
    void "test delete action when comment instance is passed with replyTo"() {
        given: 'Blog and Comment instance with replyTo'
        Blog blogInstance = getBlogInstance(1)
        Comment commentInstance = getCommentInstance(1)
        commentInstance.replyTo = getCommentInstance(2)
        commentInstance.save(com_causecode_BaseTestSetup__FLUSH_TRUE)

        assert commentInstance.id

        when: 'Delete action is called'
        Comment.count() == 2
        request.makeAjaxRequest()
        controller.delete(commentInstance.id, blogInstance.id)

        then: 'Comment should be deleted'
        Comment.count() == 1
        controller.response.status == HttpStatus.OK.value()
    }

    void "test delete action when comment instance is passed"() {
        given: 'Comment and blog instance'
        Comment commentInstance = getCommentInstance(1)
        Blog blogInstance = getBlogInstance(1)
        BlogComment blogCommentInstance = new BlogComment([blog: blogInstance, comment: commentInstance])
        blogCommentInstance.save(com_causecode_BaseTestSetup__FLUSH_TRUE)

        assert blogCommentInstance.id

        and: 'Mocking blogInstance'
        blogInstance.contentService = Mock(ContentService)
        1 * blogInstance.contentService.createLink(_) >> '/blog?id=1'

        when: 'Delete action is called'
        controller.delete(commentInstance.id, blogInstance.id)

        then: 'Comment should be deleted'
        Comment.count() == 0
        controller.response.redirectedUrl.contains('/blog?id=1')
    }
}
