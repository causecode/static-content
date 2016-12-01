/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.meta

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.dao.DataIntegrityViolationException
import com.causecode.content.ContentMeta
import org.springframework.http.HttpStatus

/**
 * Provides end point to delete any meta information of content for Content Manager.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 *
 */
@Secured(['ROLE_CONTENT_MANAGER'])
class MetaController {

    def beforeInterceptor = [action: this.&validate]
    static responseFormats = ['json']

    private Meta metaInstance

    private validate() {
        if (!params.id) {
            return true
        }

        metaInstance = Meta.get(params.id)
        if (!metaInstance) {
            if (request.xhr) {
                render status: HttpStatus.NOT_FOUND
            } else {
                redirect(action: 'list')
            }
            return false
        }

        return true
    }

    /**
     * Delete Meta instance and all its references and respond success: true.
     * If Meta ID not specified then status NOT FOUND will be rendered.
     * This action also delete join class reference added for ContentMeta for given meta instance.
     * @param id Identity of Meta domain instance to be deleted.
     * @throws DataIntegrityViolationException
     */
    def deleteMeta(Long id) {
        if (!id) {
            render status: HttpStatus.NOT_FOUND
            return
        }

        try {
            ContentMeta.findAllByMeta(metaInstance)*.delete([flush: true])
            metaInstance.delete([flush: true])
            respond([success: true])
        } catch (DataIntegrityViolationException e) {
            response.status = 500
            render status: HttpStatus.UNPROCESSABLE_ENTITY
        }
    }
}
