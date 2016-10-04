package com.causecode.content

import com.causecode.BaseTestSetup
import com.causecode.content.blog.Blog
import com.causecode.content.blog.BlogContentType
import grails.plugins.taggable.Tag
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import spock.lang.Specification

@TestFor(PageLayoutController)
@Mock([PageLayout])
class PageLayoutControllerSpec extends Specification implements BaseTestSetup {

    // Index action
    void "test index action for valid JSON response"() {
        given: 'Some PageLayout Instance'
        createInstances("PageLayout", 5)

        when: 'Index action is hit'
        controller.index()

        then: 'A valid JSON response should be received'
        PageLayout.count() == 5
        response.status == HttpStatus.FOUND.value()
        response.redirectedUrl == '/pageLayout/list'
    }

    // GetPageLayoutList action
    void "test getPageLayoutList action to get metaList"() {
        given: 'Some PageLayout Instance'
        createInstances("PageLayout", 5)

        when: 'getPageLayoutList action is hit'
        controller.getPageLayoutList()

        then: 'A valid JSON response should be received'
        PageLayout.count() == 5
        response.status == HttpStatus.OK.value()
    }


    // Create action
    void "test create action when parameters are passed"() {
        given: 'Map parameters instance'
        Map params = [layoutName: 'TestPageLayout']

        when: 'Create action is called'
        controller.request.parameters = params
        controller.create()

        then: 'Valid HttpStatus should be received'
        controller.response.status == HttpStatus.OK.value()
    }

    // List action
    void "test list action when parameters are passed"() {
        given: 'Some PageLayout Instance'
        createInstances("PageLayout", 5)

        when: 'List action is hit'
        controller.list(5)

        then: 'A valid response should be received with HttpStatus OK'
        PageLayout.count() == 5
        response.contentType == 'application/json;charset=UTF-8'
        response.status == HttpStatus.OK.value()
    }

    // Save action // error action Not Allowed
    void "test save action when request parameters are passed"() {
        given: 'Map parameters instance'
        Map params = [layoutName: 'TestPageLayout']

        when: 'Save action is called'
        controller.request.method = 'POST'
        controller.request.json = params
        controller.save()

        then: 'A valid HttpStatus OK should be received'
        response.status == HttpStatus.OK.value()
    }

    // Show action
    void "test show action when PageLayout instance is passed"() {
        given: 'PageLayout instance'
        PageLayout pageLayoutInstance = getPageLayoutInstance(1)

        when: 'Show action is hit'
        controller.show(pageLayoutInstance)

        then: 'Valid JSON response should be received'
        response.status == HttpStatus.OK.value()
    }

    // Edit action
    void "test edit action when pageLayoutInstance is passed"() {
        given: 'PageLayout instance'
        PageLayout validPageLayoutInstance = getPageLayoutInstance(1)
        PageLayout invalidPageLayoutInstance = null

        when: 'Edit action is hit'
        controller.edit(validPageLayoutInstance)

        then: 'Valid JSON response should be received'
        response.status == HttpStatus.OK.value()

        /*when: 'Edit action is hit' // Error redirecting
        controller.edit(invalidPageLayoutInstance)

        then: 'Valid JSON response should be received'
        controller.response.redirectedUrl.contains('/blog/show/')*/
    }

    // Update action
    void "test update action when pageLayoutInstance is passed"() { // action not allowed exception
        given: 'PageLayout and Map parameters instance'
        PageLayout pageLayoutInstance = getPageLayoutInstance(1)
        Map params = [layoutName: 'TestPageLayout']

        when: 'Update action is called'
        controller.request.method = 'PUT'
        controller.request.json = params
        controller.update(pageLayoutInstance, 1L)

        then: 'Valid HttpStatus should be received'
        controller.response.status == HttpStatus.OK.value()
    }

    // Delete action
    void "test delete action when pageLayoutInstance"() {
        given: 'PageLayout and Map parameters instance'
        PageLayout pageLayoutInstance = getPageLayoutInstance(1)
        Map params = [layoutName: 'TestPageLayout']

        when: 'Delete action is called'
        controller.request.method = 'DELETE'
        controller.delete(pageLayoutInstance)

        then: 'Valid HttpStatus should be received'
        controller.response.status == HttpStatus.OK.value()

        /*when: 'Delete action is called and DataIntegrityViolationException is thrown'
        PageLayout invalidPageLayoutInstance = new PageLayout()
        controller.request.method = 'DELETE'
        controller.delete(invalidPageLayoutInstance)

        *//*controller.contentService = [delete: { Blog blogInstance1 ->
            throw new DataIntegrityViolationException('Invalid blogInstance')
        }] as ContentService*//*

        then: 'A valid JSON response should be received'
        controller.response.status == HttpStatus.NOT_MODIFIED.value()*/ // For exception thrown
    }
}
