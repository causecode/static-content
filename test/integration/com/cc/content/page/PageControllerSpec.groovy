/*
 * Copyright (c) 2011-Present, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.page

import spock.lang.*

import com.cc.crm.BaseIntegrationTestCase

class PageControllerSpec extends BaseIntegrationTestCase {

    PageController controller
    Page pageInstance

    def setup() {
        controller = new PageController()

        pageInstance = new Page(getContentParams(1))
        assert pageInstance.save()
        assert pageInstance.id
    }

    def cleanup() {
    }

    void "test before interceptor with page ID"() {
        given: "Populating parameters"
        controller.params.id = pageInstance.id

        when: "Page id parameter passed"
        controller.validate()

        then: "Successfully bind page instance"
        controller.pageInstance
        controller.pageInstance.id == pageInstance.id
    }

    void "test before interceptor without page ID"() {
        when: "Page id parameter passed"
        controller.validate()

        then: "Should not bind page instance"
        controller.pageInstance == null
    }

    void "test show action with default parameteres"() {
        given: "Populating parameters"
        controller.params.id = pageInstance.id

        when: "Page id parameter passed"
        controller.request.method = "GET"
        controller.validate()
        controller.show()

        then: "Redirected to page show angular based URL."
        controller.response.redirectedUrl.contains('/page/show/' + pageInstance.id)
    }

    void "test show action for Google crawler request"() {
        given: "Populating parameters"
        controller.params.id = pageInstance.id
        controller.params._escaped_fragment_ = true

        when: "Page ID and _escaped_fragment_ parameter passed."
        controller.request.method = "GET"
        controller.validate()
        controller.show()

        then: "Should render show GSP content in JSON format."
        controller.modelAndView.model.pageInstance == pageInstance
        controller.modelAndView.viewName == "/page/show"
    }

    void "test show action for ajax request"() {
        given: "Populating parameters"
        controller.params.id = pageInstance.id

        when: "Ajax request comes in for show action"
        controller.request.method = "GET"
        controller.request.makeAjaxRequest()
        controller.validate()
        controller.show()

        then: "Should respond json data containing page instance"
        controller.response.json["pageInstance"]
        controller.response.json["pageInstance"].id
        controller.response.json["pageInstance"].title == pageInstance.title
        controller.response.json["pageInstance"].body == pageInstance.body
        controller.response.json["pageInstance"].subTitle == pageInstance.subTitle
    }
}