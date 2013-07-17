/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.navigation

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

class MenuTagLib {

    static namespace = "com"

    def springSecurityService

    /**
     * Used to render menubars.
     * @attr id REQUIRED The identifier of the Menu domain for which menu bar should be rendered.
     */
    def menu = { attrs, body ->
        if(Menu.get(attrs.id)) {
            def menuInstance = Menu.get(attrs.id)
            def menuItemList = MenuItem.findAllByMenu(menuInstance)
            out << render(template: '/menu/menu', plugin: 'content', model: ['menuInstance': menuInstance,
                'menuItemList': menuItemList].plus(attrs))
        }
    }

    def menuItem = { attrs, body ->
        def menuItemInstance = MenuItem.get(attrs.id)
        out << render(template: '/menuItem/menuItem', plugin: 'content',
        model: ['menuItemInstance': menuItemInstance , renderingSubMenu: attrs.renderingSubMenu])
    }

    /**
     * Used to validate User Logged in/not and User Roles.
     * @attr instance REQUIRED The instance of the Menu domain/MenuItem domain for which validation is performed.
     */
    def canBeVisible = { attrs, body ->
        def instance = attrs.instance
        if(!instance?.showOnlyWhenLoggedIn) {
            out << body()
        } else if(springSecurityService.isLoggedIn()) {
            if(instance?.roles && SpringSecurityUtils.ifAnyGranted(instance.roles)) {
                out << body()
            }
        }
    }

}