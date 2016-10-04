package com.causecode.seo.friendlyurl

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@Mock([])
@TestFor(FriendlyUrlService)
class FriendlyUrlServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test "() {
        given: ''
        String text = 'α'

        when: ''
        String resultText = service.removeAccents(text)

        then: ''
        resultText != null
    }
}
