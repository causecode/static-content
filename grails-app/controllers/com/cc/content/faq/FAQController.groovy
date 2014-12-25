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
@Secured(["permitAll"])
class FAQController {
	
	static responseFormats = ["json"]

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def beforeInterceptor = [action: this.&validate, except: ["index", "list", "create", "save"]]

    private FAQ FAQInstance

    private validate() {
        FAQInstance = FAQ.get(params.id)
        if(!FAQInstance) {
            flash.message = g.message(code: 'default.not.found.message', args: [message(code: 'FAQ.label', default: 'FAQ'), params.id])
            redirect(action: "list")
            return false
        }
        return true
    }

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max, Integer offset) {
        params.max = Math.min(max ?: 10, 100)
        params.offset = offset ? offset: 0
        respond ( [instanceList: FAQ.list(params), totalCount: FAQ.count()] ) 
    }

    def create() {
        [FAQInstance: new FAQ(params)]
    }

    def save() {
        params.putAll(request.JSON)
        FAQInstance = new FAQ(params)
        if (!FAQInstance.save(flush: true)) {
            respond(FAQInstance.errors)
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'FAQ.label', default: 'FAQ'), FAQInstance.id])
        redirect(action: "show", id: FAQInstance.id)
    }

    def show() {
        respond (FAQInstance)
    }

    def edit(Long id) {
        [FAQInstance: FAQInstance]
    }

    def update(Long id, Long version) {
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

        flash.message = message(code: 'default.updated.message', args: [message(code: 'FAQ.label', default: 'FAQ'), FAQInstance.id])
        redirect(action: "show", id: FAQInstance.id)
    }

    def delete(Long id) {
        println(">>>>>> In deelete")
        try {
            FAQInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'FAQ.label', default: 'FAQ'), id])
            redirect(action: "list")
        } catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'FAQ.label', default: 'FAQ'), id])
            redirect(action: "show", id: id)
        }
    }
}
