/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.navigation

class Menu {

    String name
    static hasMany = [menuItems: MenuItem]

    static constraints = {
        name blank: false
    }

}