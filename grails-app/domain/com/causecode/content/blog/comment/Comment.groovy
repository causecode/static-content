/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.blog.comment

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Used for storing comment contains reference for nested comment.
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 *
 */
@ToString(includes = ['id', 'commentText'], includePackage = false)
@EqualsAndHashCode
@SuppressWarnings(['GrailsDomainWithServiceReference'])
class Comment {

    static transients = ['commentService']

    CommentService commentService

    String commentText
    String email
    String name
    String subject

    Date dateCreated
    Date lastUpdated

    Comment replyTo

    static constraints = {
        name nullable: true
        replyTo nullable: true
        email email: true
        subject nullable: true
    }

    static mapping = {
        table 'cc_content_comment'
        commentText type: 'text'
    }

    def beforeDelete() {
        Comment.withNewSession {
            commentService.deleteNestedComment(this, false)
        }
    }
}
