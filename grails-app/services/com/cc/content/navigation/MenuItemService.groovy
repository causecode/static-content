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
            println "inseide if ........."
            menuInstance = Menu.get(args.menuId)
            menuInstance?.addToMenuItems(menuItemInstance)
        }
        if(args?.parentId ) {
            println "inseide else if ........."
            subMenuItemInstance = MenuItem.get(args?.parentId)
            subMenuItemInstance?.addToChildItems(menuItemInstance)
        }
        menuItemInstance.save()
        return menuItemInstance
    }

    MenuItem removeFromParentMenuItem(MenuItem menuItemInstance){
        def menuInstance
        MenuItem parentMenuItemInstance

        if(menuItemInstance?.menu) {
            menuInstance = Menu.findById(menuItemInstance.menu?.id)
            menuInstance.removeFromMenuItems(menuItemInstance)
            menuInstance.save()
        } else if(menuItemInstance?.parent) {
            parentMenuItemInstance = MenuItem.get(menuItemInstance.parent?.id)
            parentMenuItemInstance.removeFromChildItems(menuItemInstance)
            parentMenuItemInstance.save()
        }
        return menuItemInstance
    }

    MenuItem deleteMenuItem(MenuItem menuItemInstance){
        removeFromParentMenuItem(menuItemInstance)

        menuItemInstance.childItems?.each { deleteMenuItem(it) }
        menuItemInstance?.delete()
        return menuItemInstance
    }

    MenuItem editMenuItemsOrder(Map args){
        println args
        def menuId = args.menuId
        def menuItemId = args.menuItemId
        def parentMenuItemId = args.parentMenuItemId
        def index = args.index as int
        def parentIndex 
        MenuItem parentMenuItemInstance
        MenuItem newParentMenuItemInstance

        Menu menuInstance = Menu.get(menuId)
        MenuItem menuItemInstance = MenuItem.get(menuItemId)

        /**
         * When performing any actions regarding top level MenuItem
         */
        if(!parentMenuItemId) {
            /**
             * When making a MenuItem a top level MenuItem
             */
            if(menuItemInstance?.parent) {
                parentMenuItemInstance = menuItemInstance?.parent
                parentMenuItemInstance.childItems.remove(menuItemInstance)
            }
            menuInstance?.menuItems.remove(menuItemInstance)
            menuInstance?.menuItems.add(index,menuItemInstance)
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
        newParentMenuItemInstance = MenuItem.get(parentMenuItemId)
        newParentMenuItemInstance.childItems.add(index,menuItemInstance)
        newParentMenuItemInstance.save()
        parentIndex = menuInstance?.menuItems.indexOf(newParentMenuItemInstance) as int
        menuInstance?.menuItems.add(++parentIndex,menuItemInstance)
    }
}