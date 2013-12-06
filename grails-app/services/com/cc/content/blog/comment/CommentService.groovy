/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog.comment

class CommentService {

    static transactional = false

    void deleteNestedComment(Comment commentInstance, boolean deleteCommentAlso = true) {
        List<Comment> nestedCommentList = Comment.findAllByReplyTo(commentInstance)
        nestedCommentList.each {
            deleteNestedComment(it)
        }
        if(deleteCommentAlso) {
            commentInstance.delete(flush: true)
        }
    }

}