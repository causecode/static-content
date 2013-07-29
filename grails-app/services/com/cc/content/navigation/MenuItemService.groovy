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
        println "****************after save" + menuItemInstance
        return menuItemInstance
    }

    MenuItem update(MenuItem menuItemInstance , Map args) {
        def menuInstance
        def mainMenuInstance
        def subMenuItemInstance

        menuItemInstance.properties = args
        println "menuItemInstance.properties" +  menuItemInstance.properties
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
        println "****************after update" + menuItemInstance
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

    MenuItem editMenuItemsOrder(menuId,menuItemId,parentId,index){
        
        println "menuId"+menuId+"menuItemId"+menuItemId+"parentId"+parentId+"index"+index
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

    MenuItem saveMenuItems(Map args) {
        println "params for saving temp menuItem " + args
        def title = args?.title
        def role = args?.role
        def url = args?.url
        def showOnlyWhenLoggedIn = args?.showOnlyWhenLoggedIn
        def menuInstance
        def mainMenuInstance
        def subMenuItemInstance

        MenuItem menuItemInstance = new MenuItem()
        menuItemInstance.title = title
        menuItemInstance.roles = role
        menuItemInstance.url = url
        menuItemInstance.showOnlyWhenLoggedIn = showOnlyWhenLoggedIn
        if(args?.menuId ) {
            menuInstance = Menu.get(args.menuId)
            menuInstance?.addToMenuItems(menuItemInstance)
        }
        if(args?.parentId ) {
            subMenuItemInstance = MenuItem.get(args?.parentId)
            subMenuItemInstance?.addToChildItems(menuItemInstance)
        }
        menuItemInstance.save()
        println "****************after update" + menuItemInstance
        def menuItemId = menuItemInstance.id
        def parentId = args.parentId
        def menuId = args.menuId
        def index = args.index
        println "****************befor order" + menuItemInstance
        menuInstance = editMenuItemsOrder(menuId,menuItemId,parentId,index)
        println "****************after order" + menuItemInstance
        return menuItemInstance
    }
}