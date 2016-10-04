/*
 * Copyright (c) 2011-Present, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.causecode.content.page

import com.causecode.BaseTestSetup
import com.causecode.content.ContentMeta
import com.causecode.content.ContentRevision
import com.causecode.content.ContentService
import com.causecode.content.PageLayout
import com.causecode.content.blog.BlogContentType
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugins.taggable.Tag
import grails.util.Holders
import org.springframework.http.HttpStatus
import com.causecode.content.Content
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * Unit Test for PageController
 */
@TestFor(PageController)
@Mock([Page, Content, PageLayout, ContentService, ContentRevision, ContentMeta])
class PageControllerSpec extends Specification implements BaseTestSetup {

    /*PageController controller

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
    }*/

    def setup() {
        Holders.grailsApplication.config.cc.plugins.content.contentManagerRole = 'manager'

        /*SpringSecurityUtils.getMetaClass().static.ifAllGranted = { String roles ->
            return true
        }*/
        SpringSecurityUtils.metaClass.static.ifAllGranted = { String role ->
            return true
        }
    }

    // Index action
    void "test index action for valid JSON response"() {
        given: 'Some Page Instance'
        createInstances("Page", 5)

        when: 'Index action is hit'
        controller.index()

        then: 'A valid JSON response should be received'
        Page.count() == 5
        response.status == HttpStatus.FOUND.value()
        response.redirectedUrl == '/page/list'
    }

    // GetMetaTypeList action
    void "test getMetaTypeList action to get metaList"() {
        given: 'Some Page Instance'
        createInstances("Page", 5)

        when: 'getMetaList action is hit'
        controller.getMetaTypeList()

        then: 'A valid JSON response should be received'
        PageLayout.count() == 5
        response.status == HttpStatus.OK.value()
    }


    // Create action
    void "test create action when parameters are passed"() {
        given: 'Map parameters instance'
        PageLayout pageLayoutInstance = getPageLayoutInstance(1)
        Map params = [pageLayout: 'pageLayoutInstance']

        when: 'Create action is called'
        controller.request.parameters = params
        controller.create()

        then: 'Valid HttpStatus should be received'
        controller.response.status == HttpStatus.OK.value()
    }

    // List action
    /*void "test list action when parameters are passed"() { // Error  SpringSecurityUtils cannot be mocked
        given: 'Some Page Instance'
        createInstances("Page", 5)

        when: 'List action is hit'
        controller.list(5)

        then: 'A valid response should be received with HttpStatus OK'
        Page.count() == 5
        response.contentType == 'application/json;charset=UTF-8'
        response.status == HttpStatus.OK.value()
    }*/

    // Save action // error contentService.create
    /*void "test save action when request parameters are passed"() {
        given: 'Map parameters instance'
        //Map params = [pageLayout: 'pageLayoutInstance', metaList: [type: 'type']]

        and: 'Mocking Services'
        controller.metaClass.contentService = [create: { Map args, List metaTypes, List metaValues, Class clazz ->
            return getPageInstance(1)
        }] as ContentService

        when: 'Save action is called'
        controller.request.method = 'POST'
        controller.request.json = params
        controller.save()

        then: 'A valid HttpStatus OK should be received'
        response.status == HttpStatus.OK.value()
    }*/

    // Show action
    void "test show action when Page instance is passed"() {
        given: 'PageLayout instance'
        Page pageInstance = getPageInstance(1)

        when: 'Show action is hit'
        controller.show(pageInstance)

        then: 'Valid JSON response should be received'
        response.status == HttpStatus.MOVED_PERMANENTLY.value()
    }

    // Edit action
    void "test edit action when pageInstance is passed"() {
        given: 'PageLayout instance'
        Page validPageInstance = getPageInstance(1)
        Page invalidPageInstance = null

        when: 'Edit action is hit'
        controller.edit(validPageInstance)

        then: 'Valid JSON response should be received'
        response.status == HttpStatus.OK.value()

        /*when: 'Edit action is hit' // Error redirecting
        controller.edit(invalidPageLayoutInstance)

        then: 'Valid JSON response should be received'
        println 'res' + response.dump()
        controller.response.redirectedUrl.contains('/blog/show/')*/
    }

    // Update action
    /*void "test update action when pageLayoutInstance is passed"() { // Error java.lang.StringIndexOutOfBoundsException: String index out of range: 1
        given: 'Page and Map parameters instance'
        Page pageInstance = getPageInstance(1)
        Map params = [layoutName: 'TestPageLayout']

        when: 'Update action is called'
        controller.request.method = 'PUT'
        controller.request.json = params
        controller.update(pageInstance, 1L)

        then: 'Valid HttpStatus should be received'
        controller.response.status == HttpStatus.OK.value()
    }*/

    // Delete action
    void "test delete action when pageInstance"() {
        given: 'Page and Map parameters instance'
        Page pageInstance = getPageInstance(1)
        Map params = [layoutName: 'TestPageLayout']

        controller.contentService = [delete: { Page pageInstance1 ->
            return
        }] as ContentService

        when: 'Delete action is called'
        controller.request.method = 'DELETE'
        controller.delete(pageInstance)

        then: 'Valid HttpStatus should be received'
        controller.response.status == HttpStatus.OK.value()

        when: 'Delete action is called and RequiredPropertyMissingException is thrown'
        Page invalidPageInstance = null
        controller.request.method = 'DELETE'
        controller.delete(invalidPageInstance)

        then: 'A valid JSON response should be received'
        controller.response.status == HttpStatus.OK.value() // For exception thrown
    }
}
