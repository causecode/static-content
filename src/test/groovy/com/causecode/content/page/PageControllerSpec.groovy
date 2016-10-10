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
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.util.Holders
import org.springframework.http.HttpStatus
import com.causecode.content.Content
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(PageController)
@Mock([Page, Content, PageLayout, ContentRevision, ContentMeta])
class PageControllerSpec extends Specification implements BaseTestSetup {

    def setup() {
        Holders.grailsApplication.config.cc.plugins.content.contentManagerRole = 'manager'
        SpringSecurityUtils.metaClass.static.ifNotGranted = { String role ->
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

        when: 'Create action is hit'
        controller.request.parameters = params
        controller.create()

        then: 'Valid HttpStatus should be received'
        controller.response.status == HttpStatus.OK.value()
    }

    // List action
    void "test list action when parameters are passed"() {
        given: 'Some Page Instance'
        createInstances("Page", 5)

        and: 'Mock createCriteria'
        def customCriteria = [list: { Object params = null, Closure cls -> [] }]
        Page.metaClass.static.createCriteria = {customCriteria}

        when: 'List action is hit'
        controller.list(5)

        then: 'A valid response should be received with HttpStatus OK'
        Page.count() == 5
        response.contentType == 'application/json;charset=UTF-8'
        response.status == HttpStatus.OK.value()
    }

    // Save action
    void "test save action when request parameters are passed"() {
        given: 'Map parameters instance'
        Page pageInstance = getPageInstance(1)
        String jsonRequest = '{"metaList": [{"type": "type", "content": "content"}]}'

        and: 'Mocking Services'
        controller.contentService = Mock(ContentService)
        1 * controller.contentService.create(_,_,_,_) >> pageInstance

        pageInstance.contentService = Mock(ContentService)
        1 * pageInstance.contentService.createLink(_) >> '/page/list'

        when: 'Save action is hit'
        controller.request.method = 'POST'
        controller.request.json = jsonRequest
        controller.save()

        then: 'A valid HttpStatus OK should be received'
        response.status == HttpStatus.FOUND.value()
        controller.response.redirectedUrl.contains('/page/list')
    }

    void "test save action when pageInstance cannot be saved"() {
        given: 'Map parameters instance'
        Page pageInstance = new Page()
        String jsonRequest = '{"metaList": [{"type": "type", "content": "content"}]}'

        and: 'Mocking Services'
        controller.contentService = Mock(ContentService)
        1 * controller.contentService.create(_,_,_,_) >> pageInstance

        when: 'Save action is hit'
        controller.request.method = 'POST'
        controller.request.json = jsonRequest
        controller.save()

        then: 'A valid HttpStatus OK should be received'
        response.status == HttpStatus.OK.value()
    }

    // Show action
    void "test show action when Page instance is passed"() {
        given: 'PageLayout instance'
        Page pageInstance = getPageInstance(1)
        pageInstance.body = 'mailto:jobs@causecode.com'
        pageInstance.save(com_causecode_BaseTestSetup__FLUSH_TRUE)

        assert pageInstance.id

        when: 'Show action is hit'
        controller.request.method = 'GET'
        controller.params.subject = 'subject'
        controller.show(pageInstance)

        then: 'Valid JSON response should be received'
        response.status == HttpStatus.MOVED_PERMANENTLY.value()
    }

    void "test show action when invalid instance is passed"() {
        given: 'PageLayout instance'
        Page pageInstance = null

        when: 'Show action is hit'
        controller.request.method = 'GET'
        controller.show(pageInstance)

        then: 'Valid JSON response should be received'
        response.status == HttpStatus.NOT_ACCEPTABLE.value()
    }

    void "test show action when params is passed"() {
        given: 'PageLayout instance'
        Page pageInstance = getPageInstance(1)

        when: 'Show action is hit'
        controller.request.method = 'GET'
        controller.params._escaped_fragment_ = true
        controller.show(pageInstance)

        then: 'Valid JSON response should be received'
        response.status == HttpStatus.OK.value()
    }

    void "test show action when AJAX request is made"() {
        given: 'PageLayout instance'
        Page pageInstance = getPageInstance(1)

        when: 'Show action is hit'
        controller.request.method = 'GET'
        controller.request.makeAjaxRequest()
        controller.show(pageInstance)

        then: 'Valid JSON response should be received'
        response.status == HttpStatus.OK.value()
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

        when: 'Edit action is hit'
        controller.edit(invalidPageInstance)

        then: 'Valid JSON response should be received'
        controller.response.json['message'] == 'page.not.found'
    }

    // Update action
    void "test update action when pageLayoutInstance is passed"() {
        given: 'Page and Map parameters instance'
        Page pageInstanceWithErrors = new Page()
        Page pageInstance = getPageInstance(1)
        Page pageInstanceVersionUpdated = getPageInstance(2)

        pageInstanceVersionUpdated.version = 2L
        pageInstanceVersionUpdated.save(com_causecode_BaseTestSetup__FLUSH_TRUE)
        assert pageInstanceVersionUpdated.id

        Page invalidPageInstance
        Map params = [layoutName: 'TestPageLayout']

        and: 'Mocking Services'
        controller.contentService = Mock(ContentService)
        1 * controller.contentService.update(_,_,_,_) >> pageInstance

        pageInstance.contentService = Mock(ContentService)
        1 * pageInstance.contentService.createLink(_) >> '/page/list'

        when: 'Update action is hit with pageInstance'
        controller.request.method = 'PUT'
        controller.request.json = params
        controller.update(pageInstance, 1L)

        then: 'Valid HttpStatus should be received'
        controller.response.status == HttpStatus.FOUND.value()
        controller.response.redirectedUrl.contains('/page/list')

        when: 'Update action is hit with invalid instance'
        controller.request.method = 'PUT'
        controller.request.json = params
        controller.update(invalidPageInstance, 1L)

        then: 'Valid JSON response should be received'
        controller.response.json['message'] == 'page.not.found'

        when: 'Update action is hit with pageInstance updated version'
        controller.request.method = 'PUT'
        controller.request.json = params
        controller.update(pageInstanceVersionUpdated, 1L)

        then: 'Valid JSON response should be received'
        controller.response.status == HttpStatus.NOT_ACCEPTABLE.value()
    }

    void "test update action when invalid instance is passed"() {
        given: 'Page and Map parameters instance'
        Page pageInstance = getPageInstance(1)
        Page pageInstanceWithErrors = new Page([abc: 'abc'])
        pageInstanceWithErrors.save(com_causecode_BaseTestSetup__FLUSH_TRUE)

        Map params = [layoutName: 'TestPageLayout']

        and: 'Mock services'
        controller.contentService = Mock(ContentService)
        1 * controller.contentService.update(_,_,_,_) >> pageInstanceWithErrors

        when: 'Update action is hit'
        controller.request.method = 'PUT'
        controller.request.json = params
        controller.update(pageInstance, 1L)

        then: 'Valid JSON response should be received'
        controller.response.status == HttpStatus.UNPROCESSABLE_ENTITY.value()
    }

    void "test update action when parameters are passed"() {
        given: 'Page and Map parameters instance'
        Page pageInstance = getPageInstance(1)

        and: 'Mocking Services'
        controller.contentService = Mock(ContentService)
        1 * controller.contentService.update(_,_,_,_) >> pageInstance

        pageInstance.contentService = Mock(ContentService)
        1 * pageInstance.contentService.createLink(_) >> '/page/list'

        when: 'Update action is hit'
        controller.request.method = 'PUT'
        controller.params.createRevision = true
        controller.update(pageInstance, 1L)

        then: 'Valid JSON response should be received'
        controller.response.status == HttpStatus.FOUND.value()
        controller.response.redirectedUrl.contains('/page/list')
    }

    // Delete action
    void "test delete action when pageInstance"() {
        given: 'Page and Map parameters instance'
        Page pageInstance = getPageInstance(1)
        Map params = [layoutName: 'TestPageLayout']

        controller.contentService = [delete: { Page pageInstance1 ->
            return
        }] as ContentService

        when: 'Delete action is hit'
        controller.request.method = 'DELETE'
        controller.delete(pageInstance)

        then: 'Valid HttpStatus should be received'
        controller.response.status == HttpStatus.OK.value()

        when: 'Delete action is called and RequiredPropertyMissingException is thrown'
        Page invalidPageInstance = null
        controller.request.method = 'DELETE'
        controller.delete(invalidPageInstance)

        then: 'A valid JSON response should be received'
        controller.response.status == HttpStatus.OK.value()
    }
}
