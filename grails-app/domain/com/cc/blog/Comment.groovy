/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.blog

class Comment {
    String subject
    String name
    String email
    String commentText
    Date dateCreated
    Comment replyTo

    static constraints = {
        name nullable: true
        replyTo nullable: true
        email email: true
    }
}
