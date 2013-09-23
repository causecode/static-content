/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog.comment

import org.springframework.dao.DataIntegrityViolationException

class CommentController {

    def index() { }

    def delete(Long id) {
        Comment.withTransaction { status ->
            Comment commentInstance = Comment.get(id)
            BlogComment blogCommentInstance = BlogComment.findByComment(commentInstance)
            if(blogCommentInstance) {
                blogCommentInstance.delete()
            }
            List<Comment> nestedCommentList = getNestedCommentAndDelete(commentInstance)
            redirect(action: "list", controller: "blog")
        }
    }

    List<Comment> getNestedCommentAndDelete(Comment commentInstance) {
        List<Comment> nestedCommentList = Comment.findAllByReplyTo(commentInstance)
        if(nestedCommentList) {
            nestedCommentList.each {
                List<Comment> nestedSubCommentList = getNestedCommentAndDelete(it)
            }
        } else {
            try {
                commentInstance.delete(flush: true)
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'comment.label'), commentInstance.id])
                return
            }
            catch (DataIntegrityViolationException e) {
                flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'comment.label'), commentInstance.id])
                return
            }
        }
    }
}
