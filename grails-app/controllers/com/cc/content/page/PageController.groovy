/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.page

import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.plugin.springsecurity.annotation.Secured
import com.cc.content.meta.Meta
import grails.plugin.springsecurity.SpringSecurityUtils

import org.springframework.dao.DataIntegrityViolationException
import com.cc.annotation.shorthand.ControllerShorthand
import com.cc.content.ContentRevision

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

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    static responseFormats = ["json"]

    def beforeInterceptor = [action: this.&validate]
    def contentService
    PageRenderer groovyPageRenderer

    private Page pageInstance

    private validate() {
        if(!params.id) return true;

        pageInstance = Page.get(params.id)
        if(!pageInstance) {
            flash.message = g.message(code: 'default.not.found.message', args: [message(code: 'page.label'), params.id])
            redirect(action: "list")
            return false
        }
        return true
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

        String contentManagerRole = grailsApplication.config.cc.plugins.content.contentMangerRole
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
        pageInstance = contentService.create(requestData, requestData.metaList.type, requestData.metaList.value, Page.class)
        if(!pageInstance.save(flush: true)) {
            render(view: "create", model: [pageInstance: pageInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'page.label'), pageInstance.id])
        redirect uri: pageInstance.searchLink()
    }

    @Secured(["permitAll"])
    def show(Page pageInstance) {
        // URL that contains '_escaped_fragment_' parameter, represents a request from a crawler.
        if (params._escaped_fragment_) {
            render groovyPageRenderer.render(view: "/page/show", model: [pageInstance: pageInstance])
            return      
        }
        if (request.xhr) {
            render text:([pageInstance: pageInstance] as JSON)
            return
        }
        String pageShowUrl = grailsApplication.config.app.defaultURL + "/page/show/${id}"
        redirect(url: pageShowUrl, permanent: true)
    }

    def edit(Page pageInstance) {
        [pageInstance: pageInstance, contentRevisionList: ContentRevision.findAllByRevisionOf(pageInstance)]
    }

    def update(Page pageInstance, Long version) {
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
        try {
            contentService.delete(pageInstance)
            respond ([success: true])
        } catch (DataIntegrityViolationException e) {
            respond ([success: false])
        }
    }
}
