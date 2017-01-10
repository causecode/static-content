/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content

import com.causecode.content.meta.Meta
import com.causecode.seo.friendlyurl.FriendlyUrlService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * This is Unit test file for Content class.
 */
@Mock([ContentService, FriendlyUrlService, Meta, ContentMeta])
@TestFor(Content)
class ContentSpec extends Specification implements BaseTestSetup {

    // ResolveAuthor method
    void "test resolveAuthor method for content instance"() {
        given: 'Instance of content and Mocked ContentService'
        Content contentInstance = getContentInstance(1)

        when: 'Method resolveAuthor is called'
        String resultString = contentInstance.resolveAuthor()

        then: 'Result string anonymousUser should be received'
        resultString == 'anonymousUser'
    }

    // GetSanitizedTitle method
    void "test getSanitizedTitle method for content instance"() {
        given: 'Instance of content and Mocked ContentService'
        Content contentInstance = getContentInstance(1)

        when: 'Method resolveAuthor is called'
        String resultString = contentInstance.sanitizedTitle

        then: 'A valid result string should be received'
        resultString == 'targeting-test-1-types-andor-phases'
    }

    // GetMetaTags method
    void "test getMetaTags method for content instance"() {
        given: 'ContentMeta instance'
        ContentMeta contentMetaInstance = getContentMetaInstance(1)
        assert contentMetaInstance.toString() == 'ContentMeta(Content(Targeting Test 1 Types and/or Phases, 1),' +
                ' Meta(keywords, 1))'

        when: 'getMetaTags method is called'
        List<Meta> resultMetaList = contentMetaInstance.content.metaTags

        then: 'Result list should not be null'
        resultMetaList != null
    }
}
