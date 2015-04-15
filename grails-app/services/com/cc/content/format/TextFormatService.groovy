/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.format

import org.codehaus.groovy.grails.commons.metaclass.GroovyDynamicMethodsInterceptor
import grails.plugin.springsecurity.SpringSecurityUtils;
import org.codehaus.groovy.grails.web.metaclass.BindDynamicMethod

class TextFormatService {

    TextFormatService() {
        GroovyDynamicMethodsInterceptor i = new GroovyDynamicMethodsInterceptor(this)
        i.addDynamicMethodInvocation(new BindDynamicMethod())
    }

     /**
     * To generate available Formats list
     *
     */
    
    def getApplicableFormats() {
        def ApplicableFormats = []
        def textFormatInstanceList = TextFormat.getAll()
        textFormatInstanceList.each {
            if (SpringSecurityUtils.ifAnyGranted(it.roles)) {
                ApplicableFormats.add(it)
            }
        }
        return ApplicableFormats
    }
}