/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured("ROLE_CONTENT_MANAGER")
class ContentRevisionController {

    static defaultAction = "show"

    private ContentRevision contentRevisionInstance

    def show(Long id) {
        [contentRevisionInstance: ContentRevision.get(id)]
    }

    def load(Long id) {
        contentRevisionInstance = ContentRevision.get(id)
        render ([title: contentRevisionInstance.title, subTitle: contentRevisionInstance.subTitle, body:
            contentRevisionInstance.body] as JSON)
        return
    }

    def delete(Long id) {
        ContentRevision.get(id).delete()
        render true
    }

}
