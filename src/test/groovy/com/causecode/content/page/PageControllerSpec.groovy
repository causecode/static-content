/*
 * Copyright (c) 2011-Present, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.page

import com.causecode.content.BaseTestSetup
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
import spock.util.mop.ConfineMetaClassChanges

/**
 * This is Unit test file for PageController class.
 */
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
        given: 'Page Instance'
        createInstances('Page', 5)
        assert Page.count() == 5

        when: 'Index action is hit'
        controller.index()

        then: 'Redirect URL should contain /page/list'
        response.status == HttpStatus.FOUND.value()
        response.redirectedUrl == '/page/list'
    }

    // GetMetaTypeList action
    void "test getMetaTypeList action to get metaList"() {
        given: 'Page Instance'
        createInstances('Page', 5)
        assert PageLayout.count() == 5

        when: 'getMetaList action is hit'
        controller.metaTypeList

        then: 'A valid JSON response should be received'
        controller.response.json.metaTypeList[0] == 'keywords'
        controller.response.json.metaTypeList[1] == 'description'
        response.status == HttpStatus.OK.value()
    }

    // Create action
    void "test create action when parameters are passed"() {
        given: 'Map parameters instance'
        PageLayout pageLayoutInstance = getPageLayoutInstance(1)
        assert pageLayoutInstance.toString() == 'PageLayout(TestPageLayout-1, 1)'

        Map params = [pageLayout: 'pageLayoutInstance']

        when: 'Create action is hit'
        controller.request.method = 'POST'
        controller.request.parameters = params
        controller.create()

        then: 'JSON response with HttpStatus OK should be received'
        controller.response.json.pageInstance.publish == false
        controller.response.status == HttpStatus.OK.value()
    }

    // List action
    /*
     * Note: Suppressed GrailsMaxForListQueries warning here, as this warning should prompt in case of criteria query,
     *       here its just a controller action call
     */
    @ConfineMetaClassChanges([Page])
    @SuppressWarnings(['GrailsMaxForListQueries'])
    void "test list action when parameters are passed"() {
        given: 'Page Instances'
        createInstances('Page', 5)
        assert Page.count() == 5

        and: 'Mock createCriteria'
        def customCriteria = [list: { Object params = null, Closure cls ->
            []
        } ]
        Page.metaClass.static.createCriteria = {
            customCriteria
        }

        when: 'List action is hit'
        controller.request.method = 'GET'
        controller.params.max = 5
        controller.list()

        then: 'A valid response should be received with HttpStatus OK'
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
        1 * controller.contentService.create(_, _, _, _) >> pageInstance

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
        1 * controller.contentService.create(_, _, _, _) >> pageInstance

        when: 'Save action is hit'
        controller.request.method = 'POST'
        controller.request.json = jsonRequest
        controller.save()

        then: 'JSON response with HttpStatus OK should be received'
        controller.response.json.errors.errors[0].message.contains('Property [body] of class [class com.causecode.')
        controller.response.json.errors.errors[0].field == 'body'
        response.status == HttpStatus.OK.value()
    }

    // Show action
    void "test show action when Page instance is passed"() {
        given: 'PageLayout instance'
        Page pageInstance = getPageInstance(1)
        pageInstance.body = 'mailto:jobs@causecode.com'
        pageInstance.save()

        assert pageInstance.id

        when: 'Show action is hit'
        controller.request.method = 'GET'
        controller.params.subject = 'subject'
        controller.params.id = pageInstance.id
        controller.show()

        then: 'Response status MOVED_PERMANENTLY should be received'
        response.status == HttpStatus.MOVED_PERMANENTLY.value()
    }

    void "test show action when invalid instance is passed"() {
        when: 'Show action is hit'
        controller.request.method = 'GET'
        controller.params.id = 1L
        controller.show()

        then: 'Response status NOT_ACCEPTABLE should be received'
        controller.response.json.message == 'page.not.found'
        response.status == HttpStatus.NOT_ACCEPTABLE.value()
    }

    void "test show action when params is passed"() {
        given: 'PageLayout instance'
        Page pageInstance = getPageInstance(1)

        when: 'Show action is hit'
        controller.request.method = 'GET'
        controller.params._escaped_fragment_ = true
        controller.params.id = pageInstance.id
        controller.show()

        then: 'JSON response with HttpStatus OK should be received'
        controller.response.json.model.pageInstance.subTitle == 'To execute the JUnit integration test 1'
        controller.response.json.model.pageInstance.publish == true
        response.status == HttpStatus.OK.value()
    }

    void "test show action when AJAX request is made"() {
        given: 'PageLayout instance'
        Page pageInstance = getPageInstance(1)

        when: 'Show action is hit'
        controller.request.method = 'GET'
        controller.request.makeAjaxRequest()
        controller.params.id = pageInstance.id
        controller.show()

        then: 'Valid JSON response should be received'
        controller.response.json.body.contains('Grails organises tests by phase and by type.')
        controller.response.json.title == 'Targeting Test 1 Types and/or Phases'
        response.status == HttpStatus.OK.value()
    }

    // Edit action
    void "test edit action when pageInstance is passed"() {
        given: 'PageLayout instance'
        Page validPageInstance = getPageInstance(1)

        when: 'Edit action is hit'
        controller.request.method = 'POST'
        controller.params.id = validPageInstance.id
        controller.edit()

        then: 'JSON response should be received'
        controller.response.json.pageInstance.subTitle == 'To execute the JUnit integration test 1'
        controller.response.json.pageInstance.publish == true
        response.status == HttpStatus.OK.value()
    }

    void "test edit action for error response"() {
        when: 'Edit action is hit'
        controller.request.method = 'POST'
        controller.params.id = 1L
        controller.edit()

        then: 'JSON error response should be received'
        controller.response.json['message'] == 'page.not.found'
        response.status == HttpStatus.NOT_ACCEPTABLE.value()
    }

    // Update action
    void "test update action when pageLayoutInstance is passed"() {
        // Note: Updated instance cannot be checked as the update is performed by service method call which is mocked
        given: 'Page and Map parameters instance'
        Page pageInstance = getPageInstance(1)
        Map params = [layoutName: 'TestPageLayout']

        and: 'Mocking Services'
        controller.contentService = Mock(ContentService)
        1 * controller.contentService.update(_, _, _, _) >> pageInstance

        pageInstance.contentService = Mock(ContentService)
        1 * pageInstance.contentService.createLink(_) >> '/page/list'

        when: 'Update action is hit with pageInstance'
        controller.request.method = 'PUT'
        controller.request.json = params
        controller.params.id = pageInstance.id
        controller.update()

        then: 'HttpStatus FOUND should be received'
        controller.response.status == HttpStatus.FOUND.value()
        controller.response.redirectedUrl.contains('/page/list')

        when: 'Update action is hit with invalid instance'
        controller.request.method = 'PUT'
        controller.request.json = params
        controller.params.id = 1L
        controller.update()

        then: 'JSON error response should be received'
        controller.response.json['message'] == 'page.not.found'
    }

    void "test update action when invalid instance is passed"() {
        given: 'Page and Map parameters instance'
        Page pageInstance = getPageInstance(1)
        Page pageInstanceWithErrors = new Page([abc: 'abc'])
        pageInstanceWithErrors.save()

        Map params = [layoutName: 'TestPageLayout']

        and: 'Mock services'
        controller.contentService = Mock(ContentService)
        1 * controller.contentService.update(_, _, _, _) >> pageInstanceWithErrors

        when: 'Update action is hit'
        controller.request.method = 'PUT'
        controller.request.json = params
        controller.params.id = pageInstance.id
        controller.update()

        then: 'HttpStatus UNPROCESSABLE ENTITY should be received'
        controller.response.status == HttpStatus.UNPROCESSABLE_ENTITY.value()
    }

    void "test update action when parameters are passed"() {
        given: 'Page and Map parameters instance'
        Page pageInstance = getPageInstance(1)

        and: 'Mocking Services'
        controller.contentService = Mock(ContentService)
        1 * controller.contentService.update(_, _, _, _) >> pageInstance

        pageInstance.contentService = Mock(ContentService)
        1 * pageInstance.contentService.createLink(_) >> '/page/list'

        when: 'Update action is hit'
        controller.request.method = 'PUT'
        controller.params.createRevision = true
        controller.params.id = pageInstance.id
        controller.update()

        then: 'HttpStatus FOUND should be received with Redirect URL /page/list'
        controller.response.status == HttpStatus.FOUND.value()
        controller.response.redirectedUrl.contains('/page/list')
    }

    // Delete action
    void "test delete action when pageInstance"() {
        given: 'Page and Map parameters instance'
        Page pageInstance = getPageInstance(1)

        controller.contentService = [delete: { Page pageInstance1 ->
            return
        } ] as ContentService

        when: 'Delete action is hit'
        controller.request.method = 'DELETE'
        controller.params.id = pageInstance.id
        controller.delete()

        then: 'HttpStatus OK should be received with success TRUE'
        controller.response.json.success == true
        controller.response.status == HttpStatus.OK.value()
    }

    void "test delete action for error response"() {
        when: 'Delete action is called and RequiredPropertyMissingException is thrown'
        controller.request.method = 'DELETE'
        controller.params.id = 1L
        controller.delete()

        then: 'Error response should be received'
        controller.response.json['message'] == 'page.not.found'
        controller.response.status == HttpStatus.NOT_ACCEPTABLE.value()
    }
}
