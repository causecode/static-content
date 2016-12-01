/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.faq

import com.causecode.content.BaseTestSetup
import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.http.HttpStatus
import spock.lang.Specification

@Mock([FAQ])
@TestFor(FAQController)
class FAQControllerSpec extends Specification implements BaseTestSetup {

    // Index action
    void "test index action for valid JSON response"() {
        given: 'Some FAQ Instance'
        createInstances("FAQ", 5)

        when: 'Index action is hit'
        controller.index()

        then: 'Response status FOUND should be received'
        FAQ.count() == 5
        response.status == HttpStatus.FOUND.value()
        response.redirectedUrl == '/FAQ/list'
    }

    // Create action
    void "test create action when parameters are passed"() {
        given: 'Map parameters instance'
        Map params = [
            title   : "Targeting Test Types and/or Phases",
            author  : "Test User",
            subTitle: "To execute the JUnit integration test",
            body    : "Grails organises tests by phase and by type. The state of the Grails application."
        ]

        when: 'Create action is hit'
        controller.request.method = 'POST'
        controller.request.parameters = params
        controller.create()

        then: 'Valid JSON response with HttpStatus OK should be received'
        controller.response.json.FAQInstance.subTitle == 'To execute the JUnit integration test'
        controller.response.json.FAQInstance.title == 'Targeting Test Types and/or Phases'
        controller.response.status == HttpStatus.OK.value()
    }

    // List action
    void "test list action when parameters are passed"() {
        given: 'Some Page Instance'
        createInstances("FAQ", 5)
        assert FAQ.count() == 5

        when: 'List action is hit'
        controller.list()

        then: 'A valid response should be received with HttpStatus OK'
        controller.response.json.instanceList.subTitle.contains('To execute the JUnit integration test 1')
        controller.response.json.instanceList.publish[0] == true
        response.status == HttpStatus.OK.value()
    }

    // Save action
    void "test save action when request parameters are passed"() {
        given: 'Map parameters instance'
        String jsonRequest = (getContentParams(1) as JSON).toString()

        when: 'Save action is hit'
        controller.request.method = 'POST'
        controller.request.json = jsonRequest
        controller.save()

        then: 'HttpStatus OK and JSON response success TRUE should be received'
        controller.response.json.success == true
        response.status == HttpStatus.OK.value()
    }

    void "test save action when invalid request parameters are passed"() {
        when: 'Invalid json is passed'
        String jsonRequest2 = ([] as JSON).toString()
        controller.request.method = 'POST'
        controller.request.json = jsonRequest2
        controller.save()

        then: 'Error response should be received'
        controller.response.json.errors.errors[0].field == 'body'
        controller.response.json.errors.errors[0].message.contains('Property [body] of class')
    }

    // Show action
    void "test show action when FAQ instance is passed"() {
        given: 'PageLayout instance'
        FAQ faqInstance = getFAQInstance(1)

        when: 'Show action is hit'
        controller.request.method = 'GET'
        controller.params.id = faqInstance.id
        controller.show()

        then: 'Valid JSON response should be received with HttpStatus OK'
        controller.response.json.FAQInstance.subTitle == 'To execute the JUnit integration test 1'
        controller.response.json.FAQInstance.body.contains('Grails organises tests by phase and by type.')
        response.status == HttpStatus.OK.value()
    }

    // Edit action
    void "test edit action when faqInstance is passed"() {
        given: 'faqInstance instance'
        FAQ validFaqInstance = getFAQInstance(1)
        assert validFaqInstance.toString() == 'FAQ()'
        FAQ invalidFaqInstance = null

        when: 'Edit action is hit'
        controller.request.method = 'POST'
        controller.params.id = validFaqInstance.id
        controller.edit()

        then: 'Valid JSON response and HttpStatus OK should be received'
        controller.response.json.FAQInstance.subTitle == 'To execute the JUnit integration test 1'
        controller.response.json.FAQInstance.publish == true
        response.status == HttpStatus.OK.value()
    }

    // Update action
    void "test update action when faqInstance is passed"() {
        given: 'Page and Map parameters instance'
        FAQ faqInstance = getFAQInstance(1)
        Map params = [subTitle: 'To execute the JUnit integration']
        params.id = faqInstance.id.toString()

        when: 'Update action is hit'
        controller.request.method = 'PUT'
        controller.request.json = params
        controller.update()

        then: 'HttpStatus OK should be received'
        faqInstance.subTitle == 'To execute the JUnit integration'
        controller.response.json.status.name == 'OK'
        controller.response.status == HttpStatus.OK.value()
    }

    // Delete action
    void "test delete action when pageInstance"() {
        given: 'Page and Map parameters instance'
        FAQ faqInstance = getFAQInstance(1)
        Map params = [id: faqInstance.id.toString()]

        when: 'Delete action is hit'
        controller.request.method = 'DELETE'
        controller.params.id = faqInstance.id
        controller.delete()

        then: 'HttpStatus OK should be received'
        controller.response.json.status.name == 'OK'
        controller.response.status == HttpStatus.OK.value()
    }
}
