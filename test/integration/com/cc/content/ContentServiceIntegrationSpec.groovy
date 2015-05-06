/*
 * Copyright (c) 2011 - Present, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

import grails.test.spock.IntegrationSpec

import org.springframework.context.MessageSource

import com.cc.content.format.TextFormat;

class ContentServiceIntegrationSpec extends IntegrationSpec {
    
    MessageSource messageSource
    ContentService contentService
    TextFormat textFormatInstance1
    
    def setup() {
        textFormatInstance1 = new TextFormat([name:"xyz", roles:"ROLE_CONTENT_MANAGER", allowedTags:"div, h1",
            editor:false]).save(flush: true)
    }

    def cleanup() {
    }

    void "test if empty Body is passed in Content"() {
        when: "An empty body is passed for formatting"
        contentService.formatBody("", textFormatInstance1)
        
        then: "Warning should be given"
        Exception e = thrown(IllegalArgumentException)
        e.message == messageSource.getMessage("content.body.error", null, null)
    }
}
