/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog.comment

import grails.plugins.springsecurity.Secured
import grails.transaction.Transactional
import com.cc.content.blog.Blog

@Secured(["ROLE_CONTENT_MANAGER"])
@Transactional(readOnly = true)
class CommentController {

    def delete(Long id, Long blogId) {
        log.info "Parameters recived to delete comment: $params" + request.JSON
        Comment.withTransaction { status ->
            Blog blogInstance = Blog.get(blogId)
            Comment commentInstance = Comment.get(id)
            println "commentInstance"+commentInstance.dump()
            if(commentInstance.replyTo) {
                commentInstance.delete()
            } else {
                BlogComment.findByComment(commentInstance)?.delete()
            }

            if (request.xhr) {
                render "true"
                return
            }
            flash.message = "Comments deleted successfully."
            redirect uri: blogInstance.searchLink()
        }
    }

}
