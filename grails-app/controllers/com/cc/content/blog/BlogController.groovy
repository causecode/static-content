/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog

import java.util.regex.Matcher
import java.util.regex.Pattern

import org.grails.taggable.TagLink
import org.springframework.dao.DataIntegrityViolationException

import com.cc.content.blog.comment.BlogComment
import com.cc.content.blog.comment.Comment

class BlogController {

    static defaultAction = "list"

    static allowedMethods = [save: "POST", update: "POST"]

    def beforeInterceptor = [action: this.&validate]

    def contentService
    def springSecurityService

    private Blog blogInstance

    private static String HTML_P_TAG_PATTERN = "(?s)<p(.*?)>(.*?)<\\/p>"

    private boolean validate() {
        if(!params.id) {
            return true
        }
        blogInstance = Blog.get(params.id)
        if (!blogInstance) {
            flash.message = "Sorry, no blog found with Id $params.id"
            redirect(action: "list")
            return false
        }
        return true
    }

    def list(Integer max, Integer offset, String tag) {
        long blogInstanceTotal
        boolean publish = false

        params.offset = offset ? offset: 0
        params.max = Math.min(max ?: 2, 100)

        StringBuilder query = new StringBuilder("""SELECT new Map(b.id as id, b.body as body, b.title as title,
                            b.subTitle as subTitle, b.author as author, b.dateCreated as dateCreated) FROM Blog b """)

        if(tag) {
            query.append(", ${TagLink.class.name} tagLink WHERE b.id = tagLink.tagRef ")
            query.append("AND tagLink.type = 'blog' AND tagLink.tag.name = '$tag' ")
        }

        if(contentService.contentManager) {
            blogInstanceTotal = tag ? Blog.countByTag(tag) : Blog.count()
        } else if(tag) {
            query.append("AND b.publish = true")
            blogInstanceTotal = Blog.findAllByTagWithCriteria(tag) {
                eq("publish", true)
            }.size()
        } else {
            query.append("WHERE b.publish = true")
            blogInstanceTotal = Blog.countByPublish(true)
        }
        query.append("order by b.dateCreated desc")
        List<Map> blogList = Blog.executeQuery(query.toString(), [max: params.max, offset: params.offset])
        Pattern patternTag = Pattern.compile(HTML_P_TAG_PATTERN)

        blogList.each {
            Matcher matcherTag = patternTag.matcher(it.body)
            it.body = matcherTag.find() ? matcherTag.group(2) : ""
        }

        [blogInstanceList: blogList, blogInstanceTotal: blogInstanceTotal]
    }

    def create() {
        [blogInstance: new Blog(params)]
    }

    def save() {
        Blog.withTransaction { status ->
            blogInstance = contentService.create(params, params.meta.list("type"), params.meta.list("value"), Blog.class)
            if (blogInstance.hasErrors()) {
                status.setRollbackOnly()
                render(view: "create", model: [blogInstance: blogInstance])
                return
            }
            blogInstance.setTags(params.tags.tokenize(",")*.trim())
            blogInstance.save(flush: true)
            flash.message = message(code: 'default.created.message', args: [message(code: 'blog.label'), blogInstance.id])
            redirect(action: "show", id: blogInstance.id)
        }
    }

    def show(Long id) {
        List blogComments = BlogComment.findAllByBlog(blogInstance)*.comment
        [blogInstance: blogInstance, comments: blogComments]
    }

    def edit(Long id) {
        [blogInstance: blogInstance]
    }

    def update(Long id, Long version) {
        if (version != null) {
            if (blogInstance.version > version) {
                blogInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'blog.label')] as Object[],
                        "Another user has updated this Blog while you were editing")
                render(view: "edit", model: [blogInstance: blogInstance])
                return
            }
        }

        Blog.withTransaction { status ->
            String tags = params.remove("tags")
            contentService.update(params, blogInstance, params.meta.list("type"), params.meta.list("value"))

            if (blogInstance.hasErrors()) {
                status.setRollbackOnly()
                render(view: "edit", model: [blogInstance: blogInstance])
                return
            }

            blogInstance.setTags(tags.tokenize(",")*.trim())
            blogInstance.save(flush: true)

            flash.message = message(code: 'default.updated.message', args: [message(code: 'blog.label'), blogInstance.id])
            redirect(action: "show", id: blogInstance.id)
        }
    }

    def delete(Long id) {
        try {
            blogInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'blog.label'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'blog.label'), id])
            redirect(action: "show", id: id)
        }
    }

    def comment(Long commentId) {
        Comment.withTransaction { status ->
            Comment commentInstance = new Comment()
            bindData(commentInstance, params, [include: ['subject', 'name', 'email', 'commentText']])
            if(!commentInstance.save(flush: true)) {
                status.setRollbackOnly()
                redirect(action: "show", id: params.id)
                return
            }
            if(commentId) {
                commentInstance.replyTo = Comment.get(commentId)
                commentInstance.save()
            } else {
                BlogComment blogCommentInstance = new BlogComment()
                blogCommentInstance.blog = blogInstance
                blogCommentInstance.comment = commentInstance
                blogCommentInstance.save()
            }
            redirect(action: "show", id: params.id)
        }
    }

}