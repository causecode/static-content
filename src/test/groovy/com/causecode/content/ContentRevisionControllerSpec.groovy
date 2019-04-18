package com.causecode.content

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.http.HttpStatus
import spock.lang.Specification

/**
 * This is Unit test file of ContentRevisionController class.
 */
@Mock([ContentRevision, Content])
@TestFor(ContentRevisionController)
class ContentRevisionControllerSpec extends Specification implements BaseTestSetup {

    // Show action
    void "test show action when content instance and contentRevision is passed"() {
        when: 'Show action is hit'
        controller.request.method = 'GET'
        controller.params.id = contentRevisionInstance.id
        controller.show()

        then: 'HttpStatus.OK should be received as response'
        controller.response.status == HttpStatus.OK.value()
    }

    // Load action
    void "test load action when id is passed"() {
        when: 'Load action is hit'
        controller.request.method = 'POST'
        controller.params.id = contentRevisionInstance.id
        controller.load()

        then: 'Valid json response should be received'
        controller.response.json['subTitle'] == 'Test subtitle'
        controller.response.json['title'] == 'Sample Title'
        controller.response.json['body'] == 'Test Body'
        controller.response.status == HttpStatus.OK.value()
    }

    // Delete action
    void "test delete action when id is passed"() {
        when: 'Delete action is hit'
        controller.params.id = contentRevisionInstance.id
        controller.request.method = 'POST'
        assert ContentRevision.count() == 1
        controller.delete()

        then: 'ContentRevision count should be equal to 0'
        assert ContentRevision.count() == 0
        controller.response.status == HttpStatus.OK.value()
    }

    ContentRevision getContentRevisionInstance() {
        Content contentInstance = getContentInstance(1)

        Map contentRevisionDataMap = [
            title: 'Sample Title',
            body: 'Test Body',
            subTitle: 'Test subtitle',
            revisionOf: contentInstance,
            comment: 'Sample comments'
        ]

        ContentRevision contentRevisionInstance = new ContentRevision(contentRevisionDataMap)
        contentRevisionInstance.save()

        assert contentRevisionInstance.id

        return contentRevisionInstance
    }
}
