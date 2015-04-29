package com.cc.content

import static org.junit.Assert.*
import grails.test.mixin.*
import grails.test.mixin.support.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec  //For supporting "encodeAsHTML" method 
import org.junit.*
import spock.lang.*
import com.cc.content.format.*
/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@Mock([TextFormat,Content])
@TestFor(ContentService)
@TestMixin(GrailsUnitTestMixin)
class ContentServiceSpec extends Specification{
    def setup() {
        // Setup logic here
    }

    def cleanup() {
        // Tear down logic here
       // TextFormat.executeQuery("delete from TextFormat")
    }

    void "test formatBody when TextFormat editor is absent"() {
        given:
        mockCodec(HTMLCodec)
        TextFormat textFormatInstance1 = new TextFormat([name: "xyz", roles: "ROLE_CONTENT_MANAGER", allowedTags: "div, h1", editor:false]).save(flush: true)
      
        when: "Editor is absent"
        assert textFormatInstance1  //returns true/false
        String body = '<strong>Hello world</strong>'
        String bodyReturned = service.formatBody(body, textFormatInstance1)
        
        then: "HTML Encoded body should be returned"
        bodyReturned.indexOf("&lt;") > -1
    }
    
    void "test formatted body if non-allowed tags are passed"() {
        given:
        TextFormat textFormatInstance2 = new TextFormat([name: "PARTIAL HTML", roles: "ROLE_CONTENT_MANAGER", allowedTags: "strong, h1", editor:true]).save(flush: true)
        
        when : "Foreign tag present"
        assert textFormatInstance2
        String body = "<jolt> hello <joe> <strong> tree </strong>"
        String formattedBody = service.formatBody(body, textFormatInstance2)
        
        then : "They will be removed automatically"
        //Do something
        formattedBody.indexOf("<jolt>") == -1
    }
    
    void "test formatted body if attributes are passed in allowed tags"() {
        
        given:
        String body = "<jolt> hello  world<jolt> <strong class=sm> tree </strong>"
        TextFormat textFormatInstance = new TextFormat([name: "PARTIAL HTML", roles: "ROLE_CONTENT_MANAGER", allowedTags: "strong, div, h1", editor:true]).save(flush: true)
        
        when : "An attribute is passed in an allowed tag"
        String formattedBody = service.formatBody(body, textFormatInstance)
        
        then: "It should not be ignored or treated as a foreign element"
        // Perfect formatting done
        assert formattedBody.compareTo("  hello  world  <strong class=sm> tree </strong>") > 0
    }
    
    void "test if empty string is passed in Body"() {
        when: "An empty body is passed"
        String body = "" 
        
        then: "Warning should be given"
        //Message Enter a body
        thrown(IllegalArgumentException)
    }
    
    void "test if null textFormat instance is passed"() {
        when: "A null instance of TextFormat domain is passed"
        TextFormat textFormatinstance = null
        
        then: "Warning should be thrown"
        thrown(IllegalArgumentException)
    }
}
