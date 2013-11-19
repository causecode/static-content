package com.cc.content.blog

import grails.test.mixin.*
import grails.test.mixin.support.*

import org.grails.taggable.Tag
import org.grails.taggable.TagLink
import org.junit.*
import org.springframework.stereotype.Controller;

import com.cc.annotation.shorthand.ControllerShorthand;
import com.cc.content.Content;
import com.cc.content.ContentService
import com.cc.content.blog.comment.BlogComment;
import com.cc.content.blog.comment.Comment;

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(BlogController)
@Mock([Blog, TagLink, Tag, BlogComment, Comment])
class BlogControllerTests {

    def contentServiceMock
    def simpleCaptchaServiceMock

    void setUp() {
        // Setup logic here
        def contentService = mockFor(ContentService)
        contentServiceMock.demand.contentManager { -> false }   // mocking content service method
        controller.contentService = contentServiceMock.createMock()


    }

    def populateValidParams(params) {
        assert params != null
        params.title = "Dummy Title"
        params.subTitle = "Dummy SubTitle"
        params.body = "Dummy Body"
        params.publish = true
    }

    void testListWithoutParams() {

        populateValidParams(params)
        Blog blog = new Blog(params)

        assert blog.save() != null
        assert Blog.count() == 1
        assert blog.id == 1

        def model = controller.list()

        assert model.blogInstanceList.size() == 1
        assert model.blogInstanceTotal == 1
    }

    void createTag() {
        Tag tag = new Tag(name: "JAVA")
        assert tag.save() != null
        assert Tag.count() == 1
        assert tag.id == 1
    }

    void createTagLink() {
        TagLink tagLink = new TagLink(tag: Tag.get(1), tagRef: Blog.get(1), type: "blog")
        assert tagLink.save() != null
        assert TagLink.count() == 1
        assert tagLink.id == 1
    }

    void testListWithValidtParams() {

        populateValidParams(params)

        Blog blog = new Blog(params)
        assert blog.save() != null
        assert Blog.count() == 1
        assert blog.id == 1

        createTag()
        createTagLink()

        params.max = 2
        params.offset = 0
        params.tag = "JAVA"

        def model = controller.list()

        assert model.blogInstanceList.size() == 1
        assert model.blogInstanceTotal == 1
    }

    void testListWithValidtParamsForContentManager() {

        populateValidParams(params)

        Blog blog = new Blog(params)
        assert blog.save() != null
        assert Blog.count() == 1
        assert blog.id == 1

        createTag()
        createTagLink()

        params.max = 2
        params.offset = 0
        params.tag = "JAVA"

        def contentService = mockFor(ContentService)
        contentServiceMock.demand.contentManager { -> true }   // mocking content service method
        controller.contentService = contentServiceMock.createMock()

        def model = controller.list()

        assert model.blogInstanceList.size() == 1
        assert model.blogInstanceTotal == 1
    }

    void testCreate() {
        populateValidParams(params)
        def model = controller.create()

        assert model.blogInstance != null
    }

    void createBlogComment(Blog blog) {
        Comment commentInstance = new Comment(commentText: "Duumy commentText", email: "dummyemail@gmail.com",
        name: "Dummy name", subject: "Dummy subject")
        assert commentInstance.save()

        BlogComment.findOrSaveByBlogAndComment(blog, commentInstance)
    }

    void testShow() {
        populateValidParams(params)
        Blog blog = new Blog(params)

        assert blog.save() != null

        params.id = blog.id

        createBlogComment(blog)
        createBlogComment(blog)

        assert Comment.count() == 2
        assert BlogComment.count() == 2

        controller.validate()
        def model = controller.show()

        assert model.blogInstance.id == blog.id
        assert model.comments != null
        assert model.comments.size() == 2
    }

    void testEdit() {
        populateValidParams(params)
        Blog blog = new Blog(params)

        assert blog.save() != null

        params.id = blog.id

        controller.validate()
        def model = controller.edit()

        assert model.blogInstance == blog
    }

    void testDelete() {
        populateValidParams(params)
        Blog blog = new Blog(params)

        assert blog.save() != null
        assert Blog.count() == 1

        params.id = blog.id

        controller.validate()
        controller.delete()

        assert Blog.count() == 0
        assert Blog.get(blog.id) == null
        assert response.redirectedUrl == '/blog/list'
    }

    Content createContent() {
        populateValidParams(params)
        Content contentInstance = new Content(params)

        assert contentInstance.save() != null
        return contentInstance
    }

    void testSave() {
        defineBeans {
            contentService(ContentService)
        }

        populateValidParams(params)

        Blog blog = new Blog(params)

        assert blog.save() != null
        assert Blog.count() == 1

        params.id = blog.id

        Content contentInstance = createContent()
        def contentService = mockFor(ContentService)
        contentServiceMock.demand.create { -> contentInstance }   // mocking content service method
        controller.contentService = contentServiceMock.createMock()

        controller.validate()
        controller.save()

        assert response.redirectedUrl == '/blog/show/1'
        assert controller.flash.message != null
        assert Blog.count() == 2
    }

    void testUpdate() {

        populateValidParams(params)

        Blog blog = new Blog(params)

        assert blog.save() != null
        assert Blog.count() == 1

        params.id = blog.id

        params.tags = "JAVA,HTML"
        Content contentInstance = createContent()

        def contentService = mockFor(ContentService)
        contentServiceMock.demand.update { -> contentInstance }   // mocking content service method
        controller.contentService = contentServiceMock.createMock()

        controller.validate()
        controller.update()

        assert response.redirectedUrl == '/blog/show/1'
        assert controller.flash.message != null
        assert Blog.count() == 1
    }

    void testComment() {

        def simpleCaptchaService = mockFor(simpleCaptchaService)
        simpleCaptchaServiceMock.demand.validateCaptcha { -> true }   // mocking simpleCaptcha service method
        controller.simpleCaptchaService = simpleCaptchaServiceMock.createMock()
    }

    void testCommentForInvalidCaptcha() {

        populateValidParams(params)

        Blog blog = new Blog(params)

        assert blog.save() != null
        assert Blog.count() == 1

        params.id = blog.id

        def simpleCaptchaService = mockFor(simpleCaptchaService)
        simpleCaptchaServiceMock.demand.validateCaptcha { -> false }   // mocking simpleCaptcha service method
        controller.simpleCaptchaService = simpleCaptchaServiceMock.createMock()

        params.commentText = "Duumy commentText"
        params.email = "dummyemail@gmail.com"
        params.name = "Dummy name"
        params.subject = "Dummy subject"

        controller.validate()
        controller.comment()

        assert response.redirectedUrl == "/blog/show"
    }
}
