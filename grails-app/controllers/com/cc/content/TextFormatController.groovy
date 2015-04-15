/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

import grails.plugin.springsecurity.annotation.Secured

import org.springframework.dao.DataIntegrityViolationException

import com.cc.content.format.TextFormat

@Secured(["ROLE_USER"])
class TextFormatController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def beforeInterceptor = [action: this.&validate]
    static responseFormats= ["json"]   //Server should respond only in json format 
    
    TextFormat textFormatInstance

    private validate() {
        if(!params.id) return true;
        textFormatInstance = TextFormat.get(params.id)      //Get the TF instances from he input ids
        if(!textFormatInstance) {
            flash.message = g.message(code: 'default.not.found.message', args: [message(code: 'textFormat.label', default: 'TextFormat'), params.id])
            redirect(action: "list", max : params.max)
            return false
        }
        return true
    }

    def index() {
        log.info("params recieved",params)
        redirect(action: "list", params: params.max)        //go to the below 'list' action
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        //Send back to the client the following response in json format
        respond ([textFormatInstanceList: TextFormat.list(params), textFormatInstanceTotal: TextFormat.count()])
    }

    def create() {
      respond ({textFormatInstance: new TextFormat(params)})
    }

    def save() {
        textFormatInstance = new TextFormat(params)
        if (!textFormatInstance.save(flush: true)) {
            render([view: "create", textFormatInstance: textFormatInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'textFormat.label', default: 'TextFormat'), textFormatInstance.id])
        redirect(action: "show", id: textFormatInstance.id)
    }

    def show(Long id) {
       respond ({textFormatInstance: textFormatInstance})       //Calls C.S Angular controller & renders the respective view for Show
    }

    def edit(Long id) {
      respond ({textFormatInstance: textFormatInstance})
    }

    def update(Long id, Long version) {
        if(version != null) {
            if (textFormatInstance.version > version) {
                textFormatInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                [message(code: 'textFormat.label', default: 'TextFormat')] as Object[],
                "Another user has updated this TextFormat while you were editing")
                
                //Redirect to CS Angular 'Edit view' 
                //render(view: "edit", model: [textFormatInstance: textFormatInstance])
                respond ([uri:'textFormat/edit', textFormatInstance: textFormatInstance])
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
            redirect(action: "list", max : params.max)
        } catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'textFormat.label', default: 'TextFormat'), id])
            redirect(action: "show", id: id)
        }
    }
}