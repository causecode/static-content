/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.meta

import grails.plugin.springsecurity.annotation.Secured

import org.springframework.dao.DataIntegrityViolationException

import com.cc.content.ContentMeta

/**
 * Provides end point to delete any meta information of content for Content Manager.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 *
 */
@Secured(["ROLE_CONTENT_MANAGER"])
class MetaController {

    def beforeInterceptor = [action: this.&validate]
    
    static responseFormats = ["json"]

    private Meta metaInstance

    private validate() {
        if(!params.id) return true;

        metaInstance = Meta.get(params.id)
        if(!metaInstance) {
            if(request.xhr) {
                render "dummy"
            } else {
                flash.message = g.message(code: 'default.not.found.message', args: [message(code: 'meta.label', default: 'Meta'), params.id])
                redirect(action: "list")
            }
            return false
        }
        return true
    }
    

    /**
     * Delete Meta instance and all its references and render dummy text.
     * If Meta ID not specified then dummy text will be rendered.
     * This action also delete join class reference added for ContentMeta for given meta instance.
     * 
     * @param id Identity of Meta domain instance to be deleted.
     * @throws DataIntegrityViolationException
     */
    def deleteMeta(Long id) {
        if(!params.id) {
            render "dummy"
            return
        }
        try {
            ContentMeta.findAllByMeta(metaInstance)*.delete()
            metaInstance.delete(flush: true)
            render "dummy"
        } catch (DataIntegrityViolationException e) {
            response.status = 500
            render "dummy"
        }
    }
}
