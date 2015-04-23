/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

import grails.plugin.springsecurity.annotation.Secured
import org.grails.databinding.SimpleMapDataBindingSource
import org.springframework.dao.DataIntegrityViolationException
import com.cc.content.format.TextFormat
import org.springframework.http.HttpStatus
import grails.converters.JSON

@Secured(["ROLE_CONTENT_MANAGER"])
class TextFormatController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def beforeInterceptor = [action: this.&validate]
    static responseFormats= ["json"]

    TextFormat textFormatInstance
    def grailsWebDataBinder
    def textFormatService

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

    @Secured(["ROLE_USER"])
    def getEditorType() {
        List<TextFormat> formatsAvailable = textFormatService.getApplicableFormats()
        respond ([formatsAvailable: formatsAvailable, editor: formatsAvailable.any { it.editor } ])
    }
    
    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond ([instanceList: TextFormat.list(params), totalCount: TextFormat.count()])
    }

    def create() {
        respond ([textFormatInstance: new TextFormat(params)])
    }

    def show(Long id) {
        respond (textFormatInstance)
    }

    def edit(Long id) {
        respond ([textFormatInstance: textFormatInstance])
    }

    def delete(Long id) {
        try {
            textFormatInstance.delete(flush: true)
        } catch (DataIntegrityViolationException e) {
            respond ([status: HttpStatus.NOT_MODIFIED])
            return
        }
        respond([status: HttpStatus.OK ])
    }

    def update(Long id, Long version) {
        Map requestData = request.JSON

        if(version != null) {
            if (textFormatInstance.version > version) {
                respond ([message: "Another user has updated this textFormat instance while you were editing"],
                status: HttpStatus.NOT_MODIFIED)
                return
            }
        }

        grailsWebDataBinder.bind(textFormatInstance, requestData as SimpleMapDataBindingSource, ["name", "allowedTags",
            "roles", "editor"], null)
        
        if (textFormatInstance.hasErrors()) {
            log.warn "Error updating textFormat Instance: $textFormatInstance.errors."
            respond ([errors: textFormatInstance.errors], status: HttpStatus.NOT_MODIFIED)
            return
        } else {
            log.info "Text format instance updated successfully."
            textFormatInstance.save(flush: true)
        }
        respond ([status: HttpStatus.OK])
    }

    def save() {
        Map requestData = request.JSON
        log.info "Parameters received save text format instance: ${requestData}"
        TextFormat textFormatInstance = new TextFormat()
        grailsWebDataBinder.bind(textFormatInstance, requestData as SimpleMapDataBindingSource, ["name", "allowedTags",
            "roles", "editor"], null)
        
        if (textFormatInstance.hasErrors()) {
            log.warn "Error saving TextFormat Instance: $textFormatInstance.errors."
            respond ([errors: textFormatInstance.errors, status: HttpStatus.NOT_MODIFIED])
            return
        } else {
            textFormatInstance.save(flush: true)
        }
        respond ([status: HttpStatus.OK])
    }
}
