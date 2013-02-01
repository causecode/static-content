package com.cc.navigation

import org.springframework.dao.DataIntegrityViolationException

class MenuItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

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
        def parentInstance = new MenuItem()
        parentInstance.title = params.title
        parentInstance.url = params.url
        if(params.childTitle) {
            if (params.childTitle.class.isArray()) {
                for(def i=0; i<params.childTitle.length; i++) {
                    def childInstance = new MenuItem()
                    childInstance.title = params.childTitle[i]
                    childInstance.url = params.childUrl[i]
                    childInstance.parent = parentInstance
                    parentInstance.addToChildItems(childInstance)
                    childInstance.validate()
                    println childInstance.errors
                }
            }
            else {
                def childInstance = new MenuItem()
                childInstance.title = params.childTitle
                childInstance.url = params.childUrl
                childInstance.parent = parentInstance
                parentInstance.addToChildItems(childInstance)
                childInstance.validate()
                println childInstance.errors
            }
        }
        if (!parentInstance.save(flush: true)) {
            render(view: "create", model: [menuItemInstance: parentInstance])
            return
        }
        if(!parentInstance.parent && params.selectedMenu) {
            parentInstance.menu = Menu.get(params.selectedMenu)
            def menuInstance = Menu.get(params.selectedMenu)
            menuInstance.addToMenuItem(parentInstance)
        }
        redirect(action: "show", id: parentInstance.id)
    }

    def show(Long id) {
        def menuItemInstance = MenuItem.get(id)
        if (!menuItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'menuItem.label', default: 'MenuItem'), id])
            redirect(action: "list")
            return
        }

        [menuItemInstance: menuItemInstance]
    }

    def edit(Long id) {
        def menuItemInstance = MenuItem.get(id)
        if (!menuItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'menuItem.label', default: 'MenuItem'), id])
            redirect(action: "list")
            return
        }
        [menuItemInstance: menuItemInstance]
    }

    def update(Long id, Long version) {
        def parentInstance = MenuItem.get(id)
        if (!parentInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'menuItem.label', default: 'MenuItem'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (parentInstance.version > version) {
                parentInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'menuItem.label', default: 'MenuItem')] as Object[],
                          "Another user has updated this MenuItem while you were editing")
                render(view: "edit", model: [menuItemInstance: parentInstance])
                return
            }
        }
  
        parentInstance.title = params.title
        parentInstance.url = params.url
        if(params.childTitle) {
            if (params.childTitle.class.isArray()) {
                for(def i=0; i<params.childTitle.length; i++) {
                    def childInstance = new MenuItem()
                    childInstance.title = params.childTitle[i]
                    childInstance.url = params.childUrl[i]
                    childInstance.parent = parentInstance
                    parentInstance.addToChildItems(childInstance)
                    childInstance.validate()
                    println childInstance.errors
                }
            }
            else {
                def childInstance = new MenuItem()
                childInstance.title = params.childTitle
                childInstance.url = params.childUrl
                childInstance.parent = parentInstance
                parentInstance.addToChildItems(childInstance)
                childInstance.validate()
                println childInstance.errors
            }
        }
       if (!parentInstance.save(flush: true)) {
            render(view: "edit", model: [menuItemInstance: parentInstance])
            return
        }

       if(!parentInstance.parent && params.selectedMenu) {
           if(!(parentInstance.menu == Menu.get(params.selectedMenu))) {
               if(parentInstance.menu) {
                   parentInstance.menu.removeFromMenuItem(parentInstance)
               }
               parentInstance.menu = Menu.get(params.selectedMenu)
               def menuInstance = Menu.get(params.selectedMenu)
               menuInstance.addToMenuItem(parentInstance)
           }
        }
        else if(!params.selectedMenu) {
            if(parentInstance.menu) {
                parentInstance.menu.removeFromMenuItem(parentInstance)
                parentInstance.menu = null
            }
        }
        flash.message = message(code: 'default.updated.message', args: [message(code: 'menuItem.label', default: 'MenuItem'), parentInstance.id])
        redirect(action: "show", id: parentInstance.id)
    }

    def delete(Long id) {
        def menuItemInstance = MenuItem.get(id)
        if (!menuItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'menuItem.label', default: 'MenuItem'), id])
            redirect(action: "list")
            return
        }

        try {
            menuItemInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'menuItem.label', default: 'MenuItem'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'menuItem.label', default: 'MenuItem'), id])
            redirect(action: "show", id: id)
        }
    }
    
    def deleteChild() {
        def menuItemInstance = MenuItem.get(params.id)
        def parentId = menuItemInstance.parent.id
        if (!menuItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'menuItem.label', default: 'MenuItem'), id])
            redirect(action: "list")
            return
        }
        try {
            menuItemInstance.delete(flush: true)
            def parentInstance = MenuItem.get(parentId)
            render(view: "edit", model: [menuItemInstance: parentInstance])
            return
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'menuItem.label', default: 'MenuItem'), id])
            redirect(action: "show", id: parentId)
        }
    }
}
