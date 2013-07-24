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
        if(args?.menuId && !menuItemInstance.menuId) {
            menuInstance = Menu.get(args.menuId)
            menuInstance?.addToMenuItems(menuItemInstance)
        }else if(args?.parentId && !menuItemInstance.parentId) {
            subMenuItemInstance = MenuItem.get(args?.parentId)
            subMenuItemInstance?.addToChildItems(menuItemInstance)
        }
        menuItemInstance.save()
        return menuItemInstance
    }

    MenuItem removeFromParentMenuItem(MenuItem menuItemInstance){
        def menuInstance
        MenuItem parentMenuItemInstance

        if(menuItemInstance?.menu){
            menuInstance = Menu.findById(menuItemInstance.menu?.id)
            menuInstance.removeFromMenuItems(menuItemInstance)
            menuInstance.save()
        }else (menuItemInstance?.parent) {
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

    MenuItem editMenuItemsOrder(menuItemId,index,parentMenuItemId){
        List mainMenuItemList = []
        List parentSubMenuItemList = []
        List newParentSubMenuItemList = []
        Menu menuInstance = Menu.get("1")
        MenuItem parentMenuItemInstance
        MenuItem menuItemInstance = MenuItem.get(menuItemId)
        
        if(!parentMenuItemId) {
            List allMenuItemList = menuInstance.menuItems
            
            allMenuItemList.each {
                if(!it.parent) {
                    mainMenuItemList.add(it)
                }
            }
            
            if(menuItemInstance?.parent) {
                parentMenuItemInstance = menuItemInstance?.parent
                parentSubMenuItemList = parentMenuItemInstance?.childItems
                parentSubMenuItemList.remove(menuItemInstance)
            } else {
                menuInstance?.removeFromMenuItems(menuItemInstance)
                //mainMenuItemList?.remove(menuItemInstance)
            }
            
            menuInstance?.menuItems.add(index,menuItemInstance)
            //mainMenuItemList.add(index,menuItemInstance)
        } else {
            if(menuItemInstance?.parent) {
                parentMenuItemInstance = menuItemInstance?.parent
                parentSubMenuItemList = parentMenuItemInstance?.childItems
                parentSubMenuItemList.remove(menuItemInstance)
                
            } 
            menuInstance.removeFromMenuItems(menuItemInstance)
            MenuItem newParentMenuItemInstance = MenuItem.get(parentMenuItemId)
            newParentMenuItemInstance.addToChildItems(menuItemInstance)
            newParentSubMenuItemList = newParentMenuItemInstance?.childItems
            newParentSubMenuItemList.add(index,menuItemInstance)
        }
    }
}