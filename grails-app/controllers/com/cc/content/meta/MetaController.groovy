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

@Secured(["ROLE_CONTENT_MANAGER"])
class MetaController {

    def beforeInterceptor = [action: this.&validate]

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
