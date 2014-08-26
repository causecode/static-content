/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog.comment

import com.cc.content.blog.Blog

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

    List getComments(Blog blogInstance) {
        List<Comment> commentList = BlogComment.findAllByBlog(blogInstance)*.comment
        return getCommentsWithNestedComments(commentList)
    }

    Map getCommentData(Comment commentInstance) {
        if (!commentInstance) return [:]
        return [
            subject: commentInstance.subject, id: commentInstance.id,
            name: commentInstance.name, email: commentInstance.email, 
            commentText: commentInstance.commentText, lastUpdated: commentInstance.lastUpdated,
            comments: getCommentsWithNestedComments(Comment.findAllByReplyTo(commentInstance))]
    }

    List getCommentsWithNestedComments(List<Comment> commentList) {
        List comments = []
        commentList.each { commentInstance ->
            comments.add(getCommentData(commentInstance))
        }
        return comments
    }
}