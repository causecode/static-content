/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.meta

import com.causecode.content.BaseTestSetup
import com.causecode.content.Content
import com.causecode.content.ContentMeta
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.http.HttpStatus
import spock.lang.Specification

/**
 * This is Unit test file for MetaController class.
 */
@Mock([Meta, ContentMeta, Content])
@TestFor(MetaController)
class MetaControllerSpec extends Specification implements BaseTestSetup {

    // Validate method (private)
    void "test validate method meta instance id is passed"() {
        when: 'Validate method is called'
        controller.params.id = metaInstance.id
        boolean boolResult = controller.validate()

        then: 'boolResult TRUE should be received'
        boolResult == true
    }

    void "test validate method when wrong id is passed"() {
        when: 'Validate method is called'
        controller.params.id = 1L
        boolean boolResult = controller.validate()

        then: 'Redirect URL should be /meta/list'
        boolResult == false
        controller.response.redirectedUrl.contains('/meta/list')

        when: 'Validate method is called with AJAX request'
        controller.params.id = 1L
        controller.request.makeAjaxRequest()
        boolResult = controller.validate()

        then: 'boolResult FALSE should be received'
        boolResult == false
        response.status == HttpStatus.NOT_FOUND.value()
    }

    // deleteMeta action
    void "test deleteMeta action when id is passed as 0"() {
        when: 'deleteMeta action is hit'
        controller.params.id = 0
        controller.deleteMeta()

        then: 'HttpStatus NOT_FOUND should be received'
        controller.response.status == HttpStatus.NOT_FOUND.value()
    }

    void "test deleteMeta action when id is passed"() {
        when: 'Meta instance is passed'
        controller.request.method = 'POST'
        controller.params.id = metaInstance.id
        controller.validate()
        controller.deleteMeta()

        then: 'Success TRUE should be received as JSON response'
        controller.response.json.success == true
        controller.response.status == HttpStatus.OK.value()
    }
}
