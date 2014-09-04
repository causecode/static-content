package com.cc.content.blog

import grails.test.mixin.*
import grails.test.mixin.support.*

import org.grails.taggable.Tag
import org.grails.taggable.TagLink
import org.junit.*
import com.cc.content.blog.comment.CommentService

import com.cc.content.blog.comment.BlogComment

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(BlogController)
@Mock([Blog, BlogComment, Tag, TagLink, CommentService])
class BlogControllerTests {

    Blog blogInstance
    def mockedCriteria
    def blogService

    void setUp() {
        blogInstance = new Blog("title":"Targeting Test Types and/or Phases", "subTitle":"To execute the JUnit integration tests you can run",
        "body":"Grails organises tests by phase and by type. A test phase relates to the state of the Grails application .",
        "author":"Test User")
        assert blogInstance.save()
        assert blogInstance.id == 1
    }

    void testShowWithValidValues() {

        def blogService = mockFor(BlogService)
        blogService.demand.getAllTags {-> return []}
        controller.blogService = blogService.createMock()

        params.id = blogInstance.id

        Tag tag = new Tag([name: "Test Tag1", tagRef: 1, type: "blog"])
        assert tag.save()

        Blog.metaClass.getTags = { ->
            ["Test Tag1"]
        }

        TagLink tagLinkInstance = new TagLink([tag: tag, tagRef: 1 , type: "blog"])
        assert tagLinkInstance.save()

        controller.validate()
        def model = controller.show()

        assert model != null
        assert model.blogInstance.id == blogInstance.id
        assert model.comments == []
        assert model.tagList == []
        assert model.blogInstanceList != null
    }

}
