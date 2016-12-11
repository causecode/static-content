/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.meta

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Used for storing meta tags for contents.
 * @author Shashank Agrawal
 */
@ToString(includes = ['id', 'type'], includePackage = false)
@EqualsAndHashCode
class Meta {

    String type
    String content

    Date dateCreated
    Date lastUpdated

    static constraints = {
        type blank: false
        content blank: false
        dateCreated bindable: false
        lastUpdated bindable: false
    }

    static mapping = {
        table 'cc_content_meta'
    }

    static List<String> getTypeList() {
        return ['keywords', 'description', 'og:description', 'og:title', 'og:image', 'og:url']
    }
}
