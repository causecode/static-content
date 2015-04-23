/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.page

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import com.cc.content.meta.Meta
import grails.plugin.springsecurity.SpringSecurityUtils
import org.springframework.dao.DataIntegrityViolationException
import com.cc.annotation.shorthand.ControllerShorthand
import com.cc.content.ContentRevision
import org.springframework.http.HttpStatus
import com.cc.content.format.TextFormat

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
    def springSecurityService
    def textFormatService    

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
        Map result = [instanceList: pageInstanceList, totalCount: pageInstanceList.totalCount]
        render result as JSON
    }

    def save() {
        Map requestData = request.JSON
        println("Data recieved for save"+requestData)
        def textFormatInstance = TextFormat.findById(requestData.textFormat.id)
        // If no legitimate roles exist
        if(SpringSecurityUtils.ifNotGranted(textFormatInstance.roles)) {
            respond ([message: "Sorry! You do not possess privileges to use " + textFormatInstance.name + " format"])
            return
        }
        
        // Format the body we have obtained from the request
        requestData.body = contentService.formatBody(requestData.body,textFormatInstance)
        pageInstance = contentService.create(requestData, requestData.metaList.type, requestData.metaList.value, Page.class)
       
        if(pageInstance.hasErrors()) {
            respond ([errors: pageInstance.errors, message: "Error saving Instance in controller action: " + pageInstance.errors], status: HttpStatus.NOT_MODIFIED)
            return
        }
        redirect uri: pageInstance.searchLink()
        respond ([status: HttpStatus.OK])
    } 
        
    def show(Page pageInstance) {
        respond(pageInstance)
    }

    def edit(Long id) {
        [pageInstance: pageInstance, formatsAvailable: textFormatService.applicableFormats,
            editor:pageInstance?.textFormat?.editor, contentRevisionList: ContentRevision.findAllByRevisionOf(pageInstance)]
    }

    def update(Long version) {
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
        
        pageInstance = contentService.update(requestData, pageInstance, requestData.metaList?.type, 
            requestData.metaList?.value)

        def textFormatInstance = TextFormat.findById(requestData.textFormat.id)
        if(SpringSecurityUtils.ifNotGranted(textFormatInstance.roles)) {
            respond ([message : "Sorry! You do not possess priveleges to use " + textFormatInstance.name + " format"])
            return
        }

        requestData.body = contentService.formatBody(requestData.body,textFormatInstance)

        pageInstance = contentService.update(requestData, pageInstance, requestData.metaList.type, requestData.metaList.value)

        if(pageInstance.hasErrors()) {
            respond (pageInstance.errors)
            return
        }
       
        if(requestData.createRevision) {
            contentService.createRevision(pageInstance, PageRevision.class, requestData)
            respond ([message : "Revision created successfully."])
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