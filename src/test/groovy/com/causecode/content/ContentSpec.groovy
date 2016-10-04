package com.causecode.content

import com.causecode.BaseTestSetup
import com.causecode.content.meta.Meta
import com.causecode.seo.friendlyurl.FriendlyUrlService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@Mock([ContentService, FriendlyUrlService, Meta, ContentMeta])
@TestFor(Content)
class ContentSpec extends Specification implements BaseTestSetup {

    // ResolveAuthor method
    void "test resolveAuthor method for content instance"() {
        given: 'Instance of content and Mocked ContentService'
        Content contentInstance = getContentInstance(1)

        when: 'Method resolveAuthor is called'
        String resultString = contentInstance.resolveAuthor()

        then: 'A valid result should be received'
        resultString == 'anonymousUser'
    }

    // GetSanitizedTitle method
    void "test getSanitizedTitle method for content instance"() {
        given: 'Instance of content and Mocked ContentService'
        Content contentInstance = getContentInstance(1)

        when: 'Method resolveAuthor is called'
        String resultString = contentInstance.getSanitizedTitle()

        then: 'A valid result should be received'
        resultString == 'targeting-test-1-types-andor-phases'
    }

    // GetMetaTags method
    void "test getMetaTags method for content instance"() {
        given: 'ContentMeta instance'
        ContentMeta contentMetaInstance = getContentMetaInstance(1)

        when: 'getMetaTags method is called'
        List<Meta> resultMetaList = contentMetaInstance.content.getMetaTags()

        then: 'A valid result list should be received'
        resultMetaList != null
    }

    // SearchLink method
    /*void "test searchLink method for content instance"() { // String outOfBound exception
        given: 'Content instance'
        Content contentInstance = getContentInstance(1)

        when: 'searchLink method is called'
        String result = contentInstance.searchLink()

        then: 'A valid result string should be received'
        result != null
    }*/
}
