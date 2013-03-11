/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */


package com.cc.navigation

class MenuItem {
    String title
    String url
    static belongsTo = [parent: MenuItem, menu: Menu]
    static hasMany = [childItems: MenuItem]
    
    static constraints = {
        url url: true
        parent nullable: true
        menu nullable: true
    }
}
