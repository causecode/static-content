/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.news

import grails.plugins.springsecurity.Secured

import org.springframework.dao.DataIntegrityViolationException

import com.cc.iframe.Scraper

@Secured(["ROLE_CONTENT_MANAGER"])
class NewsController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def beforeInterceptor = [action: this.&validate]

    private News newsInstance

    private validate() {
        if(!params.id) return true;

        newsInstance = News.get(params.id)
        if(!newsInstance) {
            flash.message = g.message(code: 'default.not.found.message', args: [message(code: 'news.label', default: 'News'), params.id])
            redirect(action: "list")
            return false
        }
        return true
    }

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [newsInstanceList: News.list(params), newsInstanceTotal: News.count()]
    }

    def create() {
        [newsInstance: new News(params)]
    }

    def save() {
        newsInstance = new News(params)
        if (!newsInstance.save(flush: true)) {
            render(view: "create", model: [newsInstance: newsInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'news.label', default: 'News'), newsInstance.id])
        redirect(action: "show", id: newsInstance.id)
    }

    def show(Long id) {
        [newsInstance: newsInstance]
    }

    def edit(Long id) {
        [newsInstance: newsInstance]
    }

    def update(Long id, Long version) {
        if(version != null) {
            if (newsInstance.version > version) {
                newsInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'news.label', default: 'News')] as Object[],
                        "Another user has updated this News while you were editing")
                render(view: "edit", model: [newsInstance: newsInstance])
                return
            }
        }

        newsInstance.properties = params

        if (!newsInstance.save(flush: true)) {
            render(view: "edit", model: [newsInstance: newsInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'news.label', default: 'News'), newsInstance.id])
        redirect(action: "show", id: newsInstance.id)
    }

    def delete(Long id) {
        try {
            newsInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'news.label', default: 'News'), id])
            redirect(action: "list")
        } catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'news.label', default: 'News'), id])
            redirect(action: "show", id: id)
        }
    }

    def link(String page) {
        response.setHeader("X-Frame-Options", "GOFORIT")
        [page: page, externalContent: externalContent(page)]
    }

    private String externalContent(String page) {
        String externalContent = new Scraper(page).scrape()
        return externalContent
    }

}