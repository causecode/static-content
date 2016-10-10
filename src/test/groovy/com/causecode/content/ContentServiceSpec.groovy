/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content

import com.causecode.BaseTestSetup
import com.causecode.content.blog.Blog
import com.causecode.content.blog.BlogContentType
import com.causecode.content.blog.comment.BlogComment
import com.causecode.content.meta.Meta
import com.causecode.content.page.Page
import com.causecode.content.page.PageRevision
import com.causecode.seo.friendlyurl.FriendlyUrlService
import com.causecode.user.User
import grails.core.DefaultGrailsApplication
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugins.taggable.Tag
import grails.plugins.taggable.TagLink
import grails.plugins.taggable.TaggableService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.util.Holders
import grails.web.mapping.LinkGenerator
import org.grails.core.DefaultGrailsDomainClass
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(ContentService)
@Mock([Content, SpringSecurityService, Meta, ContentMeta, ContentRevision, Blog, Tag,
        Page, PageRevision, User, TaggableService, TagLink, BlogComment])
class ContentServiceSpec extends Specification implements BaseTestSetup {

    def setup() {
        mockOutSpringSecurityUtilsConfig()
    }

    // Method resolveAuthor
    @Unroll
    void "test resolveAuthor method when contentInstance and authorProperty is passed"() {
        given: 'Instance of content and authorProperty'
        Content contentInstance1 = getContentInstance(1)
        contentInstance1.author = '1'
        contentInstance1.save(com_causecode_BaseTestSetup__FLUSH_TRUE)

        assert contentInstance1.id

        String authorProperty = authorParameter

        and: 'Mock user'
        User.metaClass.encodePassword = { -> }
        Date dateOfBirth = new Date().parse('MM/dd/yyyy', '01/08/1993')
        def user = new User([
                        username: "cause-1",
                        password: "code-1",
                        email: "cause-1@code.com",
                        firstName: 'Cause',
                        lastName: "Code-1",
                        dateOfBirth: dateOfBirth,
                        bio: 'CauseCode beyond infinity',
                        pictureUrl: 'https://causecode-picture.com'
        ]).save(com_causecode_BaseTestSetup__FLUSH_TRUE)

        assert user.id

        when: 'resolveAuthor method is called'
        String result = service.resolveAuthor(contentInstance1, authorProperty)

        then:  'Result should pass the criteria check'
        result == validResult

        where:
        authorParameter | validResult
        'fullName' | 'Cause Code-1'
        'lastName' | 'Code-1'
        'email' | 'cause-1@code.com'
    }

    // Method isVisible
    void "test isVisible method when id is passed when contentManager is true"() {
        given: 'Content instance'
        Content contentInstance = getContentInstance(1)

        and: 'Mock isContentManager method'
        Holders.grailsApplication.config.cc.plugins.content.contentManagerRole = 'manager'
        SpringSecurityUtils.metaClass.static.ifAnyGranted = { String role ->
            return true
        }

        when: 'isVisible method is called'
        boolean boolResult = service.isVisible(contentInstance.id)

        then: 'Valid result should be received'
        boolResult == true
    }

    void "test isVisible method when id is passed"() {
        given: 'Content instance'
        Content contentInstance = getContentInstance(1)

        and: 'Mock isContentManager method and withCriteria'
        Content.metaClass.static.withCriteria = { Map args, Closure closure ->
            return contentInstance
        }
        service.metaClass.isContentManager = { -> return false }

        when: 'isVisible method is called'
        boolean boolResult = service.isVisible(contentInstance.id)

        then: 'Valid result should be received'
        boolResult == true
    }

    // Method create
    void "test create method"() {
        given: 'Instance of map, mataTypes and metaValues'
        Map args = getContentParams(1)
        List metaTypes = []
        List metaValues = []

        when: 'create method is called'
        Content content = service.create(args, metaTypes, metaValues)

        then: 'Valid response should be received'
        content.title == 'Targeting Test 1 Types and/or Phases'
        content.author == 'Author 1'
    }

    // Method update
    void "test method update for errors"() {
        given: 'content instance'
        Content contentInstance = new Content()
        Map args = [:]
        List metaTypes = []
        List metaValues = []

        when: 'update method is called'
        Content contentResult = service.update(args, contentInstance, metaTypes, metaValues)

        then: 'Valid response should be received'
        contentResult.id == null
        contentResult.hasErrors() == true
        contentResult.errors.getFieldError("title").toString().contains("rejected value [null]")
    }

    void "test update method to remove all metas"() {
        given: 'Instance of contentMeta, contentInstance, map, metaTypes and metaValues'
        ContentMeta contentMetaInstance1 = getContentMetaInstance(1)//new ContentMeta()
        ContentMeta contentMetaInstance2 = getContentMetaInstance(2)
        Content contentInstance = contentMetaInstance1.content//new Content()
        Map args = getContentParams(1)
        Meta metaInstance1 = getMetaInstance()
        Meta metaInstance2 = getMetaInstance()
        List metaTypes = [metaInstance1.type, metaInstance2.type]
        List metaValues = [metaInstance1.content, metaInstance2.content]

        and: 'Mock ContentMeta withCriteria'
        ContentMeta.metaClass.static.withCriteria = { Closure closure ->
            return contentMetaInstance2.meta
        }

        ContentMeta.metaClass.static.withCriteria = { Map args1, Closure closure ->
            metaInstance1.delete(com_causecode_BaseTestSetup__FLUSH_TRUE)
            metaInstance2.delete(com_causecode_BaseTestSetup__FLUSH_TRUE)
            return contentMetaInstance2.meta
        }

        when: 'update method is called'
        Content resultContentInstance = service.update(args, contentInstance, metaTypes, metaValues)

        then: 'Valid response should be received'
        Meta.count() == 2
        resultContentInstance.title == 'Targeting Test 1 Types and/or Phases'
    }

    // Method delete
    void "test delete method for deleting content"() {
        given: 'Content instance'
        Content contentInstance = getContentInstance(1)

        when: 'delete method is called'
        Content.count == 1
        service.delete(contentInstance)

        then: 'Content instance should be deleted'
        Content.count() == 0
    }

    void "test delete method for deleting both content and related blog"() {
        given: 'Blog instance'
        Blog blogInstance = getBlogInstance(1)

        when: 'delete method is called'
        Blog.count() == 1
        Content.count == 1
        service.delete(blogInstance)

        then: 'Blog and related content should be deleted'
        Content.count() == 0
        Blog.count() == 0
    }

    // Method CreateRevision
    void "test createRevision method when contentInstance, clazz and params are passed"() {
        given: 'Content instance'
        Map params = [revisionComment: 'hi']
        Content contentInstance = getContentInstance(1)

        when: 'createRevision method is called'
        ContentRevision resultContentRevision = service.createRevision(contentInstance, ContentRevision, params)

        then: 'Valid response should be received'
        ContentRevision.count() == 1
        resultContentRevision.title == 'Targeting Test 1 Types and/or Phases'
        resultContentRevision.comment == 'hi'
    }

    void "test createLink method when map attributes is passed"() {
        given: 'Content instance'
        Content contentInstance = getContentInstance(1)
        String domain = 'com.causecode.content.Content'
        Map attrs = [domain: domain, id: 0, absolute: true]

        and: 'Mocked Services'
        service.metaClass.getShorthandAnnotatedControllers = { ->return [] }

        service.friendlyUrlService = Mock(FriendlyUrlService)
        service.friendlyUrlService.sanitizeWithDashes(_) >> 'sanitized-title'

        and: 'Mock LinkGenerator'
        service.grailsLinkGenerator = Mock(LinkGenerator)
        service.grailsLinkGenerator.link(_) >> '/blog/list'

        when: 'createLink method is called'
        String result = service.createLink(attrs)

        then: 'Valid response should be received'
        result == '/blog/list'

        when: 'createLink method is called'
        Blog blogInstance = getBlogInstance(1)
        attrs = [domain: domain, id: blogInstance.id, absolute: true]
        String result1 = service.createLink(attrs)

        then: 'Valid response should be received'
        result1 == '/blog/list'
    }
}
