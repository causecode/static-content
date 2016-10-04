/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import grails.databinding.SimpleMapDataBindingSource
import grails.web.databinding.GrailsWebDataBinder
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus

/**
 * Provides default CRUD end point for Content Manager.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 * @author Bharti Nagdev
 */
@Secured(['ROLE_CONTENT_MANAGER'])
class PageLayoutController {

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']
    static responseFormats = ['json']

    private static final String LIST_STRING = 'list'
    private static final String DEFAULT_NOT_FOUND = 'default.not.found.message'

    private static final Map FLUSH_TRUE = [flush: true]

    GrailsWebDataBinder grailsWebDataBinder

    def index() {
        redirect(action: LIST_STRING, params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ([instanceList: PageLayout.list(params), totalCount: PageLayout.count()])
    }

    def getPageLayoutList() {
        respond([pageLayoutList: PageLayout.list(max: 20)])
    }

    def create() {
        def pageLayoutInstance = new PageLayout()
        bindData(pageLayoutInstance, params, [include: 'layoutName'])

        [pageLayoutInstance: pageLayoutInstance]
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
        }

        log.info 'Pagelayout instance saved successfully.'
        pageLayoutInstance.save(FLUSH_TRUE)
        respond ([status: HttpStatus.OK])
    }

    @Transactional(readOnly = true)
    def show(PageLayout pageLayoutInstance) {
        respond(pageLayoutInstance)
    }

    def edit(PageLayout pageLayoutInstance) {
        if (!pageLayoutInstance) {
            log.warn message(logWarnMessage())
            redirect(action: LIST_STRING)
            return
        }

        respond([pageLayoutInstance: pageLayoutInstance])
    }

    def update(PageLayout pageLayoutInstance, Long version) {
        if (!pageLayoutInstance) {
            log.warn message(logWarnMessage())
            redirect(action: LIST_STRING)
            return
        }

        if (version != null) {
            if (pageLayoutInstance.version > version) {
                respond ([message: 'Another user has updated this page layout instance while you were editing'],
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
        }

        log.info 'PageLayout instance updated successfully.'
        pageLayoutInstance.save(FLUSH_TRUE)

        respond ([status: HttpStatus.OK])
    }

    def delete(PageLayout pageLayoutInstance) {
        try {
            pageLayoutInstance.delete(FLUSH_TRUE)
        }
        catch (DataIntegrityViolationException e) {
            respond ([status: HttpStatus.NOT_MODIFIED])
            return false
        }
        respond ([status: HttpStatus.OK])
    }

    private Map logWarnMessage() {
        return [code: DEFAULT_NOT_FOUND, args: [message(code: 'pageLayout.label'), params.id]]
    }
}
