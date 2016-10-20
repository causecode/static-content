/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content

import com.causecode.content.meta.Meta
import groovy.transform.EqualsAndHashCode

/**
 * Join class provides meta tags for content.
 * @author Shashank Agrawal
 *
 */
@EqualsAndHashCode
class ContentMeta {

    Content content
    Meta meta

    Date dateCreated
    Date lastUpdated

    static constraints = {
        dateCreated bindable: false
        lastUpdated bindable: false
    }

    static mapping = {
        version false
        meta cascade: 'delete'
        table 'cc_content_content_meta'
    }

    @Override
    String toString() {
        return "ContentMeta ($content)($meta)"
    }
}
