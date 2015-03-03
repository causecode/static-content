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
import org.springframework.http.HttpStatus
import org.springframework.dao.DataIntegrityViolationException

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

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        log.info "pagelayout list"
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
        PageLayout pageLayoutInstance = new PageLayout(params)
        if (!pageLayoutInstance.save(flush: true)) {
            log.warn "Error saving pageLayout Instance: $pageLayoutInstance.errors."
            render text: ([errors: pageLayoutInstance.errors] as JSON), status: HttpStatus.NOT_MODIFIED
            return
        }

        respond ([success: true])
    }

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

        pageLayoutInstance.properties = params

        if (!pageLayoutInstance.save(flush: true)) {
            log.warn "Error saving pageLayout Instance: $pageLayoutInstance.errors."
            render text: ([errors: pageLayoutInstance.errors] as JSON), status: HttpStatus.NOT_MODIFIED
            return
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
