package com.causecode.content

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Used to support old content reference if required.
 * @author Shashank Agrawal
 *
 */
@ToString(includes = ['id', 'title'], includePackage = false)
@EqualsAndHashCode
class ContentRevision {

    String title
    String subTitle
    String body

    String comment

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
        table 'cc_content_content_revision'
    }
}
