package com.causecode.content.blog.comment

import com.causecode.BaseTestSetup
import com.causecode.content.PageLayout
import com.causecode.content.blog.Blog
import com.causecode.content.blog.BlogContentType
import com.causecode.content.page.Page
import grails.plugins.taggable.Tag
import grails.plugins.taggable.TagLink
import grails.plugins.taggable.TaggableService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.http.HttpStatus
import spock.lang.Specification

@Mock([Comment, BlogComment, Blog, Tag, TaggableService, TagLink, CommentService])
@TestFor(CommentService)
class CommentServiceSpec extends Specification implements BaseTestSetup {

    // DeleteNestedComment method
    void "test deleteNestedComment method to delete commentInstance passed"() {
        given: 'Comment instance' // Error Comment.findAllByReplyTo(commentInstance) not returning proper list
        Comment commentInstance = getCommentInstance(1)
        commentInstance.replyTo = getCommentInstance(2)
        commentInstance.save(flush: true)

        assert commentInstance.id

        when: 'DeleteNestedComment method is called'
        Comment.count() == 2
        service.deleteNestedComment(commentInstance, true)

        then: 'Comment count should be 1'
        Comment.count() == 1
    }

    // GetComments method
    void "test getComment method to get comment of blog instance"() {
        given: 'Comment instance'
        Comment commentInstance = getCommentInstance(1)
        Blog blogInstance = getBlogInstance(1)
        BlogComment blogCommentInstance = new BlogComment([blog: blogInstance, comment: commentInstance])
        blogCommentInstance.save(flush: true)

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
        // Comment.findAllByReplyTo(commentInstance) not returning value list
        given: 'Comment instance'
        Comment commentInstance = getCommentInstance(1)
        commentInstance.replyTo = getCommentInstance(2)
        commentInstance.save(flush: true)

        assert commentInstance.id

        when: 'GetCommentData method is called'
        Map resultMap = service.getCommentData(commentInstance)

        then: 'ResultMap received should be valid'
        resultMap.subject.contains('Test comment subject')
        resultMap.email == 'comment-1@test.com'
    }
}
