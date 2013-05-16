/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.page

class PageTagLib {

    static namespace = "page"

    def grailsApplication

    /**
     * Renders layout of the Page object. If not specified, the default
     * layout name as set by cc.plugins.content.default.layout.name will
     * be rendered.
     * 
     * @attr pageInstance REQUIRED the instance of Page domain class.
     */
    def renderLayout = { attrs, body ->
        if(!attrs.pageInstance)
            throwTagError("Page tag lib missing required attribute pageInstance")

        Page pageInstance = attrs.pageInstance
        if(!pageInstance.pageLayout) {
            out << body(grailsApplication.config.cc.plugins.content.default.layout.name ?: "main")
            //out << "<meta name=\"layout\" content=\"main\">"
            return
        }

        String layout = pageInstance.pageLayout.layoutName
        //out << "<meta name=\"layout\" content=\"$layout\">"
        out << body(layout)
    }

}