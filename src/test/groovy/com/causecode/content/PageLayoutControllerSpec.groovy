package com.causecode.content

import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Unroll

/**
 * This is Unit test file for PageLayoutController class.
 */
@TestFor(PageLayoutController)
@Mock([PageLayout])
class PageLayoutControllerSpec extends Specification implements BaseTestSetup {

    // Index action
    void "test index action for valid JSON response"() {
        given: 'PageLayout Instances'
        createInstances('PageLayout', 5)
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
        createInstances('PageLayout', 5)
        assert PageLayout.count() == 5

        when: 'getPageLayoutList action is hit'
        controller.pageLayoutList

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
    /*
     * Note: Suppressed GrailsMaxForListQueries warning here, as this warning should prompt in case of criteria query,
     *       here its just a controller action call
     */
    @SuppressWarnings(['GrailsMaxForListQueries'])
    void "test list action when parameters are passed"() {
        given: 'Some PageLayout Instance'
        createInstances('PageLayout', 5)
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
        when: 'Edit action is hit'
        controller.request.method = 'POST'
        controller.params.id = 2L
        controller.edit()

        then: 'Response redirect URL /list should be received'
        response.status == HttpStatus.UNPROCESSABLE_ENTITY.value()
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
        Map params = [layoutName: 'TestPageLayout']

        when: 'Update action is hit'
        controller.request.method = 'PUT'
        controller.request.json = params
        controller.params.id = 2L
        controller.update()

        then: 'HttpStatus NOT_ACCEPTABLE should be received'
        response.status == HttpStatus.UNPROCESSABLE_ENTITY.value()
    }

    // Delete action
    void "test delete action when pageLayoutInstance id is passed"() {
        given: 'PageLayout instance'
        PageLayout pageLayoutInstance = getPageLayoutInstance(1)
        assert PageLayout.count() == 1

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
