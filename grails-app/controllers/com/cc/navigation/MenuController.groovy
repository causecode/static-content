package com.cc.navigation

import org.springframework.dao.DataIntegrityViolationException

class MenuController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [menuInstanceList: Menu.list(params), menuInstanceTotal: Menu.count()]
    }

    def create() {
        def topLevelMenuItems = MenuItem.where {
                (parent == null && menu == null)
        }
        [menuInstance: new Menu(params), topLevelMenuItems: topLevelMenuItems]
    }

    def save() {
        def menuInstance = new Menu()
        menuInstance.name = params.name
        if(params.menuItem) {
            if(params.menuItem.class.isArray()) {
                params.menuItem.each {
                    menuInstance.addToMenuItem(MenuItem.get(it))
                }
            }
            else {
                menuInstance.addToMenuItem(MenuItem.get(params.menuItem))
             }
        }
        if (!menuInstance.save(flush: true)) {
            def topLevelMenuItems = MenuItem.where {
                (parent == null && menu == null)
            }
            render(view: "create", model: [menuInstance: menuInstance, topLevelMenuItems: topLevelMenuItems])
            return
        }
        menuInstance.menuItem.each {
            it.menu = menuInstance    
        }
        redirect(action: "show", id: menuInstance.id)
    }

    def show(Long id) {
        def menuInstance = Menu.get(id)
        if (!menuInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'menu.label', default: 'Menu'), id])
            redirect(action: "list")
            return
        }

        [menuInstance: menuInstance]
    }

    def edit(Long id) {
        def menuInstance = Menu.get(id)
        if (!menuInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'menu.label', default: 'Menu'), id])
            redirect(action: "list")
            return
        }
        def topLevelMenuItems = MenuItem.where {
             (parent == null && menu == null)
        }
        [menuInstance: menuInstance, topLevelMenuItems: topLevelMenuItems]
    }

    def update(Long id, Long version) {
        def menuInstance = Menu.get(id)
        if (!menuInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'menu.label', default: 'Menu'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (menuInstance.version > version) {
                menuInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'menu.label', default: 'Menu')] as Object[],
                          "Another user has updated this Menu while you were editing")
                def topLevelMenuItems = MenuItem.where {
                    (parent == null && menu == null)
                }
                render(view: "edit", model: [menuInstance: menuInstance, topLevelMenuItems: topLevelMenuItems])
                return
            }
        }

        menuInstance.name = params.name
        if(params.menuItem) {
            if(params.menuItem.class.isArray()) {
                params.menuItem.each {
                    menuInstance.addToMenuItem(MenuItem.get(it))
                }
            }
            else {
                menuInstance.addToMenuItem(MenuItem.get(params.menuItem))
             }
        }
        if (!menuInstance.save(flush: true)) {
            def topLevelMenuItems = MenuItem.where {
                (parent == null && menu == null)
            }
            render(view: "edit", model: [menuInstance: menuInstance, topLevelMenuItems: topLevelMenuItems])
            return
        }
        menuInstance.menuItem.each {
            it.menu = menuInstance
        }
        
        flash.message = message(code: 'default.updated.message', args: [message(code: 'menu.label', default: 'Menu'), menuInstance.id])
        redirect(action: "show", id: menuInstance.id)
    }

    def delete(Long id) {
        def menuInstance = Menu.get(id)
        if (!menuInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'menu.label', default: 'Menu'), id])
            redirect(action: "list")
            return
        }

        try {
            menuInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'menu.label', default: 'Menu'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'menu.label', default: 'Menu'), id])
            redirect(action: "show", id: id)
        }
    }
    
    def deleteChild() {
        def menuItemInstance = MenuItem.get(params.id)
        def menuInstance = menuItemInstance.menu
        if (!menuItemInstance) {
            redirect(action: "list")
            return
        }
        try {
            if(menuInstance.menuItem.contains(menuItemInstance)) {
                menuInstance.removeFromMenuItem(menuItemInstance)
                menuItemInstance.menu = null
            }
            def topLevelMenuItems = MenuItem.where {
                (parent == null && menu == null)
            }
            render(view: "edit", model: [menuInstance: menuInstance, topLevelMenuItems: topLevelMenuItems])
            return
        }
        catch (DataIntegrityViolationException e) {
           redirect(action: "show", id: menuInstance.id)
        }
    }
}
