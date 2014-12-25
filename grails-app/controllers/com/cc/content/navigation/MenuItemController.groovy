/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.navigation

import java.util.List;

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
@Secured(["permitAll"])
class MenuItemController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    
    static responseFormats = ["json"]

    def beforeInterceptor = [action: this.&validate, except: ["index", "list", "create", "save"]]

    private MenuItem menuItemInstance

    def menuItemService

    private validate() {
        params.putAll(request.JSON)
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
        params.putAll(request.JSON)
        if (params.roles.authority) {
            params.roles = getRoles(params.roles.authority)
        } 
        menuItemInstance = menuItemService.create(params)
//        render menuItemInstance.id
        respond (menuItemInstance)
    }

    def edit(){
        Map responseResult = [title: menuItemInstance.title, url: menuItemInstance.url, roles: menuItemInstance.roles,
            showOnlyWhenLoggedIn: menuItemInstance.showOnlyWhenLoggedIn]
        render responseResult as JSON
    }

    def update(){
        params.putAll(request.JSON)
        params.roles = getRoles(params.roles.authority)
        menuItemInstance = menuItemService.update(menuItemInstance, params)
        respond([success: true])
    }
    

    /**
     * Used to reorder menuItem instance.
     */
    def reorder() {
        menuItemInstance = menuItemService.reorder(menuItemInstance, params)
        println("done..")
        respond([success: true])
    }
    
    def getRoles(List roleList) {
        String roles = ''
        for(def role in roleList) {
            roles = roles + role + ','
        }
        return roles.substring(0, (roles.length() - 1))
    }

}
