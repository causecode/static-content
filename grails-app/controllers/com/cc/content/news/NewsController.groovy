/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.news

import grails.plugin.springsecurity.annotation.Secured

import org.grails.databinding.SimpleMapDataBindingSource
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus

import com.cc.iframe.Scraper

/**
 * Provides end point to link news information of type content for Content Manager.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 *
 */
@Secured(["ROLE_CONTENT_MANAGER"])
class NewsController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    static responseFormats = ["json"]

    def grailsWebDataBinder

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max, Integer offset) {
        params.max = Math.min(max ?: 10, 100)
        params.offset = offset ? offset: 0
        respond ([instanceList: News.list(params), totalCount: News.count()])
    }

    def create() {
        [newsInstance: new News(params)]
    }

    def save() {
        Map requestData = request.JSON
        log.info "Parameters received save news instance: ${requestData}"
        News newsInstance = new News()
        grailsWebDataBinder.bind(newsInstance, requestData as SimpleMapDataBindingSource, ["title", "subTitle",
            "author", "body", "publish"], null)
        newsInstance.validate()

        if (newsInstance.hasErrors()) {
            log.warn "Error saving news Instance: $newsInstance.errors."
            respond ([errors: newsInstance.errors], status: HttpStatus.NOT_MODIFIED)
            return
        } else {
            log.info "News instance saved successfully."
            newsInstance.save(flush: true)
        }
        respond ([status: HttpStatus.OK])
    }

    def show(News newsInstance) {
        respond(newsInstance)
    }

    def edit(News newsInstance) {
        [newsInstance: newsInstance]
    }

    def update(News newsInstance, Long version) {
        if (!newsInstance) {
            log.warn message(code: 'default.not.found.message', args: [message(code: 'news.label'), params.id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (newsInstance.version > version) {
                respond ([message: "Another user has updated this news inatnce while you were editing"],
                    status: HttpStatus.NOT_MODIFIED)
                return
            }
        }

        Map requestData = request.JSON
        grailsWebDataBinder.bind(newsInstance, requestData as SimpleMapDataBindingSource, ["title", "subTitle",
            "author", "body", "publish"], null)
        newsInstance.validate()

        if (newsInstance.hasErrors()) {
            log.warn "Error updating news Instance: $newsInstance.errors."
            respond ([errors: newsInstance.errors], status: HttpStatus.NOT_MODIFIED)
            return
        } else {
            log.info "News instance updated successfully."
            newsInstance.save(flush: true)
        }
        respond ([status: HttpStatus.OK])
    }

    def delete(News newsInstance) {
        try {
            newsInstance.delete(flush: true)
        } catch (DataIntegrityViolationException e) {
            respond ([status: HttpStatus.NOT_MODIFIED])
            return
        }
        respond ([status: HttpStatus.OK])
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
