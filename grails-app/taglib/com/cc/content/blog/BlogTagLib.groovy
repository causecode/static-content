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
        commentContent(commentInstance, out, attrs, attrs.nested ?: false)
    }

    def nestedComment = { attrs, body ->
        List blogReplyComments = Comment.findAllByReplyTo(attrs.commentInstance)
        blogReplyComments.each {
            commentContent(it, out, attrs, true)
        }
    }

    private void commentContent(Comment commentInstance, out, Map attrs, boolean nested) {
        out << """<${nested ? 'div' : 'li'} class="media comment ${nested ? 'nested' : ''} ${attrs.classes ?: ''}">"""

        String template = attrs.bodyTemplate ?: "/blog/templates/commentBody"
        String plugin = attrs.inPlugin ?: "content"

        out << render(template: template, model: [commentInstance: commentInstance, nested: nested, attrs: attrs], plugin: plugin)

        out << """</${nested ? 'div' : 'li'}>"""
    }

    def searchLink = { attrs, body ->
        out << Blog.get(attrs.id).searchLink()
    }

}