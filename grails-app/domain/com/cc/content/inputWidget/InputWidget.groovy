/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.inputWidget

class InputWidget {

    String name
    String label
    String widgetKey
    String widgetValue
    String defaultValue
    String helpText
    String helpType
    String validate
    Date dateCreated
    Date lastUpdated

    static constraints = {
        name blank: false
        label blank: false
        validate blank: false
        widgetKey nullable: true
        widgetValue nullable: true
        dateCreated bindable: false
        lastUpdated bindable: false
    }

}