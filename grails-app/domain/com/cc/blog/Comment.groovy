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
