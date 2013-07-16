/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.navigation

class Menu {

    String name

    Date dateCreated
    Date lastUpdated

    List menuItems

    static hasMany = [menuItems: MenuItem]

    static constraints = {
        name blank: false
        dateCreated bindable: false
        lastUpdated bindable: false
    }

    static mapping = {
        table "cc_content_menu"
    }

}