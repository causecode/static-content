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
     * Used to render menu bars.
     * @attr id REQUIRED The identifier of the Menu domain for which menu bar should be rendered.
     */
    def menu = { attrs, body ->
        Menu menuInstance = Menu.get(attrs.id)
        if(!menuInstance) {
            log.info "No menu found with id [$attrs.id]."
            return
        }
        String template = attrs.menuTemplate ?: "/menu/templates/menu"
        String plugin = attrs.inPlugin ?: "content"

        List<MenuItem> menuItemList = menuInstance.menuItems

        out << render(template: template, plugin: plugin, model: [menuInstance: menuInstance,
            menuItemList: menuItemList].plus(attrs))
    }

    /**
     * Used to validate User Logged in/not and User Roles.
     * @attr instance REQUIRED The instance of the Menu domain/MenuItem domain for which validation is performed.
     */
    def canBeVisible = { attrs, body ->
        def instance = attrs.instance
        if(!instance) {
            return
        }
        if(instance.roles) {
            if(SpringSecurityUtils.ifAnyGranted(instance.roles)) {
                out << body()
            }
            return
        } else {
            if(!instance.showOnlyWhenLoggedIn) {
                out << body()
            } else if(springSecurityService.isLoggedIn()) {
                out << body()
            }
        }
    }

}