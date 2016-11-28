/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.page

import com.causecode.content.ContentService
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import com.causecode.exceptions.RequiredPropertyMissingException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus

import com.causecode.annotation.shorthand.ControllerShorthand
import com.causecode.content.ContentRevision
import com.causecode.content.meta.Meta
import grails.core.GrailsApplication

/**
 * Provides default CRUD end point for Content Manager.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 *
 */
@Secured(['ROLE_CONTENT_MANAGER'])
@ControllerShorthand(value = 'c')
class PageController {

    static allowedMethods = [show: 'GET', save: 'POST', update: 'PUT', delete: 'DELETE']
    static responseFormats = ['json']

    ContentService contentService
    GrailsApplication grailsApplication

    def handleRequiredPropertyMissingException(RequiredPropertyMissingException exception) {
        log.debug 'Page instance not found', exception
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value())
        respond ([message: message(code: 'page.not.found')])

        return
    }

    def index() {
        redirect(action: 'list', params: params)

        return
    }

    def getMetaTypeList() {
        List metaTypeList = Meta.typeList
        respond ([metaTypeList: metaTypeList])

        return
    }

    def list(Integer max) {
        params.sort = params.sort ?: 'dateCreated'
        params.order = params.order ?: 'desc'
        params.max = Math.min(max ?: 10, 100)

        String contentManagerRole = grailsApplication.config.cc.plugins.content.contentMangerRole
        List pageInstanceList = Page.createCriteria().list(params) {
            if (SpringSecurityUtils.ifNotGranted(contentManagerRole)) {
                eq('publish', true)
            }
        }
        respond ([instanceList: pageInstanceList, totalCount: pageInstanceList.totalCount])

        return
    }

    def create(Page pageInstance) {
        respond([pageInstance: pageInstance])

        return
    }

    def save() {
        Map requestData = request.JSON
        Page pageInstance = contentService.create(requestData, requestData.metaList.type,
                requestData.metaList.content, Page)
        if (!pageInstance.save(flush: true)) {
            respond(view: 'create', model: [pageInstance: pageInstance], errors: pageInstance.errors)
            return
        }

        redirect uri: pageInstance.searchLink()

        return
    }

    /**
     * Transactional annotation is required since we are using autowire feature of
     * Grails domain classes so anyone can pass the domain field data which will update
     * the Page instance.
     */
    @Transactional(readOnly = true)
    @Secured(['PERMIT_ALL'])
    def show(Page pageInstance) {
        if (!pageInstance || pageInstance.hasErrors()) {
            throw new RequiredPropertyMissingException()
        }

        /*
         * URL that contains '_escaped_fragment_' parameter, represents a request from a crawler and
         * any change in data model must be updated in the GSP.
         * Render GSP content in JSON format.
         */
        if (params._escaped_fragment_) {
            respond([view: 'show', model: [pageInstance: pageInstance], contentType: 'application/json'])

            return
        }

        // Check if a subject parameter is coming in request if yes, then use that an email subject
        String subject = params.subject
        if (subject && pageInstance.body.contains('mailto:jobs@causecode.com')) {
            pageInstance.body = pageInstance.body.replaceAll('subject=.*?\"', "subject=$subject\"")
        }

        if (request.xhr) {
            respond(pageInstance)
            return
        }

        String pageShowUrl = grailsApplication.config.app.defaultURL + "/page/show/${pageInstance.id}"

        if (subject) {
            pageShowUrl += "?subject=$subject"
        }

        redirect(url: pageShowUrl, permanent: true, params: params)

        return
    }

    def edit(Page pageInstance) {
        if (!pageInstance || pageInstance.hasErrors()) {
            throw new RequiredPropertyMissingException()
        }

        respond([pageInstance: pageInstance, contentRevisionList: ContentRevision.findAllByRevisionOf(pageInstance)])

        return
    }

    def update(Page pageInstance) {
        Page updatePageInstance = pageInstance
        if (!updatePageInstance || updatePageInstance.hasErrors()) {
            throw new RequiredPropertyMissingException()
        }

        Map requestData = request.JSON

        log.info "Parameters received to update page instance: $params, $requestData"
        updatePageInstance = contentService.update(requestData, updatePageInstance, requestData.metaList?.type,
                requestData.metaList?.content)

        if (updatePageInstance.hasErrors()) {
            respond(updatePageInstance.errors)
            return
        }

        if (params.createRevision) {
            contentService.createRevision(updatePageInstance, PageRevision, params)
        }

        redirect uri: updatePageInstance.searchLink()

        return
    }

    def delete(Page pageInstance) {
        if (!pageInstance || pageInstance.hasErrors()) {
            throw new RequiredPropertyMissingException()
        }
        try {
            contentService.delete(pageInstance)
            respond ([success: true])
        } catch (DataIntegrityViolationException e) {
            respond ([success: true])
        }

        return
    }
}
