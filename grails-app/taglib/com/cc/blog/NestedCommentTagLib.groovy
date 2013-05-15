/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.blog

class NestedCommentTagLib {

    static namespace = "com"

    def nestedComment = { attrs, body ->
        def blogReplyComments = Comment.findAllByReplyTo(Comment.get(attrs.commentId))
        if(blogReplyComments == []) {
            return
        }
        else {
            blogReplyComments.each {
                out << render(template:'comments', model:[it:it])
            }

        }
    }
}