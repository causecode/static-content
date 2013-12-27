package com.cc.content.blog

import static org.junit.Assert.*
import grails.test.mixin.*
import grails.test.mixin.support.*

import org.junit.*

import com.cc.content.blog.comment.BlogComment;

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(BlogController)
@Mock([Blog, BlogComment])
class BlogControllerTests {

    Blog blogInstance

    void setUp() {
        blogInstance = new Blog("title":"Targeting Test Types and/or Phases", "subTitle":"To execute the JUnit integration tests you can run",
        "body":"Grails organises tests by phase and by type. A test phase relates to the state of the Grails application during the tests, and the type relates to the testing mechanism.",
        "author":"Test User")
        assert blogInstance.save()
        assert blogInstance.id == 1
    }

    void testShowWithValidValues() {

        params.id = blogInstance.id
        blogInstance.setTags(["TestTag1", "TestTag2", "TestTag3"].tokenize(",")*.trim())

        def model = controller.show()

        assert model == null
    }
}
