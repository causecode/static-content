/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.navigation

/**
 * This Service used to perform CRUD operation for menu items and also updates order of menu Items.
 * @author Laxmi Salunkhe
 * @author Shashank Agrawal
 */
class MenuItemService {

    /**
     * Used to create menuItem instance with given parameters.
     * @param args REQUIRED Map containing parameters required to create menuItem instance.
     * @return Newly created MenuItem Instance.
     */
    MenuItem create(Map args) {
        def menuItemInstance = new MenuItem()
        update(menuItemInstance , args)
        return menuItemInstance
    }

    /**
     * Used to update menuItem instance with given new parameters.
     * If menuItem instance is newly created, this method add menuItem to menu and reorder it.
     * @param menuItemInstance REQUIRED MenuItem Instance to be updated.
     * @param args Map containing parameters required to create menuItem instance.
     * @return Updated MenuItem Instance.
     */
    MenuItem update(MenuItem menuItemInstance , Map args = [:]) {
        menuItemInstance.properties = args

        if (!menuItemInstance.id) {
            //int index = args.index as int
            Menu menuInstance = Menu.get(args.menuId)
            menuInstance.addToMenuItems(menuItemInstance)
            menuInstance.save(flush: true)
            reorder(menuItemInstance, args)
        }

        return menuItemInstance
    }

    /**
     * Used to delete menuItem instance.
     * This method removes menuItem from menu and also deletes all its child
     * menuItems.
     * If menuItem to be removed is child of MenuItem then it removes menuItem from its parent menuItem.
     * After removing all references it deletes menuItem instance.
     * @param menuItemInstance
     */
    void delete(MenuItem menuItemInstance, boolean flush = true) {
        Menu menuInstance = menuItemInstance.menu
        menuInstance.removeFromMenuItems(menuItemInstance)
        menuInstance.save()
        if (menuItemInstance.childItems) {
            List childItems = menuItemInstance.childItems.toArray()
            childItems.each { delete(it, false) }
        }
        if (menuItemInstance.parent) {
            MenuItem parentMenuItemInstance = menuItemInstance.parent
            parentMenuItemInstance.removeFromChildItems(menuItemInstance)
            parentMenuItemInstance.save(flush: flush)
        }
        menuItemInstance.delete(flush: flush)
    }

    /**
     * Used to reorder menuItems inside menu.
     * @param menuItemInstance REQUIRED Menu Item Instance to be reordered within other menuItems.
     * @param args REQUIRED Map containing parameters for parentId and index where menuItem is placed after reordering.
     */
    void reorder(MenuItem menuItemInstance, Map args) {
        int index = args.index as int
        def parentId = args.parentId

        Menu menuInstance = menuItemInstance.menu

        log.info "Sorting $menuItemInstance at ${parentId ? 'non ' : ''}top level."

        // Removing from parent menu item (if exist) in every case
        if (menuItemInstance.parent) {
            MenuItem parentMenuItemInstance = menuItemInstance.parent
            parentMenuItemInstance.removeFromChildItems(menuItemInstance)
            parentMenuItemInstance.save()
            log.info "Removing $menuItemInstance from parent $parentMenuItemInstance"
        }

        // When dropping a menu item to top level menu item
        if (!parentId) {
            fixOrder(menuInstance, menuItemInstance, index)
            return
        }
        MenuItem newParentMenuItemInstance = MenuItem.get(parentId)
        log.info "Adding $menuItemInstance to parent $newParentMenuItemInstance at position [$index]"

        newParentMenuItemInstance.childItems.add(index, menuItemInstance)
        newParentMenuItemInstance.save()
        fixOrder(menuInstance)
    }

    /**
     * Used to fix order of menuItems  after reordering.
     * @param menuInstance REQUIRED Menu Instance whose menuItems order need to be fixed.
     * @param menuItemInstance MenuItem Instance whose order is changed and will get fixed.
     * @param index New position of menuItem instance.
     */
    void fixOrder(Menu menuInstance, MenuItem menuItemInstance = null, int index = 0) {
        if (!menuInstance.menuItems) {
            log.info "No menuitems in $menuInstance to sort."
            return
        }

        //MenuItem.findAllByMenu(menuInstance)
        List<MenuItem> allMenuItems = menuInstance.menuItems.toArray().findAll { it }
        List<MenuItem> nonTopLevelMenuItems = allMenuItems.findAll { it.parent }
        List<MenuItem> topLevelMenuItems = allMenuItems.findAll { !it.parent }

        if (menuItemInstance) {
            log.info "Adding $menuItemInstance to $menuInstance at position [$index]."
            topLevelMenuItems.remove(menuItemInstance)
            topLevelMenuItems.add(index, menuItemInstance)
        }

        menuInstance.menuItems.clear()
        menuInstance.menuItems.addAll(topLevelMenuItems + nonTopLevelMenuItems)
        menuInstance.save()
    }
}
