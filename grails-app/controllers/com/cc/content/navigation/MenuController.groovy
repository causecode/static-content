/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.navigation

import grails.plugin.springsecurity.annotation.Secured

import org.springframework.dao.DataIntegrityViolationException


/**
 * Provides default CRUD end point for Content Manager.
 * @author Vishesh Duggar
 * @author Laxmi Salunkhe
 * @author Shashank Agrawal
 *
 */
@Secured(["ROLE_CONTENT_MANAGER"])
class MenuController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    static responseFormats = ["json"]

    def contentService
    def menuItemService

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.putAll(request.JSON)
        params.max = Math.min(max ?: 10, 100)
        respond ([instanceList: Menu.list(params), totalCount: Menu.count()])
    }

    def create() {
        def roleList = contentService.getRoleClass().list()
        respond(roleList:roleList)
    }

    def save() {
        params.putAll(request.JSON)
        Menu menuInstance = new Menu(params)
        if (!menuInstance.save(flush: true)) {
            respond(menuInstance.errors)
            return
        }

        respond ([success: true])
    }
    
    def show(Menu menuInstance) {
        List<MenuItem> menuItemInstanceList = menuInstance.menuItems.findAll { !it.parent }
        respond ([menuItemInstanceList: menuItemInstanceList, menuInstance: menuInstance,
            roleList: contentService.getRoleClass().list()])
    }

    def edit(Menu menuInstance) {
        [menuInstance: menuInstance]
    }

    def update(Menu menuInstance, Long version) {
        if(version != null) {
            if (menuInstance.version > version) {
                menuInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'menu.label')] as Object[],
                        "Another user has updated this Menu while you were editing")
                respond(menuInstance.errors)
                return
            }
        }

        menuInstance.properties = params

        if (!menuInstance.save(flush: true)) {
            respond(menuInstance.errors)
            return 
        }

        respond ([success: true])
    }

    def delete(Menu menuInstance) {
        try {
            menuInstance.delete(flush: true)
            respond ([success: true])
        } catch (DataIntegrityViolationException e) {
            respond ([success: false])
        }
    }
}
