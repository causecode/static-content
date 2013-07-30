/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.navigation

import org.springframework.dao.DataIntegrityViolationException

class MenuController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def beforeInterceptor = [action: this.&validate]

    Menu menuInstance
    MenuItem menuItemInstance
    def menuItemService
    def springSecurityService
    
    private validate() {
        if(!params.id) return true;

        menuInstance = Menu.get(params.id)
        if(!menuInstance) {
            flash.message = g.message(code: 'default.not.found.message', args: [message(code: 'menu.label', default: 'Menu'), params.name])
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
        [menuInstanceList: Menu.list(params), menuInstanceTotal: Menu.count()]
    }

    def create() {
        [menuInstance: new Menu(params)]
    }

    def save() {
        menuInstance = new Menu(params)
        if (!menuInstance.save(flush: true)) {
            render(view: "create", model: [menuInstance: menuInstance])
            return
        }

        flash.message = message(code: 'default.created.message', 
            args: [message(code: 'menu.label', default: 'Menu'), menuInstance.name])
        redirect(action: "show", id: menuInstance.id)
    }

    def show(Long id) {
       menuInstance = Menu.get(params.id)
        if(request.xhr){
            menuItemInstance = menuItemService.editMenuItemsOrder(params)
        }
        [menuItemInstanceList: menuInstance?.menuItems, menuInstanceTotal: Menu.count(),menuInstance:menuInstance]
    }

    def edit(Long id) {
        [menuInstance: menuInstance]
    }

    def update(Long id, Long version) {
        if(version != null) {
            if (menuInstance.version > version) {
                menuInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                [message(code: 'menu.label', default: 'Menu')] as Object[],
                "Another user has updated this Menu while you were editing")
                render(view: "edit", model: [menuInstance: menuInstance])
                return
            }
        }

        menuInstance.properties = params

        if (!menuInstance.save(flush: true)) {
            render(view: "edit", model: [menuInstance: menuInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', 
            args: [message(code: 'menu.label', default: 'Menu'), menuInstance.name])
        redirect(action: "show", id: menuInstance.id)
    }

    def delete(Long id) {
        try {
            menuInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', 
                args: [message(code: 'menu.label', default: 'Menu'), name])
            redirect(action: "list")
        } catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', 
                args: [message(code: 'menu.label', default: 'Menu'), name])
            redirect(action: "show", id: id)
        }
    }
    
}