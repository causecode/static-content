/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.blog

import com.causecode.content.blog.comment.Comment

/**
 * This taglib provides tags for rendering comments on blog.
 * @author Shashank Agrawal
 *
 */
class BlogTagLib {

    static namespace = 'content'

    /**
     * Used to render comment on blog. This tag uses template to render comment body.
     * @attr commentInstance REQUIRED
     * @attr classes CSS classes used to apply for comments.
     * @attr bodyTemplate Template path which is used to render comment body.
     *                    Default set to '/blog/templates/commentBody'
     * @attr inPlugin Used to Specify Plugin, Default set to 'content'.
     */
    def comment = { attrs, body ->
        Comment commentInstance = attrs.commentInstance
        commentContent(commentInstance, out, attrs, attrs.nested ?: false)
    }

    /**
     * Used to render nested comment on blog . This tag uses template to render comments body.
     *
     * @attr commentInstance REQUIRED
     * @attr classes CSS classes used to apply for comments.
     * @attr bodyTemplate Template path which is used to render comment body.
     *                    Default set to '/blog/templates/commentBody'
     * @attr inPlugin Used to Specify Plugin, Default set to 'content'.
     */
    def nestedComment = { attrs, body ->
        List blogReplyComments = Comment.findAllByReplyTo(attrs.commentInstance)
        blogReplyComments.each {
            commentContent(it, out, attrs, true)
        }
    }

    /**
     * Common method block used to render comment on blog.
     * Uses all attributes from comment and nestedComment tag and renders comment body.
     */
    private void commentContent(Comment commentInstance, out, Map attrs, boolean nested) {
        out << """<${nested ? 'div' : 'li'} class="media comment ${nested ? 'nested' : ''} ${attrs.classes ?: ''}">"""

        String template = attrs.bodyTemplate ?: '/blog/templates/commentBody'
        String plugin = attrs.inPlugin ?: 'content'

        out << render(template: template, model: [commentInstance: commentInstance,
                                                  nested: nested, attrs: attrs], plugin: plugin)

        out << """</${nested ? 'div' : 'li'}>"""
    }

    /**
     * Used to render Blog show page link.
     * @attr id REQUIRED ID of Blog Instance.
     */
    def searchLink = { attrs, body ->
        out << Blog.get(attrs.id).searchLink()
    }
}
