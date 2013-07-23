/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

import com.cc.content.navigation.MenuItem
import com.cc.content.navigation.Menu
import org.codehaus.groovy.grails.commons.metaclass.GroovyDynamicMethodsInterceptor
import org.codehaus.groovy.grails.web.metaclass.BindDynamicMethod

class MenuItemService {

    MenuItemService() {
        GroovyDynamicMethodsInterceptor i = new GroovyDynamicMethodsInterceptor(this)
        i.addDynamicMethodInvocation(new BindDynamicMethod())
    }

    MenuItem create(Map args) {
        def menuItemInstance = new MenuItem(args)
        if (!menuItemInstance.save(flush: true)) {
            render(view: "create", model: [menuItemInstance: menuItemInstance])
            return
        }
        update(menuItemInstance , args)
        return menuItemInstance
    }

    MenuItem update(MenuItem menuItemInstance , Map args) {
        def menuInstance
        def mainMenuItemInstance
        if(args?.menuId) {
            menuInstance = Menu.get(args.menuId)
            menuInstance?.addToMenuItems(menuItemInstance)
        }
        if(args?.parentId) {
            mainMenuItemInstance = MenuItem.get(args?.parentId)
            mainMenuItemInstance?.addToChildItems(menuItemInstance)
        }
        return menuItemInstance
    }

    MenuItem removeFromParentMenuItem(MenuItem menuItemInstance){
        def menuInstance
        MenuItem parentMenuItemInstance

        if(menuItemInstance?.menu){
            menuInstance = Menu.findById(menuItemInstance.menu?.id)
            menuInstance.removeFromMenuItems(menuItemInstance)
            menuInstance.save()
        }
        if(menuItemInstance?.parent) {
            parentMenuItemInstance = MenuItem.get(menuItemInstance.parent?.id)
            parentMenuItemInstance.removeFromChildItems(menuItemInstance)
            parentMenuItemInstance.save()
        }
        return menuItemInstance
    }

    MenuItem deleteMenuItem(MenuItem menuItemInstance){
        removeFromParentMenuItem(menuItemInstance)

        menuItemInstance.childItems?.each {
            deleteMenuItem(it)
        }
        menuItemInstance?.delete()
        return menuItemInstance
    }

    MenuItem removeChildMenuItem(MenuItem menuItemInstance){
        removeFromParentMenuItem(menuItemInstance)

        menuItemInstance.childItems?.each {
            deleteMenuItem(it)
        }
        menuItemInstance?.delete()
        return menuItemInstance
    }

    MenuItem editMenuItemsOrder(elementId,index){
        def menuItemInstance = MenuItem.get(elementId)
        def parentMenuItemInstance = menuItemInstance.parent
        List menuItemList = parentMenuItemInstance.childItems
        println elementId+ "**************************"+index
        menuItemList.add(index , menuItemInstance)
    }
}