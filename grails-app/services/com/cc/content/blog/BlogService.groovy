/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog

import com.cc.content.blog.comment.Comment

class BlogService {

    void deleteNestedComment(Comment commentInstance) {
        List<Comment> nestedCommentList = Comment.findAllByReplyTo(commentInstance)
        nestedCommentList.each {
            List<Comment> nestedSubCommentList = deleteNestedComment(it)
        }
        commentInstance.delete()
    }

}