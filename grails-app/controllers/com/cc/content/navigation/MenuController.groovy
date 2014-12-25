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
@Secured(["permitAll"])
class MenuController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    
    static responseFormats = ["json"]

    def beforeInterceptor = [action: this.&validate, except: ["index", "list", "create", "save","getRoleList"]]

    private Menu menuInstance
    private MenuItem menuItemInstance

    def contentService
    def menuItemService

    private validate() {
        println(">>>>>>>>in validate")
        menuInstance = Menu.get(params.id)
        if(!menuInstance) {
            flash.message = g.message(code: 'default.not.found.message', args: [message(code: 'menu.label'), params.id])
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
//        [menuInstance: new Menu(params), Role: contentService.getRoleClass()]
        def roleList = contentService.getRoleClass().list()
        respond(roleList:roleList)
    }

    def save() {
        params.putAll(request.JSON)
        params.roles = getRoles(params.role.authority)
        menuInstance = new Menu(params)
        if (!menuInstance.save(flush: true)) {
            respond(menuInstance.errors)
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'menu.label'), menuInstance.name])
        redirect(action: "show", id: menuInstance.id)
    }
    
    def show(Long id) {
        List<MenuItem> menuItemInstanceList = menuInstance.menuItems.findAll { !it.parent }
        respond ([menuItemInstanceList: menuItemInstanceList, menuInstance: menuInstance,
            roleList: contentService.getRoleClass().list()])
    }

    def edit(Long id) {
        [menuInstance: menuInstance]
    }

    def update(Long id, Long version) {
        if(version != null) {
            if (menuInstance.version > version) {
                menuInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'menu.label')] as Object[],
                        "Another user has updated this Menu while you were editing")
                render(view: "edit", model: [menuInstance: menuInstance, Role: contentService.getRoleClass()])
                return
            }
        }

        menuInstance.properties = params

        if (!menuInstance.save(flush: true)) {
            render(view: "edit", model: [menuInstance: menuInstance, Role: contentService.getRoleClass()])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'menu.label'), menuInstance.name])
        redirect(action: "show", id: menuInstance.id)
    }

    def delete(Long id) {
        try {
            menuInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'menu.label'), menuInstance.name])
            redirect(action: "list")
        } catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'menu.label'), menuInstance.name])
            redirect(action: "show", id: id)
        }
    }
    
    def getRoles(List roleList) {
        String roles = ''
        for(def role in roleList) {
            roles = roles + role + ','
        }
        return roles.substring(0, (roles.length() - 1))
    }


}
