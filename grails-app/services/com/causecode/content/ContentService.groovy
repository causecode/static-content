/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.causecode.content

import grails.util.Environment
import java.lang.annotation.Annotation
import java.lang.reflect.Field

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.web.mapping.LinkGenerator
import grails.databinding.SimpleMapDataBindingSource
import org.springframework.transaction.annotation.Transactional

import com.causecode.annotation.sanitizedTitle.SanitizedTitle
import com.causecode.content.blog.Blog
import com.causecode.content.blog.comment.BlogComment
import com.causecode.content.page.Page
import com.causecode.content.meta.Meta
import grails.core.GrailsApplication
import grails.plugin.springsecurity.SpringSecurityUtils

/**
 * This taglib provides tags for rendering comments on blog.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 */
class ContentService {

    static transactional = false

    def grailsWebDataBinder
    private static final String ANONYMOUS_USER = "anonymousUser"

    /**
     * Dependency Injection for the grailsLinkGenerator
     */
    LinkGenerator grailsLinkGenerator
    
    /**
     * Dependency injection for the services
     */
    def friendlyUrlService
    def springSecurityService
    GrailsApplication grailsApplication

    /**
     * Used to get author of content instance with the help of author property passed.
     * @param contentInstance Content instance whose author needs to be resolved.
     * @param authorProperty String representing property of current user to be returned. Default set to 'fullName' 
     * field.
     * @return String specifying author for content instance. If content instance not specified and content instance
     * author field is not a number the default string 'ANONYMOUS_USER' returned.
     */
    String resolveAuthor(Content contentInstance, String authorProperty = "fullName") {
        if(!contentInstance?.id) {
            def currentUser = springSecurityService.currentUser
            return currentUser ? currentUser.id.toString() : ANONYMOUS_USER
        }
        if(contentInstance.author?.isNumber()) {
            // authorInstance returns User instance, keeping it def so that method remains generic.
            def className = SpringSecurityUtils.securityConfig.userLookup.userDomainClassName
            def authorClazz = grailsApplication.getDomainClass(className).clazz
            def authorInstance = authorClazz.get(contentInstance.author)
            if (!authorInstance[authorProperty]) {
                authorProperty = "username"
            }
            return authorInstance[authorProperty]
        }
        return contentInstance.author
    }

    /**
     * A method to check if current user have authority to view the current content instance
     * @param id Identity of Content domain instance .
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

    /**
     * Check current user has role content manager or not and provide edit access to user.
     * @return Boolean value specifying current user has content manager role or not.
     */
    @Deprecated
    boolean canEdit() {
        isContentManager()
    }

    /**
     * Check current user has role content manager or not.
     * @return Boolean value specifying current user has content manager role or not.
     */
    boolean isContentManager() {
        String contentManagerRole = grailsApplication.config.cc.plugins.content.contentManagerRole
        return SpringSecurityUtils.ifAnyGranted(contentManagerRole)
    }

    /**
     * Used to create Content instance with given parameters.
     * @param args Map containing parameters required to create content instance.
     * @param metaTypes List containing meta types for Content instance.
     * @param metaValues List containing meta values for Content instance.
     * @return Newly created Content Instance.
     */
    @Transactional
    Content create(Map args, List metaTypes, List metaValues, Class clazz = Content.class) {
        Content contentInstance = clazz.newInstance()
        contentInstance.author = resolveAuthor(contentInstance)
        update(args, contentInstance, metaTypes, metaValues)
        return contentInstance
    }

    /**
     * Used to update Content instance with given new parameters.
     * @param args Map containing parameters required to create content instance.
     * @param contentInstance REQUIRED Content Instance to be updated.
     * @param metaTypes List containing meta types for Content instance.
     * @param metaValues List containing meta values for Content instance.
     * @return Updated Content Instance.
     */
    @Transactional
    Content update(Map args, Content contentInstance, List metaTypes, List metaValues) {
        grailsWebDataBinder.bind(contentInstance, args as SimpleMapDataBindingSource)
        contentInstance.validate()
        if(contentInstance.hasErrors()) {
            log.warn "Error saving ${contentInstance.class.name}: " + contentInstance.errors
            return contentInstance
        }
        contentInstance.save()
        if (!metaTypes || !metaValues) {
            return contentInstance
        }

        // Remove all content meta relations
        List<ContentMeta> contentMetas = ContentMeta.withCriteria {
            createAlias("content", "contentInstance")
            eq("contentInstance.id", contentInstance.id)
        }*.delete();

        // Remove all metas
        contentMetas.meta*.delete()

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

    /**
     * Used to delete Content and its join class references of ContentMeta , ContentRevision class.
     * Also if contentInstance is type of blog then its comments will also be deleted.
     * @param contentInstance
     * @return Boolean value specifying delete content instance operation  status success or failure.
     */
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

    /**
     * Used to create revision of any type of content instance.
     * @param contentInstance REQUIRED Content Instance to be get revised.
     * @param clazz REQUIRED Class used to create new content revision instance.
     * @param params Map containing parameters for comments.
     * @return Newly created ContentRevision for given conetentInstance.
     */
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
            if(controllerName == attrs.controller) {
                controllerShortHand = shorthand
            }
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

    def getRoleClass() {
        return grailsApplication.getDomainClass(SpringSecurityUtils.securityConfig.authority.className).clazz
    }
}
