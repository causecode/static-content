/*
 * Copyright (c) 2011-Present, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.causecode.content.page

import com.causecode.content.ContentMeta
import com.causecode.content.ContentRevision
import com.causecode.content.ContentService
import com.causecode.content.meta.Meta
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import org.springframework.http.HttpStatus
import com.causecode.exceptions.RequiredPropertyMissingException
import com.causecode.content.Content
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.*
import spock.lang.Specification

/**
 * Unit Test for PageController
 */
@TestMixin(GrailsUnitTestMixin)
@TestFor(PageController)
@Mock([Page, Content, Meta,ContentService, ContentRevision, SpringSecurityService, ContentMeta])
class PageControllerSpec extends Specification {

    PageController controller
    @Shared
    Page pageInstance
    @Shared
    Page pageInstance1
    @Shared
    Page pageInstance2
    @Shared
    Page pageInstance3
    @Shared
    Page pageInstance4

    def output

    def setupSpec() {
        SpringSecurityUtils.metaClass.static.getIfNotGranted = { String role ->
            return true
        }

    }
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

        then: "should throw RequiredPropertyMissing Exception"
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
        pageInstance << [pageInstance1, pageInstance2, pageInstance3, pageInstance4]
    }

    void "CREATE: Testing create action"() {
        given: "Populating parameters"
        controller.params.id = pageInstance1.id
        when: "Calling create action"
        controller.create()
        then: "Should create pageInstance"
        pageInstance1 != null
        pageInstance1.title != null
        pageInstance1.body != null
    }

    void "INDEX: should redirect to list"() {
        when: "calling index action"
        controller.index()
        then: "Should redirect to list "
        response.redirectedUrl == "/page/list"
    }

    void "GETMETATYPELIST: MetaTypeList not empty model"() {
        given: "Populating Parameters"
        controller.params.id = pageInstance1.id
        new Meta(KEYWORDS: 'xyz').save(flush: true)
        new Meta(DESCRIPTION: 'Welcomes').save(flush: true)
        def expected = [metaTypeList: [new Meta(KEYWORDS: 'xyz'), new Meta(DESCRIPTION: 'Welcomes')]]
        when: "getMetaTypeList action called"
        def actual = controller.getMetaTypeList()
        then: ""
        actual != expected
    }

    void "EDIT: Testing an edit action without ID parameter"() {
        when: "action edit called"
        controller.edit()
        then: "should throw RequiredPropertyMissing Exception"
        controller.response.status == HttpStatus.NOT_ACCEPTABLE.value()
        controller.response.json.message == controller.message(code: 'page.not.found')
    }

    void "EDIT: Existing pageInstance with ID parameter"() {
        given: "Populating Parameters"
        controller.params.id = pageInstance1.id
        when: "save action called"
        controller.save()
        then: "Should get pageInstance and contentRevisonList"
        controller.edit() == [pageInstance: pageInstance1, contentRevisionList: []]
        pageInstance1 != null

    }

    void "SAVE: Should render create view when !page.save()"() {
        given: "Populating Parameters"
        Map requestData = [id: pageInstance1.id, title: ["World"], subTitle: ["Morning"], metaList: [[type: "keywords", value: "Hello"]]]
        controller.request.method = 'POST'
        when: "requestData is passed as JSON"
        controller.request.json = requestData
        controller.save()
        then: "Should redirect to create and view should not be equal to null "
        view != null
        view.endsWith "create"
        controller.flash.message == null
        pageInstance1 != null

    }

    void "SAVE: Should render show view when page.save() saved successfully "() {
        given: "Populating Parameters"
        Map requestData = [id: pageInstance1.id, title: ["Java"], subTitle: ["AngularJs"], body: ["Groovy"], metaList: [[type: "keywords", value: "Hello"]]]
        controller.request.method = 'POST'
        when: "Requestdata is passed as Json"
        controller.request.json = requestData
        controller.save()
        then: "Should get flash message"
        controller.flash.message != null
        response.redirectedUrl != null

    }

    void "DELETE: Should throw exception message when pageInstance eqauls null"() {
        when: "Populating parameters"
        controller.request.method = 'DELETE'
        controller.delete()
        then: "should throw RequiredPropertyMissing Exception"
        controller.response.status == HttpStatus.NOT_ACCEPTABLE.value()
        controller.response.json.message == controller.message(code: 'page.not.found')
    }

    void "DELETE: Should delete any Instance if pageInstance equals null"() {

        given: "Populating parameters"
        controller.params.id = pageInstance1.id
        pageInstance.save()
        when:
        controller.request.method = 'DELETE'
        controller.delete()
        then:
        controller.response.json.success == true
        where:
        pageInstance = new Page(title: "John", subTitle: "Doe", body: "Hello")
    }

    void "UPDATE: Should throw exception when pageInstance has Errors or equals null"() {
        when:
        controller.request.method = 'PUT'
        controller.update()
        then: "should throw RequiredPropertyMissing Exception"
        controller.response.status == HttpStatus.NOT_ACCEPTABLE.value()
        controller.response.json.message == controller.message(code: 'page.not.found')
    }

    void "UPDATE: Should correctly render edit view when employee.version > params.version"() {
        given: "Populating Parameters"
        Map requestData = [id: pageInstance4.id, title: ["Java"], subTitle: ["AngularJs"], body: ["Groovy"], metaList: [[type: "keywords", value: "Hello"]]]
        Map requestData1 = [id: pageInstance4.id, title: null, subTitle: ["AngularJs"], body: ["Groovy"], metaList: [[type: "keywords", value: "Hello"]]]
        controller.params.id = pageInstance4.id
        pageInstance4.version = 8
        controller.params.version = pageInstance4.version - 1
        controller.request.method = 'POST'
        controller.request.method = 'PUT'
        when: "Update action is called"
        controller.request.json = requestData
        controller.save()
        controller.update()
        then: "Should get field error named as version"
        pageInstance4.errors.getFieldError('version')
        pageInstance4.errors.errorCount != null

    }

    void "UPDATE: Testing pageInstance has Errors"() {
        given: "Populating Parameters"
        Map requestData1 = [id: pageInstance4.id, title: ["java"], subTitle: ["Javascript"], metaList: [[type: "keywords", value: "Hello"]]]
        controller.params.id = pageInstance4.id
        controller.params.version = null
        controller.params.createRevision = true
        controller.request.method = 'POST'
        controller.request.method = 'PUT'
        when: "requestData is passed as Json and update action is called"
        controller.request.json = requestData1
        controller.save()
        controller.update()
        then:
        controller.flash.message != null
        flash.message == "<em>java</em> Page updated successfully. Revision created successfully."

    }

    void "LIST: ifNotGranted "() {
        given: "Populating Parameters"
        controller.params.id = pageInstance1.id
        def pageInstanceList = [Page]
        println(">>>>>>>>>>>> Feel free to mock")
        def myCriteria = [
                list: { Closure cls ->
                    println(">>>>>>>>>>>> Yes i am mocked")
                    pageInstanceList
                }
        ]
        Page.metaClass.static.createCriteria = { myCriteria }
        when:
        controller.list()
        then:
        pageInstance1 != null
        cleanup:
        SpringSecurityUtils.metaClass = null
    }


    private Map getContentParams(Integer i) {
        return [
                title        : "Targeting Test $i Types and/or Phases",
                author       : "Test User",
                subTitle     : "To execute the JUnit integration test $i",
                body         : "Grails organises tests by phase and by type. The state of " +
                        "the Grails application.",
                publish      : true,
                dateCreated  : '2016-05-03',
                lastUpdated  : '2016-12-21',
                publishedDate: '2016-04-12'
        ]
    }
}