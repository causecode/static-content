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
        if(!attrs.domain)
            throwTagError("Tag content:createLink missing required attribute domain")

        out << contentService.createLink(attrs)
    }

}