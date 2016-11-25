/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content

import com.causecode.content.Content
import com.causecode.content.ContentMeta
import com.causecode.content.PageLayout
import com.causecode.content.blog.Blog
import com.causecode.content.blog.BlogContentType
import com.causecode.content.blog.comment.Comment
import com.causecode.content.faq.FAQ
import com.causecode.content.meta.Meta
import com.causecode.content.page.Page
import grails.plugin.springsecurity.SpringSecurityUtils
import spock.util.mop.ConfineMetaClassChanges

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
 * For creating 10 Blog instance for testing index action. Simple call createInstances method with domain name
 * as first parameter and count as the second parameter.
 *
 * createInstances('Blog', 10)
 *
 * @author causecode
 */
trait BaseTestSetup {

    // Mocked SpringSecurityUtils
    @ConfineMetaClassChanges([SpringSecurityUtils])
    void mockOutSpringSecurityUtilsConfig() {
        def config = new ConfigObject()

        // set spring security core configuration
        config.putAll([
            authority: [nameField: 'authority', className: 'com.causecode.user.Role'],
                userLookup: [
                    userDomainClassName: 'com.causecode.user.User',
                    authorityJoinClassName: 'com.causecode.user.UserRole',
                    passwordPropertyName: 'password',
                    usernamePropertyName: 'username',
                    enabledPropertyName: 'enabled',
                    authoritiesPropertyName: 'authorities',
                    accountExpiredPropertyName: 'accountExpired',
                    accountLockedPropertyName: 'accountLocked',
                    passwordExpiredPropertyName: 'passwordExpired'
            ]
        ])

        SpringSecurityUtils.metaClass.static.getSecurityConfig = {
            config
        }
    }

    def setupSpec() {
        mockOutSpringSecurityUtilsConfig()
    }

    // Create Instance
    def createInstances(String domainName, int count) {
        count.times {
            "get${domainName}Instance"(it + 1)
        }
    }

    // Content
    Map getContentParams(int index) {
        Map contentParamsMap = [
            title: "Targeting Test $index Types and/or Phases",
            subTitle: "To execute the JUnit integration test $index",
            body: 'Grails organises tests by phase and by type. The state of the Grails application.',
            author: "Author $index",
            publish: true,
            publishedDate: new Date(),
            contentType: BlogContentType.MARKDOWN
        ]

        return contentParamsMap
    }

    // Blog
    Blog getBlogInstance(int index) {
        Blog blogInstance = new Blog(getContentParams(index))
        blogInstance.save()

        assert blogInstance.id
        assert blogInstance.toString() == "Blog ($blogInstance.title)($blogInstance.contentType)"

        return blogInstance
    }

    // Comment
    Map getCommentDataMap(int index) {
        Map commentDataMap = [
            commentText: "Comment text $index",
            email: "comment-$index@test.com",
            name: "Test user-$index",
            subject: 'Test comment subject',
            dateCreated: new Date(),
            lastUpdated: new Date() + 10
        ]

        return commentDataMap
    }

    Comment getCommentInstance(int index) {
        Comment commentInstance = new Comment(getCommentDataMap(index))
        commentInstance.save()
        assert commentInstance.id
        assert commentInstance.toString() == "Comment ($commentInstance.id)($commentInstance.commentText)"

        return commentInstance
    }

    // PageLayout Instance
    PageLayout getPageLayoutInstance(int index) {
        PageLayout pageLayoutInstance = new PageLayout([layoutName: "TestPageLayout-$index"])
        pageLayoutInstance.save()
        assert pageLayoutInstance.id
        assert pageLayoutInstance.toString() == "PageLayout ($pageLayoutInstance.id)($pageLayoutInstance.layoutName)"

        return pageLayoutInstance
    }

    // Page Instance
    Page getPageInstance(int index) {
        Page pageInstance = new Page(getContentParams(index))
        pageInstance.pageLayout = getPageLayoutInstance(index)
        pageInstance.save()
        assert pageInstance.id

        return pageInstance
    }

    // FAQ Instance
    FAQ getFAQInstance(int index) {
        FAQ faqInstance = new FAQ(getContentParams(index))
        faqInstance.save()
        assert faqInstance.id
        assert faqInstance.toString() == "FAQ ($faqInstance.title)"
        return faqInstance
    }

    // Content
    Content getContentInstance(int index) {
        Content contentInstance = new Content(getContentParams(index))
        contentInstance.save()

        assert contentInstance.id
        assert contentInstance.toString() == "$contentInstance.title"

        return contentInstance
    }

    // Meta
    Meta getMetaInstance() {
        Map args = [type: 'keywords', content: '500Hrs,TechStars,YCombinator,MassChallenge']
        Meta metaInstance = new Meta(args)
        metaInstance.save()

        assert metaInstance.id

        return metaInstance
    }

    // ContentMeta Instance
    ContentMeta getContentMetaInstance(int index) {
        Map args = [content: getContentInstance(index), meta: metaInstance]
        ContentMeta contentMetaInstance = new ContentMeta(args)
        contentMetaInstance.save()

        assert contentMetaInstance.id
        assert contentMetaInstance.toString() == "ContentMeta ($contentMetaInstance.content)($contentMetaInstance.meta)"

        return contentMetaInstance
    }
}
