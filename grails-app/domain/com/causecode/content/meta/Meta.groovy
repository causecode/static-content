/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.meta

import groovy.transform.EqualsAndHashCode

/**
 * Used for storing meta tags for contents.
 * @author Shashank Agrawal
 */
@EqualsAndHashCode
class Meta {

    public static final String KEYWORDS = 'keywords'
    public static final String DESCRIPTION = 'description'
    public static final String OG_DESCRIPTION = 'og:description'
    public static final String OG_TITLE = 'og:title'
    public static final String OG_IMAGE = 'og:image'
    public static final String OG_URL = 'og:url'

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
        return [KEYWORDS, DESCRIPTION, OG_DESCRIPTION, OG_TITLE, OG_IMAGE, OG_URL]
    }

    @Override
    String toString() {
        return "Meta ($type)"
    }
}
