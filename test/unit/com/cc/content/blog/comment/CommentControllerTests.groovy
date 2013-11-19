package com.cc.content.blog.comment



import grails.test.mixin.*

import org.junit.*

import com.cc.content.blog.Blog
import com.cc.content.blog.BlogService

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(CommentController)
@Mock([Blog, Comment, BlogComment])
class CommentControllerTests {

    def blogServiceMock

    void setUp() {

        Blog blogInstance = new Blog(body: "Dummy Body", title: "Dummy title")

        assert blogInstance.save() != null

        Comment commentInstance = new Comment(commentText: "Duumy commentText", email: "dummyemail@gmail.com",
        name: "Dummy name", subject: "Dummy subject")
        assert commentInstance.save() != null

        BlogComment.findOrSaveByBlogAndComment(blogInstance, commentInstance)

        assert Blog.count() == 1
        assert Comment.count() == 1
        assert BlogComment.count() == 1

        def blogService = mockFor(BlogService)
        blogServiceMock.demand.deleteNestedComment {}   // mocking contact service method
        controller.blogService = blogServiceMock.createMock()
    }

    void testDeleteWithValidValues() {

        params.id = 1
        params.blogId = 1
        controller.delete()

        assert response.flash.message == "Comments deleted successfully"
        assert response.redirectedUrl == "/blog/list"
    }
}
