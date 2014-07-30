/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog.comment

/**
 * This taglib provides tags for rendering comments on blog.
 * @author Shashank Agrawal
 */
class CommentService {

    static transactional = false

    /**
     * Used to delete comment and its nested comments.
     * @param commentInstance REQUIRED Comment Instance whose nested comments will be deleted.
     * @param deleteCommentAlso Boolean parameter default set to true and causes to delete comment instance as well.
     * If it set to false only nested comments will be deleted for given comment instance.
     */
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