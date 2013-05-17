/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.meta

class Meta {

    public static final String KEYWORDS = "keywords"
    public static final String DESCRIPTION = "description"

    String type
    String value

    Date dateCreated
    Date lastUpdated

    static constraints = {
        type blank: false
        value blank: false
        dateCreated bindable: false
        lastUpdated bindable: false
    }

    static mapping = {
        table "cc_content_meta"
    }

    static List<String> getTypeList() {
        return [KEYWORDS, DESCRIPTION]
    }

}