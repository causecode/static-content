/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

import com.cc.annotation.sanitizedTitle.SanitizedTitle
import com.cc.content.format.TextFormat;
import com.cc.content.meta.Meta

class Content {

    transient contentService
    transient friendlyUrlService
    transient grailsApplication

    Date dateCreated
    Date lastUpdated

    @SanitizedTitle
    String title
    String subTitle
    String body
    String author

    boolean publish

    TextFormat textFormat
    boolean editor
    
    static mapping = {
        body type: 'text'
        table "cc_content_content"
    }

    static constraints = {  // Any modification here -> confirm in ContentRevision domain
        body blank: false
        title blank: false
        subTitle nullable: true
        author nullable: true, bindable: false
        dateCreated bindable: false
        lastUpdated bindable: false
        textFormat nullable:true 
    }

    @Override
    String toString() {
        title
    }

    String resolveAuthor(String authorProperty = "username") {
        return contentService.resolveAuthor(this, authorProperty)
    }

    String getSanitizedTitle() {
        friendlyUrlService.sanitizeWithDashes(title)
    }

    List<Meta> getMetaTags() {
        if(!this.id) return [];

        ContentMeta.findAllByContent(this)*.meta
    }

}