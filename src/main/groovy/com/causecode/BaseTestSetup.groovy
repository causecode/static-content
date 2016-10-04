/*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode

import com.causecode.content.Content
import com.causecode.content.ContentMeta
import com.causecode.content.PageLayout
import com.causecode.content.blog.Blog
import com.causecode.content.blog.BlogContentType
import com.causecode.content.blog.comment.Comment
import com.causecode.content.faq.FAQ
import com.causecode.content.meta.Meta
import com.causecode.content.page.Page
import grails.plugins.taggable.Tag

/**
 * This class contains common setup that can be used in unit, functional and integration test cases.
 * It helps in getting data maps or instances for any domain so that we don't have to repeat the creation of data
 * maps or instances for each and every test case.
 *
 * It also contains a dynamic method for creating multiple instances. To use that make sure to add methods as
 * 'getDomainNameDataMap' followed by 'getDomainNameInstance'. The 'getDomainNameInstance' uses the
 * 'getDomainNameDataMap' method for creating new instances and asserting the ids.
 *
 * Example:
 *
 * For creating 10 Blog instance for testing index action. Simple call createInstances method with domain name
 * as first parameter and count as the second parameter.
 *
 * createInstances('Blog', 10)
 *
 * @author Sachidanand Vaishnav
 */
trait BaseTestSetup {

    private static final Map FLUSH_TRUE = [flush: true]

    // Create Instance
    def createInstances(String domainName, int count) {
        count.times {
            "get${domainName}Instance"(it + 1)
        }
    }

    // Tag
    Tag getTagInstance(int index) {
        Tag tagInstance = new Tag([name: "Tag name-$index"])
        tagInstance.save(FLUSH_TRUE)

        if (tagInstance.hasErrors()) {
            tagInstance.errors.dump()
        }

        assert tagInstance.id

        return tagInstance
    }

    // Content
    Map getContentParams(int index) {
        return [
                title: "Targeting Test $index Types and/or Phases",
                subTitle: "To execute the JUnit integration test $index",
                body: 'Grails organises tests by phase and by type. The state of the Grails application.',
                author: "Author $index",
                version: index,
                publish: true,
                publishedDate: new Date(),
                contentType: BlogContentType.MARKDOWN
        ]
    }

    // Blog
    Blog getBlogInstance(int index) {
        Blog blogInstance = new Blog(getContentParams(index))
        blogInstance.save(FLUSH_TRUE)

        // Adding Tags as List
        blogInstance.tags = [getTagInstance(index)]
        blogInstance.save(FLUSH_TRUE)

        assert blogInstance.id

        return blogInstance
    }

    // Comment
    Map getCommentDataMap(int index) {
        return [
                commentText: "Comment text $index",
                email: "comment-$index@test.com",
                name: "Test user-$index",
                subject: 'Test comment subject',
                dateCreated: new Date(),
                lastUpdated: new Date() + 10
        ]
    }

    Comment getCommentInstance(int index) {
        Comment commentInstance = new Comment(getCommentDataMap(index))
        commentInstance.save(FLUSH_TRUE)
        assert commentInstance.id

        return commentInstance
    }

    // PageLayout Instance
    PageLayout getPageLayoutInstance(int index) {
        PageLayout pageLayoutInstance = new PageLayout([layoutName: "TestPageLayout-$index"])
        pageLayoutInstance.save(FLUSH_TRUE)
        assert pageLayoutInstance.id

        return pageLayoutInstance
    }

    // Page Instance
    Page getPageInstance(int index) {
        Page pageInstance = new Page(getContentParams(index))
        pageInstance.pageLayout = getPageLayoutInstance(index)
        pageInstance.save(FLUSH_TRUE)
        assert pageInstance.id

        return pageInstance
    }

    // FAQ Instance
    FAQ getFAQInstance(int index) {
        FAQ faqInstance = new FAQ(getContentParams(index))
        faqInstance.save(FLUSH_TRUE)
        assert faqInstance.id

        return faqInstance
    }

    // Content
    Content getContentInstance(int index) {
        Content contentInstance = new Content(getContentParams(index))
        contentInstance.save(FLUSH_TRUE)

        assert contentInstance.id
        assert contentInstance.toString()

        return contentInstance
    }

    // Meta
    Meta getMetaInstance(int index) {
        Map args = [type: "keywords-$index", content: '500Hrs,TechStars,YCombinator,MassChallenge']
        Meta metaInstance = new Meta(args)
        metaInstance.save(FLUSH_TRUE)

        assert metaInstance.id

        return metaInstance
    }

    // ContentMeta Instance
    ContentMeta getContentMetaInstance(int index) {
        Map args = [content: getContentInstance(index), meta: getMetaInstance(index)]
        ContentMeta contentMetaInstance = new ContentMeta(args)
        contentMetaInstance.save(FLUSH_TRUE)

        assert contentMetaInstance.id

        return contentMetaInstance
    }
}
