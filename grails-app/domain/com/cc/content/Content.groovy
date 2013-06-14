/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

import com.cc.annotation.sanitizedTitle.SanitizedTitle
import com.cc.content.meta.Meta

class Content {

    transient contentService
    def grailsApplication

    Date dateCreated
    Date lastUpdated

    @SanitizedTitle
    String title
    String subTitle
    String body
    String author

    boolean publish

    TextFormat textFormat
    
    static mapping = {
        body type: 'text'
        table "cc_content_content"
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
        if(!this.id) return [];

        ContentMeta.findAllByContent(this)*.meta
    }

    //    def beforeInsert() {
    //        author = resolveAuthor()
    //    }

    //    def beforeDelete() {
    //            List contentMetaList = ContentMeta.findAllByContent(this)
    //            List metaList = contentMetaList*.meta
    //            contentMetaList*.delete()
    //            metaList*.delete()
    //    }

}