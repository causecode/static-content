/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog.comment

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import com.cc.content.blog.Blog

/**
 * Provides end point for delete comment for Content Manager.
 * @author Vishesh Duggar
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 *
 */
@Secured(["ROLE_CONTENT_MANAGER"])
@Transactional(readOnly = true)
class CommentController {

    /**
     * This action delete comment instance if it has been added as reply to another comment otherwise
     * delete join class reference added for blog for given comment instance.
     * 
     * After delete operation redirects to blog show page.
     * @param id REQUIRED Identity of Comment domain instance to be deleted.
     * @param blogId Identity of Blog domain instance to get blog and redirect to blog show page.
     */
    @Transactional
    def delete(Long id, Long blogId) {
        log.info "Parameters recived to delete comment: $params" + request.JSON
        Comment.withTransaction { status ->
            Blog blogInstance = Blog.get(blogId)
            Comment commentInstance = Comment.get(id)
            if(commentInstance.replyTo) {
                commentInstance.delete(flush: true)
            } else {
                BlogComment.findByComment(commentInstance)?.delete()
            }

            log.info "Comment deleted successfully."
            if (request.xhr) {
                render "true"
                return
            }
            flash.message = "Comments deleted successfully."
            redirect uri: blogInstance.searchLink()
        }
    }

}
