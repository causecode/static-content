package com.cc.content.page



import com.cc.content.page.Page;
import com.cc.content.page.PageController;

import spock.lang.*

/**
 *
 */
class PageControllerSpec extends Specification {

    PageController controller
    Page pageInstance

    def setup() {
        controller = new PageController()

        pageInstance = new Page("title":"Targeting Test Types and/or Phases", "author":"Test User",
            "subTitle":"To execute the JUnit integration tests you can run",
            "body":"Grails organises tests by phase and by type. The state of the Grails application .")
        assert pageInstance.save()
        assert pageInstance.id == 1
    }

    def cleanup() {
    }

    void "test show: with default valid parameters passed."() {
        given:
        controller.params.id = pageInstance.id

        when:
        controller.validate()
        controller.show()

        then: "Redirected to page show angular based URL."
        controller.response.redirectedUrl.contains('/page/show/' + pageInstance.id)
    }
}