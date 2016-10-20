/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content

import com.causecode.springsecurity.Annotations
import com.causecode.utility.UtilParameters
import grails.plugin.springsecurity.annotation.Secured

/**
 * Provides end point to show, load and delete content revision for Content Manager.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 */
@Secured(Annotations.ROLE_CONTENT_MANAGER)
class ContentRevisionController {

    static defaultAction = 'show'
    static responseFormats = ['json']

    private ContentRevision contentRevisionInstance

    def show(Long id) {
        respond(ContentRevision.get(id))

        return
    }

    /**
     * Used to load Content Revision instance by ID.
     * @param id REQUIRED Identity of ContentRevision domain instance to be loaded.
     * @return Renders content revision instance data in JSON format.
     */
    def load(Long id) {
        contentRevisionInstance = ContentRevision.get(id)
        respond([title: contentRevisionInstance.title, subTitle: contentRevisionInstance.subTitle, body:
            contentRevisionInstance.body])

        return
    }

    def delete(Long id) {
        ContentRevision.get(id).delete(flush: true)
        respond(UtilParameters.SUCCESS_TRUE)

        return
    }
}
