package com.causecode.content.blog.comment

import com.causecode.content.blog.Blog
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Join class provides comments for Blog.
 * @author Shashank Agrawal
 * @author Laxmi Salunkhe
 */
@ToString(includes = ['id', 'blog.title'], includePackage = false)
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
}
