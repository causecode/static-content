/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.meta

import org.springframework.dao.DataIntegrityViolationException

import com.cc.content.ContentMeta

class MetaController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def beforeInterceptor = [action: this.&validate]

    private Meta metaInstance

    private validate() {
        if(!params.id) return true;

        metaInstance = Meta.get(params.id)
        if(!metaInstance) {
            flash.message = g.message(code: 'default.not.found.message', args: [message(code: 'meta.label', default: 'Meta'), params.id])
            redirect(action: "list")
            return false
        }
        return true
    }

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [metaInstanceList: Meta.list(params), metaInstanceTotal: Meta.count()]
    }

    def create() {
        [metaInstance: new Meta(params)]
    }

    def save() {
        metaInstance = new Meta(params)
        if (!metaInstance.save(flush: true)) {
            render(view: "create", model: [metaInstance: metaInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'meta.label', default: 'Meta'), metaInstance.id])
        redirect(action: "show", id: metaInstance.id)
    }

    def show(Long id) {
        [metaInstance: metaInstance]
    }

    def edit(Long id) {
        [metaInstance: metaInstance]
    }

    def update(Long id, Long version) {
        if(version != null) {
            if (metaInstance.version > version) {
                metaInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                [message(code: 'meta.label', default: 'Meta')] as Object[],
                "Another user has updated this Meta while you were editing")
                render(view: "edit", model: [metaInstance: metaInstance])
                return
            }
        }

        metaInstance.properties = params

        if (!metaInstance.save(flush: true)) {
            render(view: "edit", model: [metaInstance: metaInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'meta.label', default: 'Meta'), metaInstance.id])
        redirect(action: "show", id: metaInstance.id)
    }

    def delete(Long id) {
        try {
            ContentMeta.findAllByMeta(metaInstance)*.delete()
            metaInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'meta.label', default: 'Meta'), id])
            redirect(action: "list")
        } catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'meta.label', default: 'Meta'), id])
            redirect(action: "show", id: id)
        }
    }
}