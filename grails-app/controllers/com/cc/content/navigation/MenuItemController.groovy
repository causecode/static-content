/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.navigation

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus

/**
 * Provides end point to reorder menu items for Content Manager.
 * @author Vishesh Duggar
 * @author Laxmi Salunkhe
 * @author Shashank Agrawal
 *
 */
@Secured(["ROLE_CONTENT_MANAGER"])
class MenuItemController {

    static allowedMethods = [save: "POST", reorder: "POST", update: "PUT", delete: "DELETE"]
    
    static responseFormats = ["json"]

    MenuItemService menuItemService

    def delete(MenuItem menuItemInstance) {
        try {
            menuItemInstance = menuItemService.delete(menuItemInstance)
        } catch (DataIntegrityViolationException e) {
            respond ([status: HttpStatus.NOT_MODIFIED])
            return
        }
        respond ([status: HttpStatus.OK])
    }

    def save() {
        Map requestData = request.JSON
        MenuItem menuItemInstance = menuItemService.create(requestData)
        respond (menuItemInstance)
    }

    def edit(MenuItem menuItemInstance){
        Map responseResult = [title: menuItemInstance.title, url: menuItemInstance.url, roles: menuItemInstance.roles,
            showOnlyWhenLoggedIn: menuItemInstance.showOnlyWhenLoggedIn]
        render responseResult as JSON
    }

    def update(MenuItem menuItemInstance){
        Map requestData = request.JSON
        menuItemInstance = menuItemService.update(menuItemInstance, requestData)
        if (menuItemInstance.hasErrors()) {
            log.warn "Error updating menuItem Instance: $menuItemInstance.errors."
            respond ([errors: menuItemInstance.errors], status: HttpStatus.NOT_MODIFIED)
            return
        } else {
            log.info "MenuItem instance updated successfully."
            menuItemInstance.save(flush: true)
        }
        respond ([status: HttpStatus.OK])
    }

    /**
     * Used to reorder menuItem instance.
     */
    def reorder(MenuItem menuItemInstance) {
        log.info "Parameters recieved to reorder menu items: ${params}"
        if (!params.index) {
            log.warn "Unable to reorder menu items. Index Parameters not received."
            respond ([message: message(code: 'menuItem.reorder.fail'), status: HttpStatus.NOT_ACCEPTABLE])
            return
        }
        menuItemInstance = menuItemService.reorder(menuItemInstance, params)
        respond([success: true])
    }
}