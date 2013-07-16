/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.navigation

import com.cc.content.navigation.Menu;
import com.cc.content.navigation.MenuItem;

class MenuTagLib {

    static namespace = "com"

    /**
     * Used to render menubars.
     * @attr id REQUIRED The identifier of the Menu domain for which menu bar should be rendered.
     */
    def bootstrapMenu = { attrs, body ->
        if(Menu.get(attrs.id)) {
            def menuInstance = Menu.get(attrs.id)
            def menuItemList = MenuItem.findAllByMenu(menuInstance)
            out << render(template: '/menu/bootstrapMenu', plugin: 'content', model: ['menuInstance': menuInstance,
                'menuItemList': menuItemList].plus(attrs))
        }
    }

    def menu = { attrs, body ->
        def menuItemInstance = MenuItem.get(attrs.id)
        out << render(template: '/menu/menu', plugin: 'content', 
                         model: ['menuItemInstance': menuItemInstance , renderingSubMenu: attrs.renderingSubMenu])
    }

}
