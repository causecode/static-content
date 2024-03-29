/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.blog

import com.causecode.content.ContentService
import com.causecode.content.blog.comment.BlogComment
import com.causecode.content.blog.comment.CommentService
import com.causecode.content.meta.Meta
import grails.plugins.taggable.TagLink

import java.text.DateFormatSymbols
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * A service for all blog related operations.
 */
class BlogService {

    static transactional = false

    CommentService commentService
    ContentService contentService

    List getAllTags() {
        List tagList = []
        Blog.allTags.each { tagName ->
            def tagListInstance = TagLink.withCriteria([uniqueResult: true]) {
                createAlias('tag', 'tagInstance')
                projections {
                    countDistinct 'id'
                    property('tagInstance.name')
                }
                eq('type', 'blog')
                eq('tagInstance.name', tagName)

                maxResults(1)
            }
            tagList.add(tagListInstance)
        }

        return tagList
    }

    BlogContentType findBlogContentTypeByValue(String value) {

        BlogContentType blogContentType
        try {
            blogContentType = BlogContentType.valueOf(value?.toUpperCase() ?: '')
        } catch (IllegalArgumentException e) {
            log.info("Invalid value $value for BlogContentType")
        }

        return blogContentType
    }

    Map getBlog(Blog blogInstance, boolean convertToMarkdown) {
        List blogComments = commentService.getComments(blogInstance)
        List tagList = allTags
        List<String> blogInstanceTags = blogInstance.tags
        // Convert markdown content into html format
        if (convertToMarkdown) {
            blogInstance.body = blogInstance.body?.markdownToHtml()
        }

        List<Blog> blogInstanceList = Blog.findAllByPublish(true, [max: 5, sort: 'publishedDate', order: 'desc'])
        List<Meta> metaInstanceList = blogInstance.metaTags

        return [blogInstance: blogInstance, comments: blogComments, tagList: tagList,
                blogInstanceList: blogInstanceList, blogInstanceTags: blogInstanceTags, metaList: metaInstanceList]
    }

    StringBuilder queryModifierBasedOnFilter(Map params) {
        StringBuilder updatedQuery = params.query
        if (params.tag) {
            updatedQuery.append(", ${TagLink.name} tagLink WHERE b.id = tagLink.tagRef ")
            updatedQuery.append("AND tagLink.type = 'blog' AND tagLink.tag.name = '$params.tag' ")
        }
        if (params.monthFilter) {
            params.tag ? updatedQuery.append(' AND ') : updatedQuery.append(' WHERE ')
            updatedQuery.append(" monthname(b.publishedDate) = '$params.monthYearFilterMapInstance.month' " +
                    "AND year(b.publishedDate) = '$params.monthYearFilterMapInstance.year'")
        }
        if (params.queryFilter) {
            params.tag ? updatedQuery.append(' AND ') :
                    (params.monthFilter ? updatedQuery.append(' AND ') : updatedQuery.append(' WHERE '))
            updatedQuery.append(" (b.title LIKE '%$params.queryFilter%' OR b.subTitle LIKE '%$params.queryFilter%' ")
            updatedQuery.append(" OR b.body LIKE '%$params.queryFilter%' OR b.author LIKE '%$params.queryFilter%')")
        }
        if (!params.isContentManager && (params.tag || params.monthFilter || params.queryFilter)) {
            updatedQuery.append(' AND b.publish = true')
        } else if (!params.isContentManager) {
            updatedQuery.append(' where b.publish = true')
        }

        return updatedQuery
    }

    /**
     * This method is used to get the unique [month-year] list of the published blogs .i.e. the month and year on which
     * the particular blog is published; further this list will be used as filter for various search pattern.
     *
     * @param monthFilterList List to be updated.
     * @return updateMonthFilterList updated list of [month-year]
     */
    List<String> updatedMonthFilterListBasedOnPublishedDate(List<String> monthFilterList) {
        List<String> updateMonthFilterList = monthFilterList
        List<Blog> publishDateList = Blog.createCriteria().list {
            projections {
                property('publishedDate')
            }
            eq('publish', true)
            isNotNull('publishedDate')

            maxResults(20)
        }
        publishDateList.each { publishedDate ->
            updateMonthFilterList.add(new DateFormatSymbols().months[publishedDate[Calendar.MONTH]] + '-' +
                    publishedDate[Calendar.YEAR])
        }

        return updateMonthFilterList
    }

    /**
     * This method is used to get list of Blogs with there summary.
     *
     * @param blogList List of blogs
     * @param patternTag Pattern to match the search content.
     *
     * @return List of blogs
     */
    List<Blog> getBlogSummaries(List<Map> blogList, Pattern patternTag) {
        List<Blog> blogInstanceList = []
        blogList.each {
            Blog blogInstance = Blog.get(it.id as long)

            if (!blogInstance) {
                log.debug('No blog found')
                return
            }

            // Convert markdown content into html format so that first paragraph could be extracted from it
            it.body = it.body?.markdownToHtml()
            // Extracting content from first <p> tag of body to display
            Matcher matcherTag = patternTag.matcher(it.body)
            it.body = matcherTag.find() ? matcherTag.group(2) : ''
            it.author = contentService.resolveAuthor(blogInstance)
            it.numberOfComments = BlogComment.countByBlog(blogInstance)
            it.blogImgSrc = blogInstance.blogImg?.path
            blogInstanceList.add(it)
        }

        return blogInstanceList
    }

}
