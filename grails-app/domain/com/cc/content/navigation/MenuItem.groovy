/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.navigation

import java.util.Date;

class MenuItem {

    String title
    String url

    Date dateCreated
    Date lastUpdated

    boolean showOnlyWhenLoggedIn 
    String roles

    static belongsTo = [parent: MenuItem, menu: Menu]
    static hasMany = [childItems: MenuItem]

    static constraints = {
        menu nullable: true
        parent nullable: true
        dateCreated bindable: false
        lastUpdated bindable: false
    }

    static mapping = {
        table "cc_content_menu_item"
    }

}