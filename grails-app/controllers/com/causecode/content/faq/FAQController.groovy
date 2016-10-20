/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.faq

import com.causecode.exceptions.RequiredPropertyMissingException
import com.causecode.springsecurity.Annotations
import com.causecode.utility.UtilParameters
import grails.plugin.springsecurity.annotation.Secured

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus

/**
 * Provides default CRUD end point for Content Manager.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 *
 */
@Secured([Annotations.ROLE_CONTENT_MANAGER])
class FAQController {

    static responseFormats = ['json']
    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

    def index() {
        redirect(action: 'list', params: params)

        return
    }

    def list(Integer max, Integer offset) {
        params.max = Math.min(max ?: 10, 100)
        params.offset = offset ?: 0
        respond ([instanceList: FAQ.list(params), totalCount: FAQ.count()])

        return
    }

    def create(FAQ faqInstance) {
        respond ([FAQInstance: faqInstance])

        return
    }

    def save(FAQ faqInstance) {
        if (faqInstance.hasErrors()) {
            log.warn "Error saving pageLayout Instance: $faqInstance.errors."
            respond ([errors: faqInstance.errors], status: HttpStatus.NOT_MODIFIED)
            return
        }

        respond (UtilParameters.SUCCESS_TRUE)

        return
    }

    def show(FAQ faqInstance) {
        respond ([FAQInstance: faqInstance])

        return
    }

    def edit(FAQ faqInstance) {
        respond ([FAQInstance: faqInstance])

        return
    }

    def update(FAQ faqInstance) {
        if (!faqInstance || faqInstance.hasErrors()) {
            respond (faqInstance.errors)
            return
        }

        respond([status: HttpStatus.OK])

        return
    }

    def delete(FAQ faqInstance) {
        try {
            faqInstance.delete(UtilParameters.FLUSH_TRUE)
        } catch (DataIntegrityViolationException e) {
            respond ([status: HttpStatus.NOT_MODIFIED])
            return false
        }

        respond([status: HttpStatus.OK])

        return true
    }
}
