/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import org.springframework.dao.DataIntegrityViolationException

/**
 * Provides default CRUD end point for Content Manager.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 * @author Bharti Nagdev
 *
 */
@Secured(["permitAll"])
class PageLayoutController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
	static responseFormats = ["json"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ([instanceList: PageLayout.list(params), totalCount: PageLayout.count()]) 
    }
    
    def getPageLayoutList() {
        println("in getpagelayout")
        List pageLayoutList = PageLayout.list()
        respond([pageLayoutList: pageLayoutList])
        return
    }

    def create() {
        [pageLayoutInstance: new PageLayout(params)]
    }

    def save() {
        params.putAll(request.JSON)
        def pageLayoutInstance = new PageLayout(params)
        if (!pageLayoutInstance.save(flush: true)) {
            respond(pageLayoutInstance.errors)
            return
        }
        flash.message = message(code: 'default.created.message', args: [message(code: 'pageLayout.label'), pageLayoutInstance.id])
        redirect(action: "show", id: pageLayoutInstance.id)
    }

    def show(Long id) {
        def pageLayoutInstance = PageLayout.get(id)
        if (!pageLayoutInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pageLayout.label'), id])
            redirect(action: "list")
            return
        }

        respond(pageLayoutInstance)
    }

    def edit(Long id) {
        def pageLayoutInstance = PageLayout.get(id)
        if (!pageLayoutInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pageLayout.label'), id])
            redirect(action: "list")
            return
        }

        println("in edit")
        respond([pageLayoutInstance: pageLayoutInstance])
    }

    def update(Long id, Long version) {
        def pageLayoutInstance = PageLayout.get(id)
        if (!pageLayoutInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pageLayout.label'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (pageLayoutInstance.version > version) {
                pageLayoutInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'pageLayout.label')] as Object[],
                        "Another user has updated this PageLayout while you were editing")
                render(view: "edit", model: [pageLayoutInstance: pageLayoutInstance])
                return
            }
        }

        pageLayoutInstance.properties = params

        if (!pageLayoutInstance.save(flush: true)) {
            render(view: "edit", model: [pageLayoutInstance: pageLayoutInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'pageLayout.label'), pageLayoutInstance.id])
        redirect(action: "show", id: pageLayoutInstance.id)
    }

    def delete(Long id) {
        def pageLayoutInstance = PageLayout.get(id)
        if (!pageLayoutInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pageLayout.label'), id])
            redirect(action: "list")
            return
        }

        try {
            pageLayoutInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'pageLayout.label'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'pageLayout.label'), id])
            redirect(action: "show", id: id)
        }
    }
}
