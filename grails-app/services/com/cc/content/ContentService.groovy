/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

import org.codehaus.groovy.grails.commons.metaclass.GroovyDynamicMethodsInterceptor
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.web.metaclass.BindDynamicMethod

import com.cc.blog.Blog
import com.cc.page.Page

class ContentService {

    private static final String ANONYMOUS_USER = "anonymousUser"

    def grailsApplication
    def springSecurityService

    ContentService() {
        GroovyDynamicMethodsInterceptor i = new GroovyDynamicMethodsInterceptor(this)
        i.addDynamicMethodInvocation(new BindDynamicMethod())
    }

    String resolveAuthor(Content contentInstance) {
        if(!contentInstance?.id) {
            def currentUser = springSecurityService.currentUser
            return currentUser ? currentUser.id.toString() : ANONYMOUS_USER
        }
        if(contentInstance.author.isNumber()) {
            String authorClassName = SpringSecurityUtils.securityConfig.userLookup.userDomainClassName
            if(!authorClassName) return ANONYMOUS_USER; // Required if plugin run-app

            Class authorClass = grailsApplication.getDomainClass(authorClassName).clazz
            if(!authorClass) return ANONYMOUS_USER; // Required if plugin run-app

            def authorInstance = authorClass.get(contentInstance.author)
            return authorInstance[grailsApplication.config.cc.plugins.content.authorProperty ?: "username"]
        }
        return ANONYMOUS_USER
    }

    boolean isVisible(def id) {
        if(canEdit()) return true;

        List restrictedDomainClassList = [Page.class.name, Blog.class.name]
        Content contentInstance = Content.withCriteria(uniqueResult: true) {
            idEq(id.toLong())
            'in'("class", restrictedDomainClassList)
        }
        if(!contentInstance || contentInstance.publish)
            return true

        return false
    }

    boolean canEdit() {
        String contentManagerRole = grailsApplication.config.cc.plugins.content.contentMangerRole
        return SpringSecurityUtils.ifAnyGranted(contentManagerRole)
    }

}