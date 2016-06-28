/*
 * Copyright (c) 2011-Present, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.causecode.content.page

import org.springframework.http.HttpStatus

import com.causecode.content.Content
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.*

/**
 * Unit Test for PageController
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(PageController)
@Mock([Page, Content])
class PageControllerSpec extends Specification {

    PageController controller
    @Shared Page pageInstance
    @Shared Page pageInstance1
    @Shared Page pageInstance2
    @Shared Page pageInstance3
    @Shared Page pageInstance4

    def setup() {
        controller = new PageController()

        pageInstance1 = new Page(getContentParams(1))
        pageInstance2 = new Page(getContentParams(2))
        pageInstance3 = new Page(getContentParams(3))
        pageInstance4 = new Page(getContentParams(4))
        assert pageInstance1.save()
        assert pageInstance2.save()
        assert pageInstance3.save()
        assert pageInstance4.save()
        assert pageInstance1.id
        assert pageInstance2.id
        assert pageInstance3.id
        assert pageInstance4.id
    }

    void "test show action without ID parameter"() {
        given: "Before controller show called"

        when: "page id parameter not passed"
        controller.request.method = "GET"
        controller.show()

        then: "should throw equiredPropertyMissing Exception"
        controller.response.status == HttpStatus.NOT_ACCEPTABLE.value()
        controller.response.json.message == controller.message(code: 'page.not.found')
    }

    @Unroll
    void "test show action with default parameters"() {
        given: "populating parameters"
        controller.params.id = pageInstance.id

        when: "page id parameter passed"
        controller.request.method = "GET"
        controller.show()

        then: "redirected to page show angular based URL."
        controller.response.redirectedUrl.contains("/page/show/${pageInstance.id}")

        where:
        pageInstance << [pageInstance1, pageInstance2, pageInstance3, pageInstance4]
    }

    void "test show action for Google crawler request"() {
        given: "populating parameters"
        controller.params.id = pageInstance1.id
        controller.params._escaped_fragment_ = true

        when: "page ID and _escaped_fragment_ parameter passed."
        controller.request.method = "GET"
        controller.show()

        then: "should render show GSP content in JSON format."
        controller.modelAndView.model.pageInstance == pageInstance1
        controller.modelAndView.viewName == "/page/show"

    }

    @Unroll
    void "test show action for ajax request"() {
        given: "populating parameters"
        controller.params.id = pageInstance.id

        when: "ajax request comes in for show action"
        controller.request.method = "GET"
        controller.request.makeAjaxRequest()
        controller.show()

        then: "should respond json data containing page instance"
        controller.response.json
        controller.response.json["id"]
        controller.response.json["title"] == pageInstance.title
        controller.response.json["body"] == pageInstance.body
        controller.response.json["subTitle"] == pageInstance.subTitle

        where:
        pageInstance << [pageInstance1,pageInstance2,pageInstance3,pageInstance4]
    }

    private Map getContentParams(Integer i) {
        return [
                title: "Targeting Test $i Types and/or Phases",
                author: "Test User",
                subTitle: "To execute the JUnit integration test $i",
                body: "Grails organises tests by phase and by type. The state of the Grails application."]
    }
}
