/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.navigation

import java.util.Date;

class MenuItem {

    String title
    String url

    Date dateCreated
    Date lastUpdated

    static belongsTo = [parent: MenuItem, menu: Menu]
    static hasMany = [childItems: MenuItem]

    static constraints = {
        parent nullable: true
        menu nullable: true
        dateCreated bindable: false
        lastUpdated bindable: false
    }

}