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
import com.cc.content.blog.Blog

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

    /**
     * Returns list of all comments (including nested comments) on given blogInstance.
     * @param blogInstance
     * @return List of comments
     */
    List getComments(Blog blogInstance) {
        List<Comment> commentList = BlogComment.findAllByBlog(blogInstance)*.comment
        return getCommentsWithNestedComments(commentList)
    }

    /**
     * Accepts Comment instance and returns a Map of comment data which includes nested comments if
     * it has any.
     * @param commentInstance
     * @return
     */
    Map getCommentData(Comment commentInstance) {
        if (!commentInstance) return [:]
        return [
            subject: commentInstance.subject, id: commentInstance.id,
            name: commentInstance.name, email: commentInstance.email, 
            commentText: commentInstance.commentText, lastUpdated: commentInstance.lastUpdated,
            comments: getCommentsWithNestedComments(Comment.findAllByReplyTo(commentInstance))]
    }

    /**
     * Populates data from Comment instance in commentList into a Map and creates List
     * of Map. 
     * @param commentList
     * @return List of Map containing comment data
     */
    List getCommentsWithNestedComments(List<Comment> commentList) {
        List comments = []
        commentList.each { commentInstance ->
            comments.add(getCommentData(commentInstance))
        }
        return comments
    }
}