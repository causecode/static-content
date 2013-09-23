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

    def blogService
    def index() { }

    def delete(Long id) {
        Comment.withTransaction { status ->
            Comment commentInstance = Comment.get(id)
            BlogComment blogCommentInstance = BlogComment.findByComment(commentInstance)
            if(blogCommentInstance) {
                blogCommentInstance.delete()
            }
            List<Comment> nestedCommentList = blogService.getNestedCommentAndDelete(commentInstance)
            redirect(action: "list", controller: "blog")
        }
    }

}
