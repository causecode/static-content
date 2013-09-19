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

    def nestedComment = { attrs, body ->
        List blogReplyComments = Comment.findAllByReplyTo(attrs.commentInstance)
        blogReplyComments.each {
            out << render(template: '/blog/templates/comments', model: [commentInstance: it], plugin: "content")
        }
    }

}