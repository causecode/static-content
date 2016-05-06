/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.causecode.content.page

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import com.causecode.exceptions.RequiredPropertyMissingException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus

import com.causecode.annotation.shorthand.ControllerShorthand
import com.causecode.content.ContentRevision
import com.causecode.content.meta.Meta

/**
 * Provides default CRUD end point for Content Manager.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 *
 */
@Secured(["ROLE_CONTENT_MANAGER"])
@ControllerShorthand(value = "c")
class PageController {

    static allowedMethods = [show: "GET", save: "POST", update: "PUT", delete: "DELETE"]
    static responseFormats = ["json"]

    def contentService

    def handleRequiredPropertyMissingException(RequiredPropertyMissingException exception) {
        log.debug "Page instance not found"
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value())
        respond ([message: message(code: "page.not.found")])
        return
    }

    def index() {
        redirect(action: "list", params: params)
    }

    def getMetaTypeList() {
        List metaTypeList = Meta.getTypeList()
        respond ([metaTypeList:metaTypeList])
    }

    def list(Integer max) {
        params.sort = "dateCreated"
        params.order = "desc"
        params.max = Math.min(max ?: 10, 100)

        String contentManagerRole = grailsApplication.config.causecode.plugins.content.contentMangerRole
        List pageInstanceList = Page.createCriteria().list(params) {
            if(SpringSecurityUtils.ifNotGranted(contentManagerRole))
                eq("publish", true)
        }
        respond ([instanceList: pageInstanceList, totalCount: pageInstanceList.totalCount])
    }

    def create() {
        [pageInstance: new Page(params)]
    }

    def save() {
        Map requestData = request.JSON
        Page pageInstance = contentService.create(requestData, requestData.metaList.type, requestData.metaList.value, Page.class)
        if(!pageInstance.save(flush: true)) {
            render(view: "create", model: [pageInstance: pageInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'page.label'), pageInstance.id])
        redirect uri: pageInstance.searchLink()
    }

    /**
     * Transactional annotation is required since we are using autowire feature of 
     * Grails domain classes so anyone can pass the domain field data which will update
     * the Page instance.
     */
    @Transactional(readOnly = true)
    @Secured(["permitAll"])
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
            render (view: "show", model: [pageInstance: pageInstance], contentType: "application/json")
            return
        }

        if (request.xhr) {
            respond(pageInstance)
            return
        }

        String pageShowUrl = grailsApplication.config.app.defaultURL + "/page/show/${pageInstance.id}"
        redirect(url: pageShowUrl, permanent: true)
    }

    def edit(Page pageInstance) {
        if (!pageInstance || pageInstance.hasErrors()) {
            throw new RequiredPropertyMissingException()
        }

        [pageInstance: pageInstance, contentRevisionList: ContentRevision.findAllByRevisionOf(pageInstance)]
    }

    def update(Page pageInstance, Long version) {
        if (!pageInstance || pageInstance.hasErrors()) {
            throw new RequiredPropertyMissingException()
        }
        Map requestData = request.JSON

        if(version != null) {
            if (pageInstance.version > version) {
                pageInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'page.label')] as Object[],
                        "Another user has updated this Page while you were editing")
                respond(pageInstance.errors)
                return
            }
        }
        log.info "Parameters received to update page instance: $params, $requestData"
        pageInstance = contentService.update(requestData, pageInstance, requestData.metaList?.type,
                requestData.metaList?.value)

        if(pageInstance.hasErrors()) {
            respond(pageInstance.errors)
            return
        }
        flash.message = "<em>$pageInstance</em> Page updated successfully."
        if(params.createRevision) {
            contentService.createRevision(pageInstance, PageRevision.class, params)
            flash.message += " Revision created successfully."
        }

        redirect uri: pageInstance.searchLink()
    }

    def delete(Page pageInstance) {
        if (!pageInstance || pageInstance.hasErrors()) {
            throw new RequiredPropertyMissingException()
        }
        try {
            contentService.delete(pageInstance)
            respond ([success: true])
        } catch (DataIntegrityViolationException e) {
            respond ([success: false])
        }
    }
}