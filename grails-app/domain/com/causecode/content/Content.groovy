/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.causecode.content

import com.causecode.annotation.sanitizedTitle.SanitizedTitle
import com.causecode.content.meta.Meta

/**
 * A generic domain used to store generic fields for storing any content.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 * @author Bharti Nagdev
 * @author Sakshi Gangarde
 * 
 */
class Content {

    transient contentService
    transient friendlyUrlService

    Date dateCreated
    Date lastUpdated
    Date publishedDate

    @SanitizedTitle
    String title
    String subTitle
    String body
    String author

    boolean publish

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
        publishedDate nullable: true, bindable: false
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

    def beforeInsert() {
        if(publish) {
            publishedDate = new Date()
        }
    }

    def beforeUpdate() {
        if(!this.getPersistentValue("publish") && publish) {
            publishedDate = new Date()
        }
    }

    String searchLink(boolean absolute = false) {
        contentService.createLink([domain: this.class.name, absolute: absolute,
            controller: this.class.simpleName.toLowerCase(), id: this.id])
    }


}