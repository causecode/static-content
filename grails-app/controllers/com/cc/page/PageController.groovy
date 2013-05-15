/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.page

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.dao.DataIntegrityViolationException

import com.cc.content.*

class PageController {

    def springSecurityService
    def springSecurityUiService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        if(!params.max) {
            params.max=2
        }
        if(!params.offset) {
            params.offset=0
        }
        def pageList = Page.createCriteria()
        def pages = pageList.list (max:params.max, offset:params.offset) {
            order("dateCreated","desc")
        }
        [pageInstanceList: pages, pageInstanceTotal: pages.getTotalCount(), userClass: userClass()]
    }

    def create() {
        [pageInstance: new Page(params)]
    }

    def save() {
        def pageInstance = new Page(params)
        bindData(pageInstance, params, [include: ['title', 'subTitle', 'body']])
        pageInstance.pageLayout = PageLayout.get(params.pageLayout)
        def principal= springSecurityService.getPrincipal()
        if(principal == "anonymousUser") {
            pageInstance.author = principal
        }
        else	{
            pageInstance.author = principal.id as String

        }
        if (!pageInstance.save(flush: true)) {
            render(view: "create", model: [pageInstance: pageInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'page.label', default: 'Page'), pageInstance.id])
        redirect(action: "show", id: pageInstance.id)
    }

    def show(Long id) {
        def pageInstance = Page.get(id)
        def username
        if (!pageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'page.label', default: 'Page'), id])
            redirect(action: "list")
            return
        }
        def userId = pageInstance.author
        if(userId?.isNumber()) {
            userId.toInteger()
            def userInstance = userClass().get(userId)
            username= userInstance.username
        }
        else {
            username= "anonymousUser"
        }
        [pageInstance: pageInstance, username : username, layout : pageInstance?.pageLayout?.layoutFile]
    }

    def edit(Long id) {
        def pageInstance = Page.get(id)
        if (!pageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'page.label', default: 'Page'), id])
            redirect(action: "list")
            return
        }
        [pageInstance: pageInstance]
    }

    def update(Long id, Long version) {
        def pageInstance = Page.get(id)
        if (!pageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'page.label', default: 'Page'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (pageInstance.version > version) {
                pageInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'page.label', default: 'Page')] as Object[],
                        "Another user has updated this Page while you were editing")
                render(view: "edit", model: [pageInstance: pageInstance])
                return
            }
        }
        bindData(pageInstance, params, [include: ['title', 'subTitle', 'body']])
        pageInstance.pageLayout = PageLayout.get(params.pageLayout)
        if (!pageInstance.save(flush: true)) {
            render(view: "edit", model: [pageInstance: pageInstance])
            return
        }
        flash.message = message(code: 'default.updated.message', args: [message(code: 'page.label', default: 'Page'), pageInstance.id])
        redirect(action: "show", id: pageInstance.id)
    }

    def delete(Long id) {
        def pageInstance = Page.get(id)
        if (!pageInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'page.label', default: 'Page'), id])
            redirect(action: "list")
            return
        }

        try {
            pageInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'page.label', default: 'Page'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'page.label', default: 'Page'), id])
            redirect(action: "show", id: id)
        }
    }

    def layout1() {

    }

    def layout2() {

    }

    def layout3() {

    }

    protected String lookupUserClassName() {
        SpringSecurityUtils.securityConfig.userLookup.userDomainClassName
    }

    protected Class<?> userClass() {
        grailsApplication.getDomainClass(lookupUserClassName()).clazz
    }

}