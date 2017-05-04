/*
 * Copyright (c) 2011 - Present, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.page

import com.causecode.RestfulController
import com.causecode.content.Content
import com.causecode.content.ContentService
import com.causecode.content.meta.Meta
import com.causecode.util.NucleusUtils
import grails.core.GrailsApplication
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus

/**
 * Provides default CRUD endpoint for Content Manager.
 * @author Hardik Modha
 */
@Secured(['ROLE_CONTENT_MANAGER'])
class PageController extends RestfulController {

    static responseFormats = ['json']
    static namespace = 'v1'

    ContentService contentService
    GrailsApplication grailsApplication

    PageController() {
        super(Page)
    }

    /**
     * This endpoint returns Page instance for the passed title name.
     * @param params - comma separated string of titles
     *
     * @return
     */
    @Secured(['permitAll'])
    def fetchPageByTitle() {
        Map responseData = [:]

        if (params.titles) {
            String [] titles = params.titles.split(',')

            titles.each { String title ->

                Page page = Page.findByTitle(title)
                if (page) {
                    responseData.put(title, page)
                }
            }
        }

        respond([instanceList: responseData])
    }

    @Secured(['permitAll'])
    @Override
    def index() {
        log.debug "Params received to get Page are ${params}"

        params.sort = params.sort ?: 'dateCreated'
        params.order = params.order ?: 'desc'
        params.max = Math.min(params.max ?: 10, 100)

        String contentManagerRole = grailsApplication.config.cc.plugins.content.contentManagerRole

        List pageInstanceList = Page.createCriteria().list(params) {
            if (SpringSecurityUtils.ifNotGranted(contentManagerRole)) {
                eq('publish', true)
            }
        }

        render(model: [instanceList: pageInstanceList], view: '/page/index')
    }

    @Secured(['permitAll'])
    def getMetaTypeList() {
        respond([metaTypeList: Meta.typeList])
    }

    @Override
    def save() {
        Map requestData = request.JSON
        Page pageInstance = contentService.create(requestData, requestData.metaList?.type,
                requestData.metaList?.content, Page)

        if (!NucleusUtils.save(pageInstance, true, log)) {
            respondData([message: "Cannot save instance due to errors: ${pageInstance.errors}"],
                    [status: HttpStatus.UNPROCESSABLE_ENTITY])

            return
        }

        render(model: [pageInstance: pageInstance], view: '/page/show')
    }

    @Override
    @Secured(['permitAll'])
    @Transactional(readOnly = true)
    def show() {
        if (!params.id) {
            respondData([message: 'Id cannot be null.'], [status: HttpStatus.NOT_ACCEPTABLE])

            return;
        }

        Page pageInstance = Page.get(params.id)

        if (!pageInstance) {
            respondData([message: "Page with id ${params.id} does not exist."], [status: HttpStatus.NOT_FOUND])
        }

        // Check if a subject parameter is coming in request if yes, then use that an email subject
        String subject = params.subject
        if (subject && pageInstance.body.contains('mailto:jobs@causecode.com')) {
            pageInstance.body = pageInstance.body.replaceAll('subject=.*?\"', "subject=$subject\"")
        }

        render(model: [pageInstance: pageInstance], view: '/page/show')
    }

    @Override
    def update() {
        params.putAll(request.JSON)

        log.debug "Parameters received to update Page instance ${params}"

        Page pageInstance = Page.get(params.id)

        if (!pageInstance) {
            respondData([message: "Page with id ${params.id} does not exist."], [status: HttpStatus.NOT_FOUND])
        }

        pageInstance = contentService.update(params, pageInstance, params.metaList?.type, params.metaList?.content)

        if (pageInstance.hasErrors()) {
            respond(pageInstance.errors)

            return
        }

        if (params.createRevision) {
            contentService.createRevision(pageInstance, PageRevision, params)
        }

        render(model: [pageInstance: pageInstance], view: '/page/show')
    }
    
    @Override
    def delete() {
        log.debug "Parameters received to delete Page instance ${params}"

        Page pageInstance = Page.get(params.id)

        if (!pageInstance) {
            respondData([message: "Page with id ${params.id} does not exist."])
        }

        try {
            contentService.delete(pageInstance)

            respondData([message: 'Page deleted successfully.'])
        } catch (DataIntegrityViolationException e) {
            respondData([message: "Cannot delete Page with id ${params.id}"], [status: HttpStatus.NOT_ACCEPTABLE])
        }
    }
}
