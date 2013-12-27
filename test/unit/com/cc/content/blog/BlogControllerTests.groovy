package com.cc.content.blog

import static org.junit.Assert.*
import grails.test.mixin.*
import grails.test.mixin.support.*

import org.junit.*

import com.cc.content.Content

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(BlogController)
class BlogControllerTests {

    void setUp() {
        // Setup logic here
    }

    void tearDown() {
        // Tear down logic here
    }

    void testSomething() {
        fail "Implement me"
    }

    void testShowWithValidValues() {
        def blogInstance = new Blog("title":"Targeting Test Types and/or Phases", "subTitle":"To execute the JUnit integration tests you can run",
        "body":"Grails organises tests by phase and by type. A test phase relates to the state of the Grails application during the tests, and the type relates to the testing mechanism.",
        "author":"Test User")
        assert blogInstance.save()
        assert blogInstance.id == 1

        params.id = blogInstance.id
        def model = controller.show()

        assert model == null
    }
}
