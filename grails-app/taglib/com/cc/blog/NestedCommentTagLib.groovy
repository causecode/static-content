/* Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are not permitted.*/


package com.cc.blog

class NestedCommentTagLib {
    
    def nestedComment = { attrs, body ->
        def blogReplyComments = Comment.findAllByReplyTo(Comment.get(attrs.commentId))
        if(blogReplyComments == []) {
            return
        }
        else {
            blogReplyComments.each {
                out << "<div class='comment' data-comment='${it.id}'>"
                out << "${it.subject}"
                out << "<br>"
                out << "${it.name}"
                out << " | "
                out << "Date : "
                out << g.formatDate(date: it.dateCreated, format: "MM-dd-yyyy")
                out << "<br>"
                out << "${it.commentText}"
                out << "<br>"
                out << "<a href='#myModal' role='button' class='btn' data-toggle='modal'>"
                out << "Reply"
                out << "</a>"
                out << g.nestedComment(commentId: it.id)
                out << "</div>"
            }
           
        }
    }
}
    
  