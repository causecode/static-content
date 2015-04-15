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
    def textFormatService       //instance of Service : TextFormats

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
        if(!params.editor) {
            params.editor = true
        }
        if(!pageInstance) {
            pageInstance = new Page(params)
        }
        [pageInstance: pageInstance, formatsAvailable: textFormatService.applicableFormats,
            editor:params.boolean('editor')]
    }

    def save() {
        Map requestData = request.JSON
        pageInstance = contentService.create(requestData, requestData.metaList.type, requestData.metaList.value, Page.class)
        if(!pageInstance.save(flush: true)) {
        def textFormatInstance = TextFormat.findById(params.textFormat.id)
        if(SpringSecurityUtils.ifNotGranted(textFormatInstance?.roles)) {
            flash.message = "Sorry! You do not possess previleges to use " + textFormatInstance.name + " format"
            render(view: "create", model: [pageInstance: pageInstance])
            return
        }
        }
        params.body = contentService.formatBody(params.body,textFormatInstance)

        pageInstance = contentService.create(params, params.meta.list("type"), params.meta.list("value"), Page.class)
        if(pageInstance.hasErrors()) {
            flash.message = "Error saving Instance in controller action: " + pageInstance.errors
            render(view: "create", model: [pageInstance: pageInstance])
            return
        }
        flash.message = message(code: 'default.created.message', args: [message(code: 'page.label'), pageInstance.id])
        redirect uri: pageInstance.searchLink()
    } 
        
    def show(Page pageInstance) {
        respond(pageInstance)
    }

   /* def edit(Page pageInstance) {
        [pageInstance: pageInstance, contentRevisionList: ContentRevision.findAllByRevisionOf(pageInstance)]
    }*/
    
    def edit(Long id) {
        [pageInstance: pageInstance, formatsAvailable: textFormatService.applicableFormats,
            editor:pageInstance?.textFormat?.editor, contentRevisionList: ContentRevision.findAllByRevisionOf(pageInstance)]
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

        def textFormatInstance = TextFormat.findById(params.textFormat.id)
        if(SpringSecurityUtils.ifNotGranted(textFormatInstance?.roles)) {
            flash.message = "Sorry! You do not possess priveleges to use " + textFormatInstance.name + " format"
            render(view: "create", model: [pageInstance: pageInstance])
            return
        }

        params.body = contentService.formatBody(params.body,textFormatInstance)

        pageInstance = contentService.update(params, pageInstance, params.meta.list("type"), params.meta.list("value"))

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

    /**
     * To switch between Text Area and Ckeditor
     */
    def editorSwitch() {
        render(template: "bodyEditor", plugin:"content",
        model: [textFormatInstance: TextFormat.get(params.textInstanceId), useEditor:params.boolean('useEditor')])
    }
}