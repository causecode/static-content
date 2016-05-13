/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */

package com.causecode.content.blog

import org.grails.taggable.TagLink
import com.com.content.blog.BlogContentType
import java.lang.Integer
import com.com.content.blog.comment.CommentService
import com.com.content.blog.BlogService
import com.com.content.meta.Meta
import java.lang.IllegalArgumentException
import grails.plugins.taggable.TagLink

class BlogService {

    static transactional = false

    CommentService commentService

    List getAllTags() {
        List tagList = []
        Blog.allTags.each { tagName ->
            def tagListInstance = TagLink.withCriteria(uniqueResult: true) {
                createAlias('tag', 'tagInstance')
                projections {
                    countDistinct 'id'
                    property("tagInstance.name")
                }
                eq('type','blog')
                eq('tagInstance.name', tagName)
            }
            tagList.add(tagListInstance)
        }
        return tagList
    }

    BlogContentType findBlogContentTypeByValue(String value) {
        BlogContentType blogContentType
        try {
            blogContentType = BlogContentType.valueOf(value?.toUpperCase() ?: "")
        } catch (IllegalArgumentException e) {
            log.info("Invalid value $value for BlogContentType")
        }
        return blogContentType
    }

    Map getBlog(Blog blogInstance, boolean convertToMarkdown) {
        List blogComments = commentService.getComments(blogInstance)
        List tagList = getAllTags()
        List<String> blogInstanceTags = blogInstance.tags
        // Convert markdown content into html format
        if (convertToMarkdown) {
            blogInstance.body = blogInstance.body?.markdownToHtml()
        }

        List<Blog> blogInstanceList = Blog.findAllByPublish(true, [max: 5, sort: 'publishedDate', order: 'desc'])
        List<Meta> metaInstanceList = blogInstance.getMetaTags()

        return [blogInstance: blogInstance, comments: blogComments, tagList: tagList,
                blogInstanceList: blogInstanceList, blogInstanceTags: blogInstanceTags, metaList: metaInstanceList]
    }

}