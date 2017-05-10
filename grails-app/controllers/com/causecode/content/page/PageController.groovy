/*
 * Copyright (c) 2011 - Present, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.page

import com.causecode.RestfulController
import com.causecode.content.ContentService
import com.causecode.content.meta.Meta
import com.causecode.util.NucleusUtils
import grails.converters.JSON
import grails.core.GrailsApplication
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus

/**
 * Provides default CRUD endpoint for Content Manager.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 * @author Hardik Modha
 */
@Secured(['ROLE_CONTENT_MANAGER'])
class PageController extends RestfulController {

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
     * @return List of Page instances
     */
    @Secured(['permitAll'])
    def byTitle() {
        Map responseData = [:]

        if (params.titles) {
            String[] titles = params.titles.split(',')

            titles.each { String title ->

                Page page = Page.findByTitle(title)

                String contentManagerRole = grailsApplication.config.cc.plugins.content.contentManagerRole

                if (page && (SpringSecurityUtils.ifAnyGranted(contentManagerRole) || page.publish)) {
                    responseData.put(title, page)
                }
            }
        }

        respond([instanceList: responseData])
    }

    /**
     * Endpoint to fetch PaginatedList of Page instances based on passed parameters. This endpoint also provides
     * facility to filter and show only published records to normal users. User with contentManagerRole can see
     * unpublished records and filter records by passing query as parameter.
     *
     * @return PaginatedList of matching Page instances
     */
    @Secured(['permitAll'])
    @Override
    def index() {
        log.debug "Params received to get Page are ${params}"

        params.sort = params.sort ?: 'dateCreated'
        params.order = params.order ?: 'desc'

        try {
            params.max = Math.min(params.max?.toInteger() ?: 10, 100)
            params.offset = params.offset?.toInteger() ?: 0
        } catch (NumberFormatException e) {
            params.max = 10
            params.offset = 0
        }

        String contentManagerRole = grailsApplication.config.cc.plugins.content.contentManagerRole

        List pageInstanceList = Page.createCriteria().list(params) {

            if (SpringSecurityUtils.ifAnyGranted(contentManagerRole)) {
                if (params.publish?.toString()) {
                    eq('publish', params.publish)
                }

                if (params.query) {
                    or {
                        ilike('title', "%${params.query}%")
                        ilike('subTitle', "%${params.query}%")
                        ilike('body', "%${params.query}%")
                    }
                }
            } else {
                eq('publish', true)
            }
        }

        int totalCount = pageInstanceList ? pageInstanceList.totalCount : 0

        render(model: [instanceList: pageInstanceList, totalCount: totalCount], view: '/page/index')
    }

    /**
     * This endpoint returns the list of Meta tags useful for SEO.
     *
     * @return list of meta tags.
     */
    @Secured(['permitAll'])
    def getMetaTypeList() {
        respond([metaTypeList: Meta.typeList])
    }

    /**
     * An Endpoint to save the instance of Page.
     *
     * @return Error message when instance cannot be saved, saved instance otherwise.
     */
    @Override
    def save() {
        Map requestData = request.JSON
        Page pageInstance = contentService.create(requestData, requestData.metaList?.type,
                requestData.metaList?.content, Page)

        if (!NucleusUtils.save(pageInstance, true, log)) {
            respondData([message: 'Error occurred while saving the Page.'], [status: HttpStatus.UNPROCESSABLE_ENTITY])

            return false
        }

        render(model: [pageInstance: pageInstance], view: '/page/show')
    }

    /**
     * An endpoint to return the Page instance for show and edit ui pages.
     *
     * @return Page instance
     */
    @Override
    @Secured(['permitAll'])
    @Transactional(readOnly = true)
    def show() {
        Page pageInstance = pageInstanceFromParams

        if (pageInstance) {
            // Check if a subject parameter is coming in request if yes, then use that an email subject
            String subject = params.subject
            if (subject && pageInstance.body.contains('mailto:jobs@causecode.com')) {
                pageInstance.body = pageInstance.body.replaceAll('subject=.*?\"', "subject=$subject\"")
            }

            render(model: [pageInstance: pageInstance], view: '/page/show')
        }
    }

    /**
     * Endpoint to update the Page instance. It updates the instance whose id is sent in the params.
     * If createRevision parameter is passed then it creates a revision for the instance.
     *
     * @return On success updated page instance, error message with appropriate status code otherwise.
     */
    @Override
    def update() {
        params.putAll(request.JSON)

        log.debug "Parameters received to update Page instance ${params}"

        Page pageInstance = pageInstanceFromParams

        if (pageInstance) {
            pageInstance = contentService.update(params, pageInstance, params.metaList?.type, params.metaList?.content)

            if (pageInstance.hasErrors()) {
                respondData([message: 'Error occurred while updating the Page.'],
                        [status: HttpStatus.UNPROCESSABLE_ENTITY])

                return false
            }

            if (params.createRevision) {
                contentService.createRevision(pageInstance, PageRevision, params)
            }

            render(model: [pageInstance: pageInstance], view: '/page/show')
        }
    }

    /**
     * An endpoint to delete an instance of Page.
     *
     * @return Success message when instance is successfully deleted, error message with appropriate status code if an
     * error occurs while deleting the instance.
     */
    @Override
    def delete() {
        log.debug "Parameters received to delete Page instance ${params}"

        Page pageInstance = pageInstanceFromParams

        if (pageInstance) {
            try {
                contentService.delete(pageInstance)

                respondData([message: 'Page deleted successfully.'])
            } catch (DataIntegrityViolationException e) {
                respondData([message: "Cannot delete Page with id ${params.id}."], [status: HttpStatus.NOT_ACCEPTABLE])

                return false
            }
        }
    }

    /**
     * This method validates for id and if id exist then fetches the instance from database and returns it.
     * If id does not exist in params or instance does not exist for provided id then appropriate error message is
     * returned with status code.
     *
     * @return Page instance when instance exists for the provided id, null otherwise.
     */
    private Page getPageInstanceFromParams() {
        if (!params.id) {
            response.setStatus(HttpStatus.NOT_ACCEPTABLE.value())
            render([message: 'Id cannot be null.'] as JSON)

            return
        }

        Page pageInstance = Page.get(params.id)

        if (!pageInstance) {
            response.setStatus(HttpStatus.NOT_FOUND.value())
            render([message: "Page with id ${params.id} does not exist."] as JSON)

            return
        }

        return pageInstance
    }
}
