/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.navigation

import grails.converters.JSON
import grails.plugins.springsecurity.Secured

import org.springframework.dao.DataIntegrityViolationException

@Secured(["ROLE_CONTENT_MANAGER"])
class MenuItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def beforeInterceptor = [action: this.&validate]

    private MenuItem menuItemInstance

    def menuItemService

    private validate() {
        if(!params.id) return true;

        menuItemInstance = MenuItem.get(params.id)
        if(!menuItemInstance) {
            response.sendError = 404
            flash.message = g.message(code: 'default.not.found.message', args: [message(code: 'menuItem.label'), params.id])
            redirect(action: "list")
            return false
        }
        return true
    }

    def delete(Long id) {
        try {
            menuItemInstance = menuItemService.delete(menuItemInstance)
            render true
        } catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'menuItem.label'), menuItemInstance.title])
        }
    }

    def save() {
        menuItemInstance = menuItemService.create(params)
        render menuItemInstance.id
    }

    def edit(){
        Map responseResult = [title: menuItemInstance.title, url: menuItemInstance.url, roles: menuItemInstance.roles,
            showOnlyWhenLoggedIn: menuItemInstance.showOnlyWhenLoggedIn]
        render responseResult as JSON
    }

    def update(){
        menuItemInstance = menuItemService.update(menuItemInstance, params)
        render ([success: true] as JSON)
    }

    def reorder() {
        menuItemInstance = menuItemService.reorder(menuItemInstance, params)
        render ([success: true]) as JSON
    }

}