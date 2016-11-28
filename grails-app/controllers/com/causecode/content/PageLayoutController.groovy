/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
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

    GrailsWebDataBinder grailsWebDataBinder

    def index() {
        redirect(action: 'list', params: params)

        return
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond([instanceList: PageLayout.list(params), totalCount: PageLayout.count()])

        return
    }

    def getPageLayoutList() {
        respond([pageLayoutList: PageLayout.list(max: 200)])

        return
    }

    def create(PageLayout pageLayoutInstance) {
        respond([pageLayoutInstance: pageLayoutInstance])

        return
    }

    def save(PageLayout pageLayoutInstance) {
        if (pageLayoutInstance.hasErrors()) {
            log.warn "Error saving pageLayout Instance: $pageLayoutInstance.errors."
            respond ([errors: pageLayoutInstance.errors], status: HttpStatus.NOT_MODIFIED)
            return
        }

        log.info 'PageLayout instance saved successfully.'
        pageLayoutInstance.save([flush: true])
        respond([status: HttpStatus.OK])

        return
    }

    @Transactional(readOnly = true)
    def show(PageLayout pageLayoutInstance) {
        respond([pageLayoutInstance: pageLayoutInstance])

        return
    }

    def edit(PageLayout pageLayoutInstance) {
        if (!pageLayoutInstance) {
            log.warn message(logWarnMessage())
            redirect(action: 'list')
            return
        }

        respond([pageLayoutInstance: pageLayoutInstance])

        return
    }

    def update(PageLayout pageLayoutInstance) {
        if (!pageLayoutInstance || pageLayoutInstance.hasErrors()) {
            log.warn message(logWarnMessage())
            redirect(action: 'list')
            return
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
        pageLayoutInstance.save([flush: true])

        respond ([status: HttpStatus.OK])

        return
    }

    def delete(PageLayout pageLayoutInstance) {
        try {
            pageLayoutInstance.delete([flush: true])
        }
        catch (DataIntegrityViolationException e) {
            respond ([status: HttpStatus.NOT_MODIFIED])
            return false
        }
        respond ([status: HttpStatus.OK])

        return true
    }

    private Map logWarnMessage() {

        return [code: 'default.not.found.message', args: [message(code: 'pageLayout.label'), params.id]]
    }
}
