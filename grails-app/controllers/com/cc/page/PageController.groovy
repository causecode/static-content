/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.page

import java.util.Iterator;

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.dao.DataIntegrityViolationException

import com.cc.annotation.shorthand.ControllerShorthand
import com.cc.content.*

@ControllerShorthand(value = "c")
class PageController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def beforeInterceptor = [action: this.&validate]
    def contentService
    def springSecurityService

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

    def list(Integer max) {
        params.sort = "dateCreated"
        params.order = "desc"
        params.max = Math.min(max ?: 10, 100)

        String contentManagerRole = grailsApplication.config.cc.plugins.content.contentMangerRole
        List pageInstanceList = Page.createCriteria().list(params) {
            if(SpringSecurityUtils.ifNotGranted(contentManagerRole))
                eq("publish", true)
        }
        [pageInstanceList: pageInstanceList, pageInstanceTotal: pageInstanceList.totalCount]
    }

    def create() {
        def textFormatNamesList = []
        if(!params.editor) {
            params.editor = true
        }
        def auth = SpringSecurityUtils.getPrincipalAuthorities()
        def textFormatList = TextFormat.getAll()
        for (textFormatInstance in textFormatList) {
            def rolesList = SpringSecurityUtils.parseAuthoritiesString(textFormatInstance.roles.toString()) 
            if (!auth.disjoint(rolesList)) {
                textFormatNamesList.add(textFormatInstance.name)
            }
        }
        def pageInstance = new Page(params)
        def textFormatInstance = new TextFormat()
        pageInstance.textFormat = textFormatInstance
        [pageInstance: pageInstance, formatsAvailable: textFormatNamesList, editor:params.boolean('editor')]
    }

    def save() {
//        pageInstance = contentService.create(params, params.meta.list("type"), params.meta.list("value"), Page.class)
        
        def textFormatInstance = TextFormat.findByName(params.textFormat.name)
        def tags = textFormatInstance.allowedTags

        if(tags && tags != "") {
            def tagsList = tags.tokenize(', ')
            String regexPart = ""
            for (tag in tagsList) {
                regexPart += "(?!" + tag.toString() + ")"
            }
            String regex = "<" + regexPart + "[^>]*" + regexPart + ">"
            params.body.replaceAll(regex, "")
        }

        render "Partial Text is :" + params.body
        
/*        if(!pageInstance.save(flush: true)) {
            render(view: "create", model: [pageInstance: pageInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'page.label'), pageInstance.id])
        redirect(action: "show", id: pageInstance.id)
*/
    }

    def show(Long id) {
        [pageInstance: pageInstance]
    }

    def edit(Long id) {
        [pageInstance: pageInstance]
    }

    def update(Long id, Long version) {
        if(version != null) {
            if (pageInstance.version > version) {
                pageInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'page.label')] as Object[],
                        "Another user has updated this Page while you were editing")
                render(view: "edit", model: [pageInstance: pageInstance])
                return
            }
        }
        pageInstance = contentService.update(params, pageInstance, params.meta.list("type"), params.meta.list("value"))

        if(pageInstance.hasErrors()) {
            render(view: "edit", model: [pageInstance: pageInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'page.label'), pageInstance.id])
        redirect(action: "show", id: pageInstance.id)
    }

    def delete(Long id) {
        try {
            Page.withTransaction {
                List<ContentMeta> contentMetaList = ContentMeta.findAllByContent(pageInstance)
                List metaList = contentMetaList*.meta
                contentMetaList*.delete()
                metaList*.delete()
                pageInstance.delete()
            }
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'page.label'), id])
            redirect(action: "list")
        } catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'page.label'), id])
            redirect(action: "show", id: id)
        }
    }
}