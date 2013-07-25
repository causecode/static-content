/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.navigation

import org.springframework.dao.DataIntegrityViolationException
import com.cc.content.navigation.*

class MenuItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def beforeInterceptor = [action: this.&validate]

    MenuItem menuItemInstance
    //Menu menuInstance
    def mainMenu
    def mainMenuItem
    def menuItemService


    private validate() {
        if(!params.id) return true;

        menuItemInstance = MenuItem.get(params.id)
        if(!menuItemInstance) {
            flash.message = g.message(code: 'default.not.found.message',
            args: [message(code: 'menuItem.label', default: 'MenuItem'), params.title])
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
        [menuItemInstanceList: MenuItem.list(params), menuItemInstanceTotal: MenuItem.count()]
    }

    def create() {
        [menuItemInstance: new MenuItem(params)]
    }

    def save() {
        menuItemInstance = menuItemService.create(params)

        flash.message = message(code: 'default.created.message',
        args: [message(code: 'menuItem.label', default: 'MenuItem'), menuItemInstance.title])
        redirect(action: "show", id: menuItemInstance.id)
    }

    def show(Long id) {
        [menuItemInstance: menuItemInstance]
    }

    def edit(Long id) {
        [menuItemInstance: menuItemInstance]
    }

    def update(Long id, Long version) {
        if(version != null) {
            if (menuItemInstance.version > version) {
                menuItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'menuItem.label', default: 'MenuItem')] as Object[],
                        "Another user has updated this MenuItem while you were editing")
                render(view: "edit", model: [menuItemInstance: menuItemInstance])
                return
            }
        }

        menuItemInstance = menuItemService.update(menuItemInstance , params)
        if(menuItemInstance.hasErrors()) {
            render(view: "create", model: [menuItemInstance: menuItemInstance])
            return
        }
        flash.message = message(code: 'default.updated.message',
        args: [message(code: 'menuItem.label', default: 'MenuItem'), menuItemInstance.title])
        redirect(action: "show", id: menuItemInstance.id)
    }

    def delete(Long id) {
        try {
            menuItemInstance = menuItemService.deleteMenuItem(menuItemInstance)

            //menuItemInstance?.delete(flush: true)
            flash.message = message(code: 'default.deleted.message',
            args: [message(code: 'menuItem.label', default: 'MenuItem'), menuItemInstance.title])
            redirect(action: "list")
        } catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message',
            args: [message(code: 'menuItem.label', default: 'MenuItem'), menuItemInstance.title])
            redirect(action: "show", id: id)
        }
    }

    def editOrder() {
        Menu menuInstance = Menu.get(params.id)
        if(request.xhr){
            menuItemInstance = menuItemService.editMenuItemsOrder(params)
        }
        [menuItemInstanceList: menuInstance?.menuItems, menuInstanceTotal: Menu.count(),menuInstance:menuInstance]
    }
    def jqui() {

    }
}