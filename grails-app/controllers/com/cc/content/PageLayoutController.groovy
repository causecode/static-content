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
        params.putAll(request.JSON)
        params.max = Math.min(max ?: 10, 100)
        respond ([instanceList: PageLayout.list(params), totalCount: PageLayout.count()]) 
    }
    
    def getPageLayoutList() {
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
        
        respond ([success: true])
    }

    def show(Long id) {
        params.putAll(request.JSON)
        def pageLayoutInstance = PageLayout.get(id)
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
        params.putAll(request.JSON)
        def pageLayoutInstance = PageLayout.get(id)
        
        if (!pageLayoutInstance) {
            respond ([success: false])
            return
        }

        if (version != null) {
            if (pageLayoutInstance.version > version) {
                respond ([success: false, message: "Another user has updated this Email Template while you were editing"])
                return
            }
        }

        pageLayoutInstance.properties = params

        if (!pageLayoutInstance.save(flush: true)) {
            respond(pageLayoutInstance.errors)
            return
        }

        respond ([success: true])
    }

    def delete(Long id) {
        def pageLayoutInstance = PageLayout.get(id)
        
        try {
            pageLayoutInstance.delete(flush: true)
            respond ([success: true])
        }
        catch (DataIntegrityViolationException e) {
            respond ([success: false])
        }
    }
}
