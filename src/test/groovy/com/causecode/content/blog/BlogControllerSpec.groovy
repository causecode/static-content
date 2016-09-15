package com.causecode.content.blog

import com.causecode.content.Content
import com.causecode.content.blog.comment.CommentService
import com.naleid.grails.MarkdownService
import grails.plugins.taggable.Tag
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * Unit test for BlogController
 */
@TestFor(BlogController)
@Mock([Blog, Content, Tag])
class BlogControllerSpec extends Specification {

    Blog blogInstance

    def setup() {
        //--Mocking Services, Methods and Variables
        Blog.metaClass.static.getTagLinks = { tagService, obj ->
            return
        }

        Map args = getContentParams(1) + [publish: true, publishedDate: new Date()]
        blogInstance = new Blog(args)
        blogInstance.save(flush: true)
        assert blogInstance.id

        controller.commentService = [getComments: { Blog blogInstanceComment ->
            return []
        }] as CommentService

        controller.blogService = [getAllTags: { ->
            return []
        }] as BlogService

        controller.markdownService = [markdown: { String body ->
            return
        }] as MarkdownService

        Blog.metaClass.getMetaTags = { -> ["phonegap, phonegap 3, phonegap wrap web app in android"] }
        Blog.metaClass.tags = []
    }

    void "test show action with default parameters"() {
        given: "populating parameters"
        controller.params.id = blogInstance.id

        when: "blog ID parameter passed."
        controller.request.method = "GET"
        controller.show()

        then: "redirected to blog show angular based URL."
        controller.response.redirectedUrl.contains('/blog/show/' + blogInstance.id)
    }

    void "test show action for Google crawler request"() {
        given: "populating parameters"

        controller.params.id = blogInstance.id
        controller.params._escaped_fragment_ = true

        and: "populating show GSP content"
        List<Blog> blogInstanceList = Blog.findAllByPublish(true, [max: 5, sort: 'publishedDate', order: 'desc'])

        when: "blog ID and _escaped_fragment_ parameter passed."
        controller.request.method = "GET"
        controller.show()

        then: "redirected to blog show angular based URL."
        controller.modelAndView.model.blogInstance == blogInstance
        controller.modelAndView.model.comments == null
        controller.modelAndView.model.tagList == []
        controller.modelAndView.model.blogInstanceList == blogInstanceList
        controller.modelAndView.viewName == '/blog/show'
    }

    void "test show action for ajax request"() {
        given: "populating parameters"
        controller.params.id = blogInstance.id

        when: "ajax request comes in for show action"
        controller.request.method = "GET"
        controller.request.makeAjaxRequest()
        controller.show()

        then: "should respond json data containing page instance"
        controller.response.json["blogInstance"]
        controller.response.json["blogInstance"].id
        controller.response.json["blogInstance"].title == blogInstance.title
        controller.response.json["blogInstance"].body == blogInstance.body
        controller.response.json["blogInstance"].subTitle == blogInstance.subTitle
        controller.response.json["blogInstance"].publish
    }

    void "test show action of blog"() {
        given: "populating parameters"
        controller.params.id = blogInstance.id

        when: "blog ID parameter passed."
        controller.request.method = "GET"
        controller.show()

        then: "redirected to blog show angular based URL."
        controller.response.redirectedUrl.contains('/blog/show/' + blogInstance.id)
    }

    private Map getContentParams(Integer i) {
        return [
                title   : "Targeting Test $i Types and/or Phases",
                author  : "Test User",
                subTitle: "To execute the JUnit integration test $i",
                body    : "Grails organises tests by phase and by type. The state of the Grails application.",
        ]
    }
}