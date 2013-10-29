/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.navigation

class MenuItem {

    String roles
    String title
    String url

    Date dateCreated
    Date lastUpdated

    boolean showOnlyWhenLoggedIn

    List childItems

    MenuItem parent
    Menu menu

    static hasMany = [childItems: MenuItem]
    static mappedBy = [childItems: "parent"]

    static constraints = {
        roles nullable: true
        dateCreated bindable: false
        lastUpdated bindable: false
    }

    static mapping = {
        table "cc_content_menu_item"
    }

    @Override
    String toString() {
        "MenuItem [$title][$id]"
    }

}