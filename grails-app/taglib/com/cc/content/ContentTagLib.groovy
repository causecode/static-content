/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

class ContentTagLib {

    static namespace = "content"

    def contentService

    def breadcrumb = {attrs, body ->
        if(!attrs.map)
            throwTagError("Tag [content:breadcrumb] is missing required attribute [map]")

        if(!(attrs.map instanceof Map))
            throwTagError("Tag [content:breadcrumb] attribute [map] must be of type java.util.Map")

        LinkedHashMap baseURLMap = grailsApplication.config.cc.plugins.content.breadcrumbs.baseMap
        out << g.render(template: "/home/templates/breadcrumb", model: [baseURLMap: baseURLMap,
            urlMap: attrs.map])
    }

    /**
     * Used when current user have content role
     * described by cc.plugins.content.contentManagerRole
     */
    @Deprecated
    def canEdit = { attrs, body ->
        if(contentService.contentManager) {
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

    /**
     * Used to create SEO friendly search url like /o/151/hewlet-packard
     * @param domain REQUIRED the name of the domain class from which
     * sanitized title will be appended in uri. Domain class must have SanitizedTitle
     * annotation.
     * @param controller REQUIRED the name of the controller for which 
     * SEO friendly url needs to be generated. The controller must have annotaion
     * ControllerShortHand ehich specific value.
     * @return String SEO friendly url.
     */
    def createLink = { attrs, body ->
        if(!attrs.domain)
            throwTagError("Tag content:createLink missing required attribute domain")

        out << contentService.createLink(attrs)
    }

    /**
     * @attr id REQUIRED
     */
    def resolveAuthor = { attrs, body ->
        out << contentService.resolveAuthor(Content.get(attrs.id), attrs.authorProperty ?: "fullName")
    }

    /**
     * @attr id REQUIRED
     */
    def renderContent = { attrs, body ->
        if(!attrs.id) {
            throwTagError("Required id")
        }
        Content contentInstance = Content.get(attrs.id)
        if(contentInstance) {
            out << body(contentInstance)
        }
    }

}