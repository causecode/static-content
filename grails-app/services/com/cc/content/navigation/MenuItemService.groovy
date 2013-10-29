/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.navigation

class MenuItemService {

    MenuItem create(Map args) {
        def menuItemInstance = new MenuItem()
        update(menuItemInstance , args)
        return menuItemInstance
    }

    MenuItem update(MenuItem menuItemInstance , Map args = [:]) {
        menuItemInstance.properties = args

        if(!menuItemInstance.id) {
            int index = args.index as int
            Menu menuInstance = Menu.get(args.menuId)
            menuInstance.addToMenuItems(menuItemInstance)
            menuInstance.save(flush: true)
            reorder(menuItemInstance, args)
        }

        return menuItemInstance
    }

    void delete(MenuItem menuItemInstance, boolean flush = true) {
        Menu menuInstance = menuItemInstance.menu
        menuInstance.removeFromMenuItems(menuItemInstance)
        menuInstance.save()
        if(menuItemInstance.childItems) {
            List childItems = menuItemInstance.childItems.toArray()
            childItems.each { delete(it, false) }
        }
        if(menuItemInstance.parent) {
            MenuItem parentMenuItemInstance = menuItemInstance.parent
            parentMenuItemInstance.removeFromChildItems(menuItemInstance)
            parentMenuItemInstance.save(flush: flush)
        }
        menuItemInstance.delete(flush: flush)
    }

    void reorder(MenuItem menuItemInstance, Map args) {
        int index = args.index as int
        def parentId = args.parentId

        Menu menuInstance = menuItemInstance.menu

        log.info "Sorting $menuItemInstance at ${parentId ? 'non ' : ''}top level."

        // Removing from parent menu item (if exist) in every case
        if(menuItemInstance.parent) {
            MenuItem parentMenuItemInstance = menuItemInstance.parent
            parentMenuItemInstance.removeFromChildItems(menuItemInstance)
            parentMenuItemInstance.save()
            log.info "Removing $menuItemInstance from parent $parentMenuItemInstance"
        }

        // When dropping a menu item to top level menu item
        if(!parentId) {
            fixOrder(menuInstance, menuItemInstance, index)
            return
        }
        MenuItem newParentMenuItemInstance = MenuItem.get(parentId)
        log.info "Adding $menuItemInstance to parent $newParentMenuItemInstance at position [$index]"

        newParentMenuItemInstance.childItems.add(index, menuItemInstance)
        newParentMenuItemInstance.save()
        fixOrder(menuInstance)
    }

    void fixOrder(Menu menuInstance, MenuItem menuItemInstance = null, int index = 0) {
        if(!menuInstance.menuItems) {
            log.info "No menuitems in $menuInstance to sort."
            return
        }

        List<MenuItem> allMenuItems = menuInstance.menuItems.toArray().findAll { it } //MenuItem.findAllByMenu(menuInstance)
        List<MenuItem> nonTopLevelMenuItems = allMenuItems.findAll { it.parent }
        List<MenuItem> topLevelMenuItems = allMenuItems.findAll { !it.parent }

        if(menuItemInstance) {
            log.info "Adding $menuItemInstance to $menuInstance at position [$index]."
            topLevelMenuItems.remove(menuItemInstance)
            topLevelMenuItems.add(index, menuItemInstance)
        }

        menuInstance.menuItems.clear()
        menuInstance.menuItems.addAll(topLevelMenuItems + nonTopLevelMenuItems)
        menuInstance.save()
    }

}