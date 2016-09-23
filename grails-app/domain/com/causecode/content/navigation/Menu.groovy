/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.navigation

import groovy.transform.EqualsAndHashCode

/**
 * Used for rendering menu bar with menu items.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 * @author Bharti Nagdev
 *
 */
@EqualsAndHashCode
class Menu {

    String name
    String roles

    Date dateCreated
    Date lastUpdated

    boolean showOnlyWhenLoggedIn

    List menuItems

    static hasMany = [menuItems: MenuItem]
    static mappedBy = [menuItems: 'menu']

    static constraints = {
        name blank: false
        dateCreated bindable: false
        lastUpdated bindable: false
        roles nullable: true
    }

    static mapping = {
        table 'cc_content_menu'
    }

    @Override
    String toString() {
        "Menu [$name][$id]"
    }
}
