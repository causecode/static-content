/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.news

import grails.plugin.springsecurity.annotation.Secured

import org.springframework.dao.DataIntegrityViolationException

import grails.converters.JSON

import com.cc.iframe.Scraper

/**
 * Provides end point to link news information of type content for Content Manager.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 *
 */
@Secured(["permitAll"])
class NewsController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "PUT"]

    def beforeInterceptor = [action: this.&validate]
    
    static responseFormats = ["json"]

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

    def list(Integer max, Integer offset) {
        params.max = Math.min(max ?: 10, 100)
        params.offset = offset ? offset: 0
        respond ( [instanceList: News.list(params), totalCount: News.count()] )
    }

    def create() {
        [newsInstance: new News(params)]
    }

    def save() {
        params.putAll(request.JSON)
        println(params)
        newsInstance = new News(params)
        if (!newsInstance.save(flush: true)) {
            respond(newsInstance.errors)
            println("fail")
            return
        }
        println("pass")
        respond(newsInstance)
    }

    def show(Long id) {
        println("in show")
        respond(newsInstance)
    }

    def edit(Long id) {
        [newsInstance: newsInstance]
    }

    def update(Long id, Long version) {
        println("in update")
        params.putAll(request.JSON)
        if(version != null) {
            if (newsInstance.version > version) {
                newsInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'news.label', default: 'News')] as Object[],
                        "Another user has updated this News while you were editing")
                respond(newsInstance.errors)
                return
            }
        }

        println("jerer")
        newsInstance.properties = params

        if (!newsInstance.save(flush: true)) {
            println("fail")
            respond(newsInstance.errors)
            return 
        }

        respond ([success: true])
        return
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
