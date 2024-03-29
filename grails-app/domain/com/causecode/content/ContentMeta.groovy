package com.causecode.content

import com.causecode.content.meta.Meta
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Join class provides meta tags for content.
 * @author Shashank Agrawal
 *
 */
@ToString(includes = ['content', 'meta'], includePackage = false)
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
}
