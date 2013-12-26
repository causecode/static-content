/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

import grails.util.Environment

import java.lang.reflect.Field

import org.codehaus.groovy.grails.commons.metaclass.GroovyDynamicMethodsInterceptor
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.codehaus.groovy.grails.web.metaclass.BindDynamicMethod
import org.springframework.transaction.annotation.Transactional

import com.cc.annotation.sanitizedTitle.SanitizedTitle
import com.cc.content.blog.Blog
import com.cc.content.blog.comment.BlogComment
import com.cc.content.meta.Meta
import com.cc.content.page.Page

class ContentService {

    static transactional = false

    private static final String ANONYMOUS_USER = "anonymousUser"

    LinkGenerator grailsLinkGenerator
    def friendlyUrlService
    def grailsApplication
    def springSecurityService

    ContentService() {
        GroovyDynamicMethodsInterceptor i = new GroovyDynamicMethodsInterceptor(this)
        i.addDynamicMethodInvocation(new BindDynamicMethod())
    }

    String resolveAuthor(Content contentInstance, String authorProperty = "username") {
        if(!contentInstance?.id) {
            def currentUser = springSecurityService.currentUser
            return currentUser ? currentUser.username.toString() : ANONYMOUS_USER
        }
        if(contentInstance.author.isNumber()) {
            def authorInstance = getAuthorClass().get(contentInstance.author)
            return authorInstance[authorProperty]
        }
        return contentInstance.author
    }

    /**
     * A method to check if current user have authority to view the current content instance
     */
    boolean isVisible(def id) {
        if(contentManager) return true;

        List restrictedDomainClassList = [Page.class.name, Blog.class.name]
        Content contentInstance = Content.withCriteria(uniqueResult: true) {
            idEq(id.toLong())
            'in'("class", restrictedDomainClassList)
        }
        if(!contentInstance || contentInstance.publish)
            return true

        return false
    }

    @Deprecated
    boolean canEdit() {
        isContentManager()
    }

    boolean isContentManager() {
        String contentManagerRole = grailsApplication.config.cc.plugins.content.contentManagerRole
        return SpringSecurityUtils.ifAnyGranted(contentManagerRole)
    }

    @Transactional
    Content create(Map args, List metaTypes, List metaValues, Class clazz = Content.class) {
        Content contentInstance = clazz.newInstance()
        contentInstance.author = resolveAuthor(contentInstance)
        update(args, contentInstance, metaTypes, metaValues)
        return contentInstance
    }

    @Transactional
    Content update(Map args, Content contentInstance, List metaTypes, List metaValues) {
        bindData(contentInstance, args)
        contentInstance.validate()
        if(contentInstance.hasErrors()) {
            log.warn "Error saving ${contentInstance.class.name}: " + contentInstance.errors
            return contentInstance
        }
        contentInstance.save()
        if(!metaTypes || !metaValues)
            return contentInstance

        metaTypes.eachWithIndex { type, index ->
            Meta metaInstance = ContentMeta.withCriteria(uniqueResult: true) {
                createAlias("content", "contentInstance")
                createAlias("meta", "metaInstance")
                projections {
                    property("meta")
                }
                eq("contentInstance.id", contentInstance.id)
                eq("metaInstance.type", type)
            }
            if(!metaInstance) {
                metaInstance = new Meta(type: type)
            }
            metaInstance.value = metaValues[index]
            metaInstance.validate()
            if(!metaInstance.hasErrors()) {
                metaInstance.save()
                ContentMeta.findOrSaveByContentAndMeta(contentInstance, metaInstance)
            }
        }
        return contentInstance
    }

    @Transactional
    boolean delete(Content contentInstance) {
        ContentMeta.findAllByContent(contentInstance)*.delete()
        ContentRevision.findAllByRevisionOf(contentInstance)*.delete()
        if(contentInstance instanceof Blog) {
            contentInstance.setTags([])
            BlogComment.findAllByBlog(contentInstance)*.delete()
        }
        contentInstance.delete()
    }

    @Transactional
    ContentRevision createRevision(Content contentInstance, Class clazz, Map params) {
        ContentRevision contentRevisionInstance = clazz.newInstance()
        contentRevisionInstance.title = contentInstance.title
        contentRevisionInstance.body = contentInstance.body
        contentRevisionInstance.subTitle = contentInstance.subTitle
        contentRevisionInstance.revisionOf = contentInstance
        contentRevisionInstance.comment = params.revisionComment ?: ""
        contentRevisionInstance.save()
        contentRevisionInstance
    }

    /**
     * Used to create SEO friendly search url like /o/151/hewlet-packard
     * @param attrs
     * @param attrs.domain REQUIRED the name of the domain class from which
     * sanitized title will be appended in uri. Domain class must have SanitizedTitle
     * annotation.
     * @param attrs.controller REQUIRED the name of the controller for which 
     * SEO friendly url needs to be generated. The controller must have annotaion
     * ControllerShortHand ehich specific value.
     * @return String SEO friendly url.
     */
    String createLink(Map attrs) {
        Class DomainClass = grailsApplication.getDomainClass(attrs.domain).clazz
        def domainClassInstance = DomainClass.get(attrs.id)     // Get actual domainInstance
        List<Field> fields = []

        if(DomainClass.superclass && DomainClass.superclass != Object)
            fields.addAll(DomainClass.superclass.getDeclaredFields())   // Adding fields from super class
        fields.addAll(DomainClass.getDeclaredFields())      // Adding fields from current class

        String action, fieldName, sanitizedTitle, controllerShortHand = ""
        for(field in fields) {
            if(field.isAnnotationPresent(SanitizedTitle.class) && field.type == String) {  // Searching annotation on each field
                fieldName = field.name
                break
            }
        }
        if(fieldName) {
            if(domainClassInstance) {
                sanitizedTitle = friendlyUrlService.sanitizeWithDashes(domainClassInstance[fieldName])
            } else {
                log.error "No entry found with id [$attrs.id] for domain [$attrs.domain]."
            }
        } else
            log.error "No annotated field found in domain class [$attrs.domain]."

        action = attrs.action ? attrs.action + "/" : ""

        // See hooking into dynamic events
        getShorthandAnnotatedControllers().each { controllerName, shorthand ->
            if(controllerName == attrs.controller)
                controllerShortHand = shorthand
        }

        if(controllerShortHand)
            attrs.uri = "/$controllerShortHand/${action}${attrs.id}/${sanitizedTitle ?: ''}"
        else
            log.error "No shorthand found for controller: ${attrs.controller}"

        if(attrs.absolute?.toBoolean()) {
            attrs.absolute = false
            attrs.base = grailsApplication.config.grails.serverURL
            if(Environment.current == Environment.DEVELOPMENT)
                attrs.base = grailsApplication.config.grails.other.serverURL    // Other config which contains public IP like: 13.14.28.153:8080
        }

        return grailsLinkGenerator.link(attrs)
    }

}