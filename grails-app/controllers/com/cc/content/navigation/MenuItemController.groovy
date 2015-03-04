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
            respond ([success: true])
        } catch (DataIntegrityViolationException e) {
            respond ([success: false])
        }
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
        respond([success: true])
    }

    /**
     * Used to reorder menuItem instance.
     */
    def reorder(MenuItem menuItemInstance) {
        menuItemInstance = menuItemService.reorder(menuItemInstance, params)
        respond([success: true])
    }
}