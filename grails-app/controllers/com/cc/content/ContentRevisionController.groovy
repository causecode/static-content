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

/**
 * Provides end point to show, load and delete content revision for Content Manager.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 */
@Secured("permitAll")
class ContentRevisionController {

    static defaultAction = "show"

    private ContentRevision contentRevisionInstance

    def show(Long id) {
        [contentRevisionInstance: ContentRevision.get(id)]
    }

    /**
     * Used to load COntent Revision instance by ID.
     * @param id REQUIRED Identity of ContentRevision domain instance to be loaded.
     * @return Renders content revision instance data in JSON format.
     */
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
