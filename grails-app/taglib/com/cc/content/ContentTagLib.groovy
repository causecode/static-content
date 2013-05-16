/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

import java.lang.reflect.Field

import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest
import org.springframework.beans.factory.annotation.Autowired

import com.cc.annotation.sanitizedTitle.SanitizedTitle;

class ContentTagLib {

    static namespace = "content"

    @Autowired
    LinkGenerator linkGenerator

    def contentService
    def friendlyUrlService

    /**
     * Used when current user have content role
     * described by cc.plugins.content.contentMangerRole
     */
    def canEdit = { attrs, body ->
        if(contentService.canEdit()) {
            out << body()
        }
    }

    /**
     * Renders meta tags of the content object.
     *
     * @attr contentInstance REQUIRED the instance of Content or child domain class.
     */
    def renderMetaTags = { attrs, body ->
        if(!attrs.contentInstance)
            throwTagError("Page tag lib missing required attribute contentInstance")

        Content contentInstance = attrs.contentInstance
        if(!contentInstance?.metaTags)
            return
        contentInstance.metaTags.each {
            out << "<meta name=\"${it.type}\" content=\"${it.value}\" />\n"
        }
    }

    def createLink = { attrs, body ->
        boolean useJsessionId = grailsApplication.config.grails.views.enable.jsessionid
        if(!attrs.domain)
            throwTagError("Tag content:createLink missing required attribute domain")

        Class domainClass = grailsApplication.getDomainClass(attrs.domain).clazz
        def domainClassInstance = domainClass.get(attrs.id)
        List<Field> fields = []

        if(domainClass.superclass && domainClass.superclass != Object)
            fields.addAll(domainClass.superclass.getDeclaredFields())
        fields.addAll(domainClass.getDeclaredFields())
        

        String fieldName, sanitizedTitle = ""
        for(field in fields) {
            if(field.isAnnotationPresent(SanitizedTitle.class)) {
                fieldName = field.name
                break
            }
        }
        if(fieldName)
            sanitizedTitle = friendlyUrlService.sanitizeWithDashes(domainClassInstance[fieldName])
            
        
        def urlAttrs = attrs
        if (attrs.url instanceof Map) {
            urlAttrs = attrs.url
        }
        def params = urlAttrs.params && urlAttrs.params instanceof Map ? urlAttrs.params : [:]
        if (request['flowExecutionKey']) {
            params."execution" = request['flowExecutionKey']
            urlAttrs.params = params
            if (attrs.controller == null && attrs.action == null && attrs.url == null && attrs.uri == null) {
                urlAttrs[LinkGenerator.ATTRIBUTE_ACTION] = GrailsWebRequest.lookup().actionName
            }
        }
        if (urlAttrs.event) {
            params."_eventId" = urlAttrs.remove('event')
            urlAttrs.params = params
        }
        def generatedLink = linkGenerator.link(attrs, request.characterEncoding)

        if (useJsessionId) {
            return response.encodeURL(generatedLink)
        } else {
            out << generatedLink
        }
    }

}