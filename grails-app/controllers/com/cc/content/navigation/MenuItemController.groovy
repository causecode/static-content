/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.navigation

import grails.converters.JSON

import org.springframework.dao.DataIntegrityViolationException

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
            if(request.xhr) {
                MenuItem menuItemInstance = MenuItem.get(params.menuItemId)
                menuItemInstance = menuItemService.deleteMenuItem(menuItemInstance)
                render menuItemInstance as JSON
            }
        } catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message',
            args: [message(code: 'menuItem.label'), menuItemInstance.title])
            redirect(action: "show", id: id)
        }
    }

    def save() {
        menuItemInstance = menuItemService.create(params)
        render menuItemInstance.id
    }

    def edit(){
        def itemData = ['title':menuItemInstance.title,'url':menuItemInstance.url,'roles':menuItemInstance.roles,
            'showOnlyWhenLoggedIn':menuItemInstance.showOnlyWhenLoggedIn]
        render itemData as JSON
    }

    def update(){
        menuItemInstance = menuItemService.update(menuItemInstance, params)
    }

    def reorder() {
        menuItemInstance = menuItemService.reorder(menuItemInstance, params)
        render ([success: true]) as JSON
    }

}