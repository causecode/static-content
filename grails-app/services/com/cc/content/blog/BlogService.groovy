/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.cc.content.blog

import org.codehaus.groovy.grails.commons.metaclass.GroovyDynamicMethodsInterceptor
import org.codehaus.groovy.grails.web.metaclass.BindDynamicMethod
import org.springframework.dao.DataIntegrityViolationException

import com.cc.content.blog.comment.Comment
import com.sun.org.apache.xpath.internal.operations.String;

class BlogService {

    BlogService() {
        GroovyDynamicMethodsInterceptor i = new GroovyDynamicMethodsInterceptor(this)
        i.addDynamicMethodInvocation(new BindDynamicMethod())
    }

    List<Comment> getNestedCommentAndDelete(Comment commentInstance) {
        String message
        List<Comment> nestedCommentList = Comment.findAllByReplyTo(commentInstance)
        if(nestedCommentList) {
            nestedCommentList.each {
                List<Comment> nestedSubCommentList = getNestedCommentAndDelete(it)
            }
        } else {
            commentInstance.delete(flush: true)
            return
        }
    }
}