/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog.comment

import com.cc.content.blog.Blog

class CommentController {

    def blogService

    def delete(Long id, Long blogId) {
        Comment.withTransaction { status ->
            Blog blogInstance = Blog.get(blogId)
            Comment commentInstance = Comment.get(id)
            BlogComment.findByComment(commentInstance)?.delete()

            blogService.deleteNestedComment(commentInstance)

            flash.message = "Comments deleted successfully."
            redirect uri: blogInstance.searchLink()
        }
    }

}
