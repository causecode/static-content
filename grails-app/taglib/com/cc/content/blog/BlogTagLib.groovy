/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog

import com.cc.content.blog.comment.Comment

class BlogTagLib {

    static namespace = "content"

    def comment = { attrs, body ->
        Comment commentInstance = attrs.commentInstance
        out << """<li class="media comment">"""
        out << render(template: '/blog/templates/commentBody', model: [commentInstance: commentInstance], plugin: "content")
        out << """</li>"""
    }

    def nestedComment = { attrs, body ->
        List blogReplyComments = Comment.findAllByReplyTo(attrs.commentInstance)
        blogReplyComments.each {
            out << """<div class="media comment nested">"""
            out << render(template: '/blog/templates/commentBody', model: [commentInstance: it, nested: true], plugin: "content")
            out << """</div>"""
        }
    }

    def searchLink = { attrs, body ->
        out << Blog.get(attrs.id).searchLink()
    }

}