package com.causecode.content.faq

import com.causecode.BaseTestSetup
import com.causecode.content.ContentService
import com.causecode.content.PageLayout
import com.causecode.content.blog.BlogContentType
import com.causecode.content.page.Page
import grails.converters.JSON
import grails.plugins.taggable.Tag
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

        then: 'A valid JSON response should be received'
        FAQ.count() == 5
        response.status == HttpStatus.FOUND.value()
        response.redirectedUrl == '/FAQ/list'
    }

    // GetMetaTypeList action
//    void "test getMetaTypeList action to get metaList"() {
//        given: 'Some Page Instance'
//        createInstances("Page", 5)
//
//        when: 'getMetaList action is hit'
//        controller.getMetaTypeList()
//
//        then: 'A valid JSON response should be received'
//        PageLayout.count() == 5
//        response.status == HttpStatus.OK.value()
//    }
//
//
    // Create action
    void "test create action when parameters are passed"() {
        given: 'Map parameters instance'
        Map params = [
                title   : "Targeting Test Types and/or Phases",
                author  : "Test User",
                subTitle: "To execute the JUnit integration test",
                body    : "Grails organises tests by phase and by type. The state of the Grails application."]

        when: 'Create action is called'
        controller.request.parameters = params
        controller.create()

        then: 'Valid HttpStatus should be received'
        controller.response.status == HttpStatus.OK.value()
    }
//
    // List action
    void "test list action when parameters are passed"() {
        given: 'Some Page Instance'
        createInstances("FAQ", 5)

        when: 'List action is hit'
        controller.list()

        then: 'A valid response should be received with HttpStatus OK'
        FAQ.count() == 5
        response.contentType == 'application/json;charset=UTF-8'
        response.status == HttpStatus.OK.value()
    }
//
    // Save action
    /*void "test save action when request parameters are passed"() {
        given: 'Map parameters instance'
        String jsonRequest = (getContentParams(1) as JSON).toString()

        when: 'Save action is called'
        controller.request.method = 'POST'
        controller.request.json = jsonRequest
        controller.save()

        then: 'A valid HttpStatus OK should be received'
        response.status == HttpStatus.OK.value()

        when: 'Invalid json is passed'
        String jsonRequest2 = ([] as JSON).toString()
        controller.request.method = 'POST'
        controller.request.json = jsonRequest2
        controller.save()

        then: 'A valid error response should be received'
    }*/
//
    // Show action
    void "test show action when FAQ instance is passed"() {
        given: 'PageLayout instance'
        FAQ faqInstance = getFAQInstance(1)

        when: 'Show action is hit'
        controller.show(faqInstance)

        then: 'Valid JSON response should be received'
        response.status == HttpStatus.OK.value()
    }
//
    // Edit action
    void "test edit action when pageInstance is passed"() {
        given: 'PageLayout instance'
        FAQ validFaqInstance = getFAQInstance(1)
        FAQ invalidFaqInstance = null

        when: 'Edit action is hit'
        controller.edit(validFaqInstance)

        then: 'Valid JSON response should be received'
        response.status == HttpStatus.OK.value()

        /*when: 'Edit action is hit' // Error redirecting
        controller.edit(invalidPageLayoutInstance)

        then: 'Valid JSON response should be received'
        controller.response.redirectedUrl.contains('/blog/show/')*/
    }
//
    // Update action
    void "test update action when pageLayoutInstance is passed"() { // Error java.lang.StringIndexOutOfBoundsException: String index out of range: 1
        given: 'Page and Map parameters instance'
        FAQ validFaqInstance = getFAQInstance(1)
        Map params = [author: 'Test Author']

        when: 'Update action is called'
        controller.request.method = 'PUT'
        controller.request.json = params
        controller.update(validFaqInstance, 1L)

        then: 'Valid HttpStatus should be received'
        controller.response.status == HttpStatus.OK.value()
    }
//
    // Delete action
    void "test delete action when pageInstance"() {
        given: 'Page and Map parameters instance'
        FAQ faqInstance = getFAQInstance(1)

        when: 'Delete action is called'
        controller.request.method = 'DELETE'
        controller.delete(faqInstance)

        then: 'Valid HttpStatus should be received'
        controller.response.status == HttpStatus.OK.value()

        /*when: 'Delete action is called and RequiredPropertyMissingException is thrown'
        Page invalidPageInstance = null
        controller.request.method = 'DELETE'
        controller.delete(invalidPageInstance)

        then: 'A valid JSON response should be received'
        controller.response.status == HttpStatus.OK.value() // For exception thrown*/
    }
}
