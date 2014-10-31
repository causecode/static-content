/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.inputWidget

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
class InputWidgetController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def beforeInterceptor = [action: this.&validate, except: ["index", "list", "create", "save"]]

    InputWidget inputWidgetInstance

    private validate() {
        inputWidgetInstance = InputWidget.get(params.id)
        if(!inputWidgetInstance) {
            flash.message = g.message(code: 'default.not.found.message', args: [message(code: 'inputWidget.label', default: 'InputWidget'), params.id])
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
        [inputWidgetInstanceList: InputWidget.list(params), inputWidgetInstanceTotal: InputWidget.count()]
    }

    def create() {
        [inputWidgetInstance: new InputWidget(params)]
    }

    def save() {
        inputWidgetInstance = new InputWidget(params)
        if (!inputWidgetInstance.save(flush: true)) {
            render(view: "create", model: [inputWidgetInstance: inputWidgetInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'inputWidget.label', default: 'InputWidget'), inputWidgetInstance.id])
        redirect(action: "show", id: inputWidgetInstance.id)
    }

    def show(Long id) {
        [inputWidgetInstance: inputWidgetInstance]
    }

    def edit(Long id) {
        [inputWidgetInstance: inputWidgetInstance]
    }

    def update(Long id, Long version) {
        if(version != null) {
            if (inputWidgetInstance.version > version) {
                inputWidgetInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'inputWidget.label', default: 'InputWidget')] as Object[],
                        "Another user has updated this InputWidget while you were editing")
                render(view: "edit", model: [inputWidgetInstance: inputWidgetInstance])
                return
            }
        }

        inputWidgetInstance.properties = params

        if (!inputWidgetInstance.save(flush: true)) {
            render(view: "edit", model: [inputWidgetInstance: inputWidgetInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'inputWidget.label', default: 'InputWidget'), inputWidgetInstance.id])
        redirect(action: "show", id: inputWidgetInstance.id)
    }

    def delete(Long id) {
        try {
            inputWidgetInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'inputWidget.label', default: 'InputWidget'), id])
            redirect(action: "list")
        } catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'inputWidget.label', default: 'InputWidget'), id])
            redirect(action: "show", id: id)
        }
    }

}
