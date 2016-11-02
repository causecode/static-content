/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.blog.comment

import com.causecode.content.blog.Blog
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Join class provides comments for Blog.
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 */
@ToString
@EqualsAndHashCode
class BlogComment {

    Blog blog
    Comment comment

    static mapping = {
        table 'cc_content_blog_comment'
    }

    def afterDelete() {
        Comment.withNewSession {
            comment.delete(flush: true)
        }
    }

    @Override
    String toString() {
        return "BlogComment ($id)($blog.title)"
    }
}
