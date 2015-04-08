package com.cc.content.blog



import spock.lang.*


class BlogControllerSpec extends Specification {

    BlogController controller
    Blog blogInstance

    def setup() {
        controller = new BlogController()
    }

    def cleanup() {
    }

    void "test show: with default valid parameters passed."() {
        given:
        Blog blogInstance = new Blog("title":"Targeting Test Types and/or Phases", "author":"Test User",
            "subTitle":"To execute the JUnit integration tests you can run",
            "body":"Grails organises tests by phase and by type. The state of the Grails application .")
        assert blogInstance.save()
        assert blogInstance.id == 1

        controller.params.id = blogInstance.id

        when:
        controller.validate()
        controller.show()

        then: "Redirected to blog show angular based URL."
        controller.response.redirectedUrl.contains('/blog/show/' + blogInstance.id)
    }

    void "test show: with Internet bot crawler request."() {
        given:
        Blog blogInstance = new Blog("title":"Targeting Test Types and/or Phases", "author":"Test User",
            "subTitle":"To execute the JUnit integration tests you can run",
            "body":"Grails organises tests by phase and by type. The state of the Grails application .")
        assert blogInstance.save()
        assert blogInstance.id == 2

        controller.params.id = blogInstance.id
        controller.params._escaped_fragment_ = true

        when:
        controller.validate()
        controller.show()

        then: "Redirected to blog show angular based URL."
        controller.response.json == null
        controller.response.redirectedUrl.contains('/blog/show/' + blogInstance.id)
    }
}
