/* Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are not permitted.*/


package com.cc.content


import org.springframework.dao.DataIntegrityViolationException
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils


class ContentController {
	def springSecurityService
	def springSecurityUiService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [contentInstanceList: Content.list(params), contentInstanceTotal: Content.count(), userClass: userClass()]
    }

    def create() {
        [contentInstance: new Content(params)]
    }

    def save() {
        def contentInstance = new Content(params)
		def principal= springSecurityService.getPrincipal()
		if(principal == "anonymousUser") {
					contentInstance.author = principal
		}
		else {
					contentInstance.author = principal.id as String		
		}
		if (!contentInstance.save(flush: true)) {
           render(view: "create", model: [contentInstance: contentInstance])
            return
        }
        flash.message = message(code: 'default.created.message', args: [message(code: 'content.label', default: 'Content'), contentInstance.id])
        redirect(action: "show", id: contentInstance.id)
    }

    def show(Long id) {
        def contentInstance = Content.get(id)
		if (!contentInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'content.label', default: 'Content'), id])
			redirect(action: "list")
			return
		}
		def username
		def userId = contentInstance.author
		if(userId.isNumber()) {
			userId.toInteger()
			def userInstance = userClass().get(userId)
			username= userInstance.username
		}
		else {
			username= "anonymousUser"
		}
        [contentInstance: contentInstance, username : username]
    }

    def edit(Long id) {
        def contentInstance = Content.get(id)
        if (!contentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'content.label', default: 'Content'), id])
            redirect(action: "list")
            return
        }
        [contentInstance: contentInstance]
    }

    def update(Long id, Long version) {
        def contentInstance = Content.get(id)
        if (!contentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'content.label', default: 'Content'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (contentInstance.version > version) {
                contentInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'content.label', default: 'Content')] as Object[],
                          "Another user has updated this Content while you were editing")
                render(view: "edit", model: [contentInstance: contentInstance])
                return
            }       
        }

        contentInstance.properties = params

        if (!contentInstance.save(flush: true)) {
            render(view: "edit", model: [contentInstance: contentInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'content.label', default: 'Content'), contentInstance.id])
        redirect(action: "show", id: contentInstance.id)
    }

    def delete(Long id) {
        def contentInstance = Content.get(id)
        if (!contentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'content.label', default: 'Content'), id])
            redirect(action: "list")
            return
        }

        try {
            contentInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'content.label', default: 'Content'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'content.label', default: 'Content'), id])
            redirect(action: "show", id: id)
        }
    }
	protected String lookupUserClassName() {
		SpringSecurityUtils.securityConfig.userLookup.userDomainClassName
	}

	protected Class<?> userClass() {
		grailsApplication.getDomainClass(lookupUserClassName()).clazz
	}
}
