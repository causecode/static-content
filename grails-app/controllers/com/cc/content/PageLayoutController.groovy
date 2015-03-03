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
import grails.transaction.Transactional

import org.grails.databinding.SimpleMapDataBindingSource
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus

/**
 * Provides default CRUD end point for Content Manager.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 * @author Bharti Nagdev
 *
 */
@Secured(["ROLE_CONTENT_MANAGER"])
class PageLayoutController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    static responseFormats = ["json"]

    def grailsWebDataBinder

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ([instanceList: PageLayout.list(params), totalCount: PageLayout.count()])
    }

    def getPageLayoutList() {
        respond([pageLayoutList: PageLayout.list()])
    }

    def create() {
        [pageLayoutInstance: new PageLayout(params)]
    }

    def save() {
        Map requestData = request.JSON
        log.info "Parameters received save pageLayout instance: ${requestData}"
        PageLayout pageLayoutInstance = new PageLayout()
        grailsWebDataBinder.bind(pageLayoutInstance, requestData as SimpleMapDataBindingSource)
        pageLayoutInstance.validate()

        if (pageLayoutInstance.hasErrors()) {
            log.warn "Error saving pageLayout Instance: $pageLayoutInstance.errors."
            respond ([errors: pageLayoutInstance.errors], status: HttpStatus.NOT_MODIFIED)
            return
        } else {
            log.info "Pagelayout instance saved successfully."
            pageLayoutInstance.save(flush: true)
        }

        respond ([success: true])
    }

    @Transactional(readOnly = true)
    def show(PageLayout pageLayoutInstance) {
        respond(pageLayoutInstance)
    }

    def edit(PageLayout pageLayoutInstance) {
        if (!pageLayoutInstance) {
            log.warn message(code: 'default.not.found.message', args: [message(code: 'pageLayout.label'), params.id])
            redirect(action: "list")
            return
        }

        respond([pageLayoutInstance: pageLayoutInstance])
    }

    def update(PageLayout pageLayoutInstance, Long version) {
        if (!pageLayoutInstance) {
            log.warn message(code: 'default.not.found.message', args: [message(code: 'pageLayout.label'), params.id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (pageLayoutInstance.version > version) {
                respond ([message: "Another user has updated this Email Template while you were editing"], 
                    status: HttpStatus.NOT_MODIFIED)
                return
            }
        }

        Map requestData = request.JSON
        grailsWebDataBinder.bind(pageLayoutInstance, requestData as SimpleMapDataBindingSource)
        pageLayoutInstance.validate()

        if (pageLayoutInstance.hasErrors()) {
            log.warn "Error updating pageLayout Instance: $pageLayoutInstance.errors."
            respond ([errors: pageLayoutInstance.errors], status: HttpStatus.NOT_MODIFIED)
            return
        } else {
            log.info "Pagelayout instance updated successfully."
            pageLayoutInstance.save(flush: true)
        }

        respond ([success: true])
    }

    def delete(PageLayout pageLayoutInstance) {
        try {
            pageLayoutInstance.delete(flush: true)
            respond HttpStatus.OK
        }
        catch (DataIntegrityViolationException e) {
            respond HttpStatus.NOT_MODIFIED
        }
    }
}
