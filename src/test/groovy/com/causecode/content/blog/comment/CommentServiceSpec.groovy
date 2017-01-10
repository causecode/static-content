/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.blog.comment

import com.causecode.content.BaseTestSetup
import com.causecode.content.blog.Blog
import grails.plugins.taggable.Tag
import grails.plugins.taggable.TagLink
import grails.plugins.taggable.TaggableService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * This is Unit test file for CommentService class.
 */
@Mock([Comment, BlogComment, Blog, Tag, TaggableService, TagLink, CommentService])
@TestFor(CommentService)
class CommentServiceSpec extends Specification implements BaseTestSetup {

    // DeleteNestedComment method
    void "test deleteNestedComment method to delete commentInstance passed"() {
        given: 'Comment instance'
        Comment commentInstance = getCommentInstance(1)
        commentInstance.replyTo = getCommentInstance(2)
        commentInstance.save()

        assert commentInstance.id

        when: 'DeleteNestedComment method is called'
        Comment.count() == 2
        service.deleteNestedComment(commentInstance, true)

        then: 'Comment count should be 1'
        Comment.count() == 1
    }

    // GetComments method
    void "test getComments method to get comments of blog instance"() {
        given: 'Comment instance'
        Comment commentInstance = getCommentInstance(1)
        Blog blogInstance = getBlogInstance(1)
        BlogComment blogCommentInstance = new BlogComment([blog: blogInstance, comment: commentInstance])
        blogCommentInstance.save()

        assert blogCommentInstance.id

        when: 'getComments method is called'
        List resultCommentList = service.getComments(blogInstance)

        then: 'resultCommentList should not be empty'
        resultCommentList.name == ['Test user-1']
        resultCommentList.email == ['comment-1@test.com']
        resultCommentList.commentText == ['Comment text 1']
    }

    // GetCommentData method
    void "test getCommentData when comment instance is passed"() {
        given: 'Comment instance'
        Comment commentInstance = getCommentInstance(1)
        commentInstance.replyTo = getCommentInstance(2)
        commentInstance.save()

        assert commentInstance.id

        when: 'GetCommentData method is called'
        Map resultMap = service.getCommentData(commentInstance)

        then: 'ResultMap received should be valid'
        resultMap.subject.contains('Test comment subject')
        resultMap.email == 'comment-1@test.com'

        when: 'GetCommentData method is called'
        Map resultMap1 = service.getCommentData(null)

        then: 'ResultMap received empty'
        assert resultMap1 == [:]
    }
}
