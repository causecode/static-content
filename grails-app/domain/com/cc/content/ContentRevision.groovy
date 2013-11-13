/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

class ContentRevision {

    String title
    String subTitle
    String body

    Content revisionOf

    Date dateCreated
    Date lastUpdated

    static constraints = {
        //importFrom Content
        body blank: false
        title blank: false
        subTitle nullable: true
        dateCreated bindable: false
        lastUpdated bindable: false
    }

    static mapping = {
        body type: 'text'
        table "cc_content_content_revision"
    }

}