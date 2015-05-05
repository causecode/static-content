/*
 * Copyright (c) 2011 - Present, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec

import spock.lang.Specification

import com.cc.content.format.TextFormat

@Mock([TextFormat])
@TestFor(ContentService)
@TestMixin(GrailsUnitTestMixin)
class ContentServiceSpec extends Specification {
    
    TextFormat textFormatInstance1
    TextFormat textFormatInstance2
    
    def setup() {
        // Setup logic here
        textFormatInstance1 = new TextFormat([name:"xyz", roles:"ROLE_CONTENT_MANAGER", allowedTags:"div, h1",
            editor:false]).save(flush: true)
        textFormatInstance2 = new TextFormat([name:"PARTIAL HTML", roles:"ROLE_CONTENT_MANAGER", allowedTags:
            "strong, h1", editor:true]).save(flush: true)
    }
    
    void "test formatBody when TextFormat editor is absent"() {
        given: "Mocked Grails HTML codec for encodeAsHTML() method"
        mockCodec(HTMLCodec)
        
        and: "An instance of TextFormat which does not allow editor option"
        assert textFormatInstance1.id       // Confirm that instance is persisted
        
        and: "Body to be processed"
        String body = "<strong>Hello world</strong>"
        
        when: "A textFormat instance with no editor permissions is passed"
        String bodyReturned = service.formatBody(body, textFormatInstance1)
        
        then: "HTML Encoded body should be returned"
        bodyReturned.contains("&lt;strong&gt;Hello world&lt;/strong&gt;")
    }
    
    void "test formatted body if non-allowed tags are passed"() {
        given: "Body with allowed and non-allowed tags"
        String body = "<hr> hello </hr> <strong> world </strong>"
        
        when: "Such a body is passed for formatting"
        assert textFormatInstance2
        String formattedBody = service.formatBody(body, textFormatInstance2)
        
        then: "Non-allowed tags will be removed automatically"
        assert formattedBody.contains("<hr>") == false
        assert formattedBody.contains("<strong> world </strong>")
    }

    void "test formatted body if attributes are passed in allowed tags"() {
        given: "Allowed tags with attributes defined"
        String body = "<u> hello  world<u> <strong class = sm> tree </strong>"
        TextFormat textFormatInstance = new TextFormat([name:"PARTIAL HTML", roles:"ROLE_CONTENT_MANAGER", allowedTags
            :"strong, div, h1", editor:true]).save(flush: true)
        
        when: "An attribute is passed in an allowed tag"
        String formattedBody = service.formatBody(body, textFormatInstance)
        
        then: "It should not be ignored or treated as a foreign element"
        // Perfect formatting done
        assert formattedBody.contains("<strong class = sm> tree </strong>")
    }
    
    void "test if empty string is passed in Body"() {
        when: "An empty body is passed for formatting"
        service.formatBody("", textFormatInstance1)
        
        then: "Warning should be given"
        Exception e = thrown(IllegalArgumentException)
        e.message == "Sorry, You have passed an empty body"
    }
}