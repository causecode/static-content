/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.navigation

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
        def menuItemInstance = new MenuItem()
        update(menuItemInstance , args)
        return menuItemInstance
    }

    MenuItem update(MenuItem menuItemInstance , Map args) {
        def menuInstance
        def mainMenuInstance
        def subMenuItemInstance
        menuItemInstance.properties = args
        if(args?.menuId ) {
            menuInstance = Menu.get(args.menuId)
            if(args?.index) {
                menuInstance?.menuItems.add(args?.index as int,menuItemInstance)
            } else {
                menuInstance?.menuItems.add(0,menuItemInstance)
            }
            menuItemInstance.menu = menuInstance
        }
        if(args?.parentId) {
            subMenuItemInstance = MenuItem.get(args?.parentId)
            if(args?.index) {
                subMenuItemInstance.childItems.add(args.index as int,menuItemInstance)
            } else {
                subMenuItemInstance?.childItems.add(0,menuItemInstance)
            }
        }
        menuItemInstance.save()
        return menuItemInstance
    }

    MenuItem removeFromParentMenuItem(MenuItem menuItemInstance){
        def menuInstance
        MenuItem parentMenuItemInstance

        if(menuItemInstance?.menu) {
            menuInstance = Menu.findById(menuItemInstance.menu?.id)
            menuInstance?.menuItems.remove(menuItemInstance)
            //menuInstance.removeFromMenuItems(menuItemInstance)
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
        menuItemInstance?.childItems?.each { deleteMenuItem(it) }
        menuItemInstance?.delete()
        return menuItemInstance
    }

    MenuItem editMenuItemsOrder(Map args){
        def menuId = args.menuId
        def menuItemId = args.menuItemId
        def parentId = args.parentId
        def index = args.index as int
        def parentIndex
        MenuItem parentMenuItemInstance
        MenuItem newParentMenuItemInstance

        Menu menuInstance = Menu.get(menuId)
        MenuItem menuItemInstance = MenuItem.get(menuItemId)

        /**
         * When performing any actions regarding top level MenuItem
         */
        if(!parentId) {
            /**
             * When making a MenuItem a top level MenuItem
             */
            if(menuItemInstance?.parent) {
                parentMenuItemInstance = menuItemInstance?.parent
                parentMenuItemInstance.childItems.remove(menuItemInstance)
            }
            menuInstance?.menuItems.remove(menuItemInstance)
            menuInstance?.menuItems.add(index as int,menuItemInstance)
            return
        }
        /**
         * When sorting any non-top level MenuItem
         */
        if(menuItemInstance?.parent) {
            parentMenuItemInstance = menuItemInstance?.parent
            parentMenuItemInstance?.childItems.remove(menuItemInstance)
        }
        /**
         * When making a top level MenuItem a child MenuItem
         */
        else {
            menuInstance.removeFromMenuItems(menuItemInstance)
            menuInstance.save()
        }
        newParentMenuItemInstance = MenuItem.get(parentId)
        newParentMenuItemInstance.childItems.add(index as int,menuItemInstance)
        newParentMenuItemInstance.save()
        parentIndex = menuInstance?.menuItems.indexOf(newParentMenuItemInstance) as int
        menuInstance?.menuItems.add(++parentIndex,menuItemInstance)
    }
}