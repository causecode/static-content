/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

import grails.plugin.springsecurity.annotation.Secured

import org.springframework.dao.DataIntegrityViolationException

@Secured(["ROLE_CONTENT_MANAGER"])
class PageLayoutController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [pageLayoutInstanceList: PageLayout.list(params), pageLayoutInstanceTotal: PageLayout.count()]
    }

    def create() {
        [pageLayoutInstance: new PageLayout(params)]
    }

    def save() {
        def pageLayoutInstance = new PageLayout(params)
        if (!pageLayoutInstance.save(flush: true)) {
            render(view: "create", model: [pageLayoutInstance: pageLayoutInstance])
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

        [pageLayoutInstance: pageLayoutInstance]
    }

    def edit(Long id) {
        def pageLayoutInstance = PageLayout.get(id)
        if (!pageLayoutInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'pageLayout.label'), id])
            redirect(action: "list")
            return
        }

        [pageLayoutInstance: pageLayoutInstance]
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
