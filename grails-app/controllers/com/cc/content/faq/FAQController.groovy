/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.faq

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import org.springframework.dao.DataIntegrityViolationException

/**
 * Provides default CRUD end point for Content Manager.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 *
 */
@Secured(["ROLE_CONTENT_MANAGER"])
class FAQController {
	
	static responseFormats = ["json"]

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max, Integer offset) {
        params.putAll(request.JSON)
        params.max = Math.min(max ?: 10, 100)
        params.offset = offset ? offset: 0
        respond ([instanceList: FAQ.list(params), totalCount: FAQ.count()]) 
    }

    def create() {
        [FAQInstance: new FAQ(params)]
    }

    def save() {
        params.putAll(request.JSON)
        FAQ FAQInstance = new FAQ(params)
        if (!FAQInstance.save(flush: true)) {
            respond(FAQInstance.errors)
            return
        }

        respond ([success: true])
    }

    def show(FAQ FAQInstance) {
        respond (FAQInstance)
    }

    def edit(FAQ FAQInstance) {
        [FAQInstance: FAQInstance]
    }

    def update(FAQ FAQInstance, Long version) {
        params.putAll(request.JSON)
        if(version != null) {
            if (FAQInstance.version > version) {
                FAQInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'FAQ.label', default: 'FAQ')] as Object[],
                        "Another user has updated this FAQ while you were editing")
                respond(FAQInstance.errors)
                return
            }
        }

        FAQInstance.properties = params

        if (!FAQInstance.save(flush: true)) {
            respond(FAQInstance.errors)
            return
        }

        respond ([success: true])
    }

    def delete(FAQ FAQInstance) {
        try {
            FAQInstance.delete(flush: true)
            respond ([success: true])
        } catch (DataIntegrityViolationException e) {
            respond ([success: false])
        }
    }
}
