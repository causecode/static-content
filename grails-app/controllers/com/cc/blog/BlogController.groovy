/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.blog

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.dao.DataIntegrityViolationException

class BlogController {

    def springSecurityService

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        if(!params.max) {
            params.max=2
        }
        if(!params.offset) {
            params.offset=0
        }
        def blogList = Blog.createCriteria()
        def blogs = blogList.list (max:params.max, offset:params.offset) {
            order("dateCreated","desc")
        }
        [blogInstanceList: blogs, blogInstanceTotal: blogs.getTotalCount(), userClass: userClass()]
    }

    def create() {
        [blogInstance: new Blog(params)]
    }

    def save() {
        def blogInstance = new Blog(params)
        bindData(blogInstance, params, [include: ['title', 'subTitle', 'body']])
        def principal= springSecurityService.getPrincipal()
        if(principal == "anonymousUser") {
            blogInstance.author = principal
        }
        else {
            blogInstance.author = principal.id as String

        }
        if (!blogInstance.save(flush: true)) {
            render(view: "create", model: [blogInstance: blogInstance])
            return
        }
        flash.message = message(code: 'default.created.message', args: [message(code: 'blog.label', default: 'Blog'), blogInstance.id])
        params.tags =params.tags.toLowerCase()
        def seperatedTags = params.tags.tokenize(',')
        seperatedTags.each() {
            blogInstance.addTag(it);
        }
        blogInstance.save()
        redirect(action: "show", id: blogInstance.id)
    }

    def show(Long id) {
        List arrayOfComments = []
        def username
        def blogInstance = Blog.get(id)
        if (!blogInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'blog.label', default: 'Blog'), id])
            redirect(action: "list")
            return
        }
        def userId = blogInstance.author
        if(userId.isNumber()) {
            userId.toInteger()
            def userInstance = userClass().get(userId)
            username= userInstance.username
        }
        else {
            username= "anonymousUser"
        }
        def blogComments = BlogComment.findAllByBlog(Blog.get(id))
        blogComments.each {
            def comment = it.comment
            arrayOfComments.add(comment)
        }
        [blogInstance: blogInstance, username : username, comments: arrayOfComments]
    }

    def edit(Long id) {
        def blogInstance = Blog.get(id)
        if (!blogInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'blog.label', default: 'Blog'), id])
            redirect(action: "list")
            return
        }
        [blogInstance: blogInstance]
    }

    def update(Long id, Long version) {
        def blogInstance = Blog.get(id)
        if (!blogInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'blog.label', default: 'Blog'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (blogInstance.version > version) {
                blogInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'blog.label', default: 'Blog')] as Object[],
                        "Another user has updated this Blog while you were editing")
                render(view: "edit", model: [blogInstance: blogInstance])
                return
            }
        }

        blogInstance.properties = params

        if (!blogInstance.save(flush: true)) {
            render(view: "edit", model: [blogInstance: blogInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'blog.label', default: 'Blog'), blogInstance.id])
        params.tags =params.tags.toLowerCase()
        def seperatedTags = params.tags.tokenize(',')
        blogInstance.getTags().each() {
            if(it in seperatedTags) { }
            else {
                blogInstance.removeTag(it)
            }
        }
        seperatedTags.each() {
            blogInstance.addTag(it);
        }
        blogInstance.save()
        redirect(action: "show", id: blogInstance.id)
    }

    def delete(Long id) {
        def blogInstance = Blog.get(id)
        if (!blogInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'blog.label', default: 'Blog'), id])
            redirect(action: "list")
            return
        }

        try {
            blogInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'blog.label', default: 'Blog'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'blog.label', default: 'Blog'), id])
            redirect(action: "show", id: id)
        }
    }

    def findByTag() {
        def blogList= Blog.findAllByTag(params.tag)
        render(view: "list", model: [blogInstanceList: blogList, blogInstanceTotal: blogList.size(), userClass: userClass()])
    }

    protected String lookupUserClassName() {
        SpringSecurityUtils.securityConfig.userLookup.userDomainClassName
    }

    protected Class<?> userClass() {
        grailsApplication.getDomainClass(lookupUserClassName()).clazz
    }

    def comment() {
        def commentInstance = new Comment()
        bindData(commentInstance, params, [include: ['subject', 'name', 'email', 'commentText']])
        commentInstance.validate()
        if (!commentInstance.save(flush: true)) {
            redirect(action: "show", id:params.blog_id)
            return
        }
        if (params.replyCommentId.isNumber()) {
            commentInstance.replyTo = Comment.get(params.replyCommentId)
            commentInstance.save()
        }
        else {
            def blogCommentInstance = new BlogComment()
            blogCommentInstance.blog = Blog.get(params.blog_id)
            blogCommentInstance.comment = commentInstance
            blogCommentInstance.validate()
            blogCommentInstance.save()
        }
        redirect(action: "show", id: params.blog_id)
    }

}