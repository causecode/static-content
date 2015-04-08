/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

import org.springframework.dao.DataIntegrityViolationException

import com.cc.content.format.TextFormat;

class TextFormatController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def beforeInterceptor = [action: this.&validate]

    TextFormat textFormatInstance

    private validate() {
        if(!params.id) return true;

        textFormatInstance = TextFormat.get(params.id)
        if(!textFormatInstance) {
            flash.message = g.message(code: 'default.not.found.message', args: [message(code: 'textFormat.label', default: 'TextFormat'), params.id])
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
        [textFormatInstanceList: TextFormat.list(params), textFormatInstanceTotal: TextFormat.count()]
    }

    def create() {
        [textFormatInstance: new TextFormat(params)]
    }

    def save() {
        textFormatInstance = new TextFormat(params)
        if (!textFormatInstance.save(flush: true)) {
            render(view: "create", model: [textFormatInstance: textFormatInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'textFormat.label', default: 'TextFormat'), textFormatInstance.id])
        redirect(action: "show", id: textFormatInstance.id)
    }

    def show(Long id) {
        [textFormatInstance: textFormatInstance]
    }

    def edit(Long id) {
        [textFormatInstance: textFormatInstance]
    }

    def update(Long id, Long version) {
        if(version != null) {
            if (textFormatInstance.version > version) {
                textFormatInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                [message(code: 'textFormat.label', default: 'TextFormat')] as Object[],
                "Another user has updated this TextFormat while you were editing")
                render(view: "edit", model: [textFormatInstance: textFormatInstance])
                return
            }
        }

        textFormatInstance.properties = params

        if (!textFormatInstance.save(flush: true)) {
            render(view: "edit", model: [textFormatInstance: textFormatInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'textFormat.label', default: 'TextFormat'), textFormatInstance.id])
        redirect(action: "show", id: textFormatInstance.id)
    }

    def delete(Long id) {
        try {
            textFormatInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'textFormat.label', default: 'TextFormat'), id])
            redirect(action: "list")
        } catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'textFormat.label', default: 'TextFormat'), id])
            redirect(action: "show", id: id)
        }
    }
}