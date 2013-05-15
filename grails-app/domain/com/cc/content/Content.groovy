/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

class Content {

    Date dateCreated
    Date lastUpdated

    String title
    String subTitle
    String body
    String author

    static mapping = {
        body type: 'text'
    }

    static constraints = {
        body size: 0..5000, blank: false
        title nullable: false, blank: false
        subTitle nullable: true
        author nullable: true
        dateCreated bindable: false
        lastUpdated bindable: false
    }

}