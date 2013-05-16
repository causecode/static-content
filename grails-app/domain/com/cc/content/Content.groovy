/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

import com.cc.content.meta.Meta

class Content {

    transient contentService
    def grailsApplication

    Date dateCreated
    Date lastUpdated

    String title
    String subTitle
    String body
    String author

    boolean publish

    static mapping = {
        body type: 'text'
    }

    static constraints = {
        body blank: false
        title nullable: false, blank: false
        subTitle nullable: true
        author nullable: true, bindable: false
        dateCreated bindable: false
        lastUpdated bindable: false
    }

    @Override
    String toString() {
        title
    }

    String resolveAuthor() {
        return contentService.resolveAuthor(this)
    }

    List<Meta> getMetaTags() {
        ContentMeta.findAllByContent(this)*.meta
    }

    //    def beforeInsert() {
    //        author = resolveAuthor()
    //    }

}