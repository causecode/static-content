/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog.comment

import grails.plugins.springsecurity.Secured

import com.cc.content.blog.Blog

@Secured(["ROLE_CONTENT_MANAGER"])
class CommentController {

    def delete(Long id, Long blogId) {
        Comment.withTransaction { status ->
            Blog blogInstance = Blog.get(blogId)
            Comment commentInstance = Comment.get(id)

            if(commentInstance.replyTo) {
                commentInstance.delete()
            } else {
                BlogComment.findByComment(commentInstance)?.delete()
            }

            flash.message = "Comments deleted successfully."
            redirect uri: blogInstance.searchLink()
        }
    }

}
