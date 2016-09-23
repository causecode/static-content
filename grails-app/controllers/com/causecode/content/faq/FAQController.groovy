/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.faq

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
@Secured(['ROLE_CONTENT_MANAGER'])
class FAQController {

    static responseFormats = ['json']

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'DELETE']

    private static final Map FLUSH_TRUE = [flush: true]

    def index() {
        redirect(action: 'list', params: params)
    }

    def list(Integer max, Integer offset) {
        params.putAll(request.JSON)
        params.max = Math.min(max ?: 10, 100)
        params.offset = offset ?: 0
        respond ([instanceList: FAQ.list(params), totalCount: FAQ.count()])
    }

    def create() {
        def faqInstance = new FAQ()
        bindData(faqInstance, params)

        [FAQInstance: faqInstance]

    }

    def save() {
        params.putAll(request.JSON)
        FAQ faqInstance = new FAQ()
        bindData(faqInstance, params)
        if (!faqInstance.save(FLUSH_TRUE)) {
            respond(faqInstance.errors)
            return
        }

        respond ([success: true])
    }

    def show(FAQ faqInstance) {
        respond (faqInstance)
    }

    def edit(FAQ faqInstance) {
        [FAQInstance: faqInstance]
    }

    def update(FAQ faqInstance, Long version) {
        params.putAll(request.JSON)
        if (version != null) {
            if (faqInstance.version > version) {
                faqInstance.errors.rejectValue('version', 'default.optimistic.locking.failure',
                        [message(code: 'FAQ.label', default: 'FAQ')] as Object[],
                        'Another user has updated this FAQ while you were editing')
                respond(faqInstance.errors)
                return
            }
        }
        bindData(faqInstance.properties, params)
        //faqInstance.properties = params

        if (!faqInstance.save(FLUSH_TRUE)) {
            respond(faqInstance.errors)
            return
        }

        respond ([status: HttpStatus.OK])
    }

    def delete(FAQ faqInstance) {
        try {
            faqInstance.delete(FLUSH_TRUE)
        } catch (DataIntegrityViolationException e) {
            respond ([status: HttpStatus.NOT_MODIFIED])
            return false
        }
        respond ([status: HttpStatus.OK])
    }
}
