/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content

import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(PageLayoutController)
@Mock([PageLayout])
class PageLayoutControllerSpec extends Specification implements BaseTestSetup {

    // Index action
    void "test index action for valid JSON response"() {
        given: 'PageLayout Instances'
        createInstances("PageLayout", 5)
        assert PageLayout.count() == 5

        when: 'Index action is hit'
        controller.index()

        then: 'HttpStatus FOUND should be received'
        response.status == HttpStatus.FOUND.value()
        response.redirectedUrl == '/pageLayout/list'
    }

    // GetPageLayoutList action
    void "test getPageLayoutList action to get metaList"() {
        given: 'Some PageLayout Instance'
        createInstances("PageLayout", 5)
        assert PageLayout.count() == 5

        when: 'getPageLayoutList action is hit'
        controller.getPageLayoutList()

        then: 'A valid JSON response should be received'
        controller.response.json.pageLayoutList[0].id == 1
        controller.response.json.pageLayoutList[0].layoutName == 'TestPageLayout-1'
        response.status == HttpStatus.OK.value()
    }

    // Create action
    void "test create action when parameters are passed"() {
        given: 'Map parameters instance'
        Map params = [layoutName: 'TestPageLayout']

        when: 'Create action is called'
        controller.request.method = 'POST'
        controller.request.json = (params as JSON).toString()
        controller.create()

        then: 'Valid HttpStatus OK should be received with valid JSON response'
        controller.response.json.pageLayoutInstance.layoutName == 'TestPageLayout'
        controller.response.status == HttpStatus.OK.value()
    }

    // List action
    void "test list action when parameters are passed"() {
        given: 'Some PageLayout Instance'
        createInstances("PageLayout", 5)
        assert PageLayout.count() == 5

        when: 'List action is hit'
        controller.params.max = 5
        controller.list()

        then: 'HttpStatus OK should be received with valid JSON response'
        controller.response.json.instanceList[0].id == 1
        controller.response.json.instanceList[0].layoutName == 'TestPageLayout-1'
        response.contentType == 'application/json;charset=UTF-8'
        response.status == HttpStatus.OK.value()
    }

    // Save action
    @Unroll
    void "test save action when request parameters are passed"() {
        given: 'Map parameters instance'
        Map params = parameterMap

        when: 'Save action is hit'
        controller.request.method = 'POST'
        controller.request.json = params
        controller.save()

        then: 'Criteria check should be satisfied'
        response.status == responseResult

        where:
        parameterMap                    | responseResult
        [layoutName: 'TestPageLayout']  | HttpStatus.OK.value()
        [abc: 'TestPageLayout']         | HttpStatus.NOT_MODIFIED.value()
    }

    void "test save action when invalid request parameters are passed"() {
        given: 'Map parameters instance'
        Map params = [abc: 'TestPageLayout']

        when: 'Save action is hit'
        controller.request.method = 'POST'
        controller.request.json = params
        controller.save()

        then: 'HttpStatus NOT MODIFIED should be received'
        response.status == HttpStatus.NOT_MODIFIED.value()
    }

    // Show action
    void "test show action when PageLayout instance is passed"() {
        given: 'PageLayout instance'
        PageLayout pageLayoutInstance = getPageLayoutInstance(1)

        when: 'Show action is hit'
        controller.params.id = pageLayoutInstance.id
        controller.show()

        then: 'Valid JSON response should be received'
        controller.response.json.pageLayoutInstance.id == 1
        controller.response.json.pageLayoutInstance.layoutName == 'TestPageLayout-1'
        response.status == HttpStatus.OK.value()
    }

    // Edit action
    void "test edit action when pageLayoutInstance is passed"() {
        given: 'PageLayout instance'
        PageLayout validPageLayoutInstance = getPageLayoutInstance(1)

        when: 'Edit action is hit'
        controller.request.method = 'POST'
        controller.params.id = validPageLayoutInstance.id
        controller.edit()

        then: 'Valid JSON response should be received'
        controller.response.json.pageLayoutInstance.id == 1
        controller.response.json.pageLayoutInstance.layoutName == 'TestPageLayout-1'
        response.status == HttpStatus.OK.value()
    }

    void "test edit action when invalid pageLayoutInstance is passed"() {
        // Cannot be clubbed into one block for DRYness gives: Cannot issue a redirect(..) here.
        given: 'PageLayout instance'
        PageLayout invalidPageLayoutInstance = null

        when: 'Edit action is hit'
        controller.request.method = 'POST'
        controller.params.id = 1L
        controller.edit()

        then: 'Response redirect URL /list should be received'
        controller.response.redirectedUrl.contains('/list')
        response.status == HttpStatus.MOVED_TEMPORARILY.value()
    }

    // Update action
    void "test update action when pageLayoutInstance is passed"() {
        given: 'PageLayout and Map parameters instance'
        PageLayout pageLayoutInstance = getPageLayoutInstance(1)
        Map params = [layoutName: 'TestPageLayout']

        when: 'Update action is hit'
        controller.request.method = 'PUT'
        controller.request.json = params
        controller.params.id = pageLayoutInstance.id
        controller.update()

        then: 'HttpStatus OK should be received'
        pageLayoutInstance.layoutName == 'TestPageLayout'
        controller.response.json.status.name == 'OK'
        controller.response.status == HttpStatus.OK.value()
    }

    void "test update action when invalid pageLayoutInstance is passed"() {
        // Cannot be clubbed into one block for DRYness gives: Cannot issue a redirect(..) here.
        given: 'PageLayout and Map parameters instance'
        PageLayout pageLayoutInstance = null
        Map params = [layoutName: 'TestPageLayout']

        when: 'Update action is hit'
        controller.request.method = 'PUT'
        controller.request.json = params
        controller.params.id = 1L
        controller.update()

        then: 'HttpStatus NOT_ACCEPTABLE should be received'
        controller.response.redirectedUrl.contains('/list')
        response.status == HttpStatus.MOVED_TEMPORARILY.value()
    }

    // Delete action
    void "test delete action when pageLayoutInstance"() {
        given: 'PageLayout and Map parameters instance'
        PageLayout pageLayoutInstance = getPageLayoutInstance(1)
        assert PageLayout.count() == 1
        Map params = [layoutName: 'TestPageLayout']

        when: 'Delete action is hit'
        controller.request.method = 'DELETE'
        controller.params.id = pageLayoutInstance.id
        controller.delete()

        then: 'HttpStatus OK should be received'
        PageLayout.count() == 0
        controller.response.json.status.name == 'OK'
        controller.response.status == HttpStatus.OK.value()
    }
}
