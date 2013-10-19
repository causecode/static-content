/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog.comment

class Comment {

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
    }

    static mapping = {
        table "cc_content_comment"
        commentText type: "text"
    }

}