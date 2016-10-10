/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.meta

import com.causecode.BaseTestSetup
import com.causecode.content.Content
import com.causecode.content.ContentMeta
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.http.HttpStatus
import spock.lang.Specification

@Mock([Meta, ContentMeta, Content])
@TestFor(MetaController)
class MetaControllerSpec extends Specification implements BaseTestSetup {

    // Validate method (private)
    void "test validate method meta instance id is passed"() {
        given: 'Meta instance'
        Meta metaInstance = getMetaInstance()

        when: 'Validate method is called'
        controller.params.id = metaInstance.id
        boolean boolResult = controller.validate()

        then: 'Valid response should be received'
        boolResult == true
    }

    void "test validate method when wrong id is passed"() {
        when: 'Validate method is called'
        controller.params.id = 1L
        boolean boolResult = controller.validate()

        then: 'Valid response should be received'
        boolResult == false
        controller.response.redirectedUrl.contains('/meta/list')

        when: 'Validate method is called'
        controller.params.id = 1L
        controller.request.makeAjaxRequest()
        boolean boolResult1 = controller.validate()

        then: 'Valid response should be received'
        boolResult == false
        controller.response.status == HttpStatus.FOUND.value()
    }

    // deleteMeta action
    void "test deleteMeta action when id is passed as 0"() {
        when: 'deleteMeta action is hit'
        controller.deleteMeta(0)

        then: 'Valid response should be received'
        controller.response.status == HttpStatus.OK.value()
    }
}
