/*
 * Copyright (c) 2016, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */
package com.causecode.content.blog

import com.causecode.content.BaseTestSetup
import com.causecode.content.ContentMeta
import com.causecode.content.ContentService
import com.causecode.content.blog.comment.BlogComment
import com.causecode.content.blog.comment.Comment
import com.causecode.content.blog.comment.CommentService
import com.causecode.user.User
import com.naleid.grails.MarkdownService
import grails.plugins.taggable.Tag
import grails.plugins.taggable.TagLink
import grails.plugins.taggable.TaggableService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.mop.ConfineMetaClassChanges

import java.util.regex.Pattern

/**
 * This is Unit test file for BlogService class.
 */
@TestFor(BlogService)
@Mock([Blog, Tag, TagLink, TaggableService, Comment, BlogComment, User, MarkdownService, ContentMeta, CommentService])
class BlogServiceSpec extends Specification implements BaseTestSetup {

    // Method getAllTags
    @ConfineMetaClassChanges([Blog, TagLink])
    void 'test getAllTags method to get list of all tags'() {
        given: 'Instances of tag'
        Tag tag1 = new Tag([name: 'tag1']).save(failOnError: true)
        Tag tag2 = new Tag([name: 'tag2']).save(failOnError: true)
        List listOfTags = [tag1, tag2]

        and: 'Mock withCriteria'
        Blog.metaClass.static.getAllTags = { ->
            [tag1, tag2]
        }

        TagLink.metaClass.static.withCriteria = { Map args, Closure closure ->
            [tag1, tag2]
        }

        when: 'getAllTags method is called'
        List resultList = service.allTags

        then: 'List of Tags should be received'
        resultList[0] == listOfTags
        resultList[1] == listOfTags
    }

    // Method findBlogContentTypeByValue
    @Unroll
    void 'test findBlogContentTypeByValue method when parameter is passed'() {
        when: 'findBlogContentTypeByValue method is called'
        BlogContentType blogContentTypeInstance = service.findBlogContentTypeByValue(parameter)

        then: 'Criteria check should be satisfied'
        blogContentTypeInstance == result

        where:
        parameter    | result
        'tinymce'    | BlogContentType.TINYMCE
        'markdown'   | BlogContentType.MARKDOWN
        'rawcontent' | BlogContentType.RAWCONTENT
    }

    // Method queryModifierBasedOnFilter
    void 'test queryModifierBasedOnFilter method when blogInstance and convertToMarkDown is passed'() {
        given: 'Instance of query, tag, monthYearFilter, queryFilter, monthFilter'
        StringBuilder query = new StringBuilder('SELECT * from user')
        String tag = 'Grails'
        Map monthYearFilters = [month: 'september', year: '1991']
        String queryFilter = 'My new Blog'
        String monthFilter = 'september'

        when: 'queryModifierBasedOnFilter method is called'
        StringBuilder resultQuery = service.queryModifierBasedOnFilter(query, tag, monthYearFilters, queryFilter,
                monthFilter)

        then: 'Valid query instance'
        resultQuery.contains('SELECT * from user, grails.plugins.taggable.TagLink tagLink WHERE b.id = ' +
                'tagLink.tagRef AND')
    }

    // Method updatedMonthFilterListBasedOnPublishedDate
    @ConfineMetaClassChanges([Blog])
    void 'test updatedMonthFilterListBasedOnPublishedDate method when monthFilterList is passed'() {
        given: 'Instance of monthFilterList'
        String month = (Calendar.instance).get(Calendar.MONTH) + 1
        List<String> monthFilterList = [month]

        and: 'Mock createCriteria'
        def customCriteria = [list: { Object params = null, Closure cls ->
            [new Date()]
        } ]

        Blog.metaClass.static.createCriteria = {
            customCriteria
        }

        when: 'updatedMonthFilterListBasedOnPublishedDate method is called'
        List<String> resultList = service.updatedMonthFilterListBasedOnPublishedDate(monthFilterList)

        then: 'List of string should be received'
        resultList.contains(month)
    }

    // Method getBlogSummaries
    @ConfineMetaClassChanges([User, String])
    void 'test getBlogInstanceList method when monthFilterList is passed'() {
        given: 'Instance of monthFilterList'
        Pattern patternTag = Pattern.compile('(?s)<p(.*?)>(.*?)<\\/p>') // HTML_PATTERN_TAG
        List<Map> blogList = [getBlogInstance(1), getBlogInstance(2)]

        and: 'Mock user and services'
        User.metaClass.encodePassword = { -> }
        Date dateOfBirth = new Date().parse('MM/dd/yyyy', '01/08/1993')
        def user = new User([
            username: 'cause-1',
            password: 'code-1',
            email: 'cause-1@code.com',
            firstName: 'Cause',
            lastName: 'Code-1',
            dateOfBirth: dateOfBirth,
            bio: 'CauseCode beyond infinity',
            pictureUrl: 'https://causecode-picture.com'
        ]).save()

        assert user.id

        service.contentService = Mock(ContentService)
        service.contentService.resolveAuthor(_) >> user

        String.metaClass.markdownToHtml = { ->
            return 'Sample text for body'
        }

        when: 'updatedMonthFilterListBasedOnPublishedDate method is called'
        List<Blog> resultList = service.getBlogSummaries(blogList, patternTag)

        then: 'Valid Response should be received'
        resultList[0].title == 'Targeting Test 1 Types and/or Phases'
        resultList[1].publish == true
        resultList[1].author.contains('User(cause-1, 1)')
    }

    void 'test getBlogInstanceList method when empty list is passed'() {
        given: 'Instance of monthFilterList'
        Pattern patternTag = Pattern.compile('(?s)<p(.*?)>(.*?)<\\/p>') // HTML_PATTERN_TAG
        Map mapInstance1 = [id: 1]
        List<Map> blogList = [mapInstance1]

        when: 'updatedMonthFilterListBasedOnPublishedDate method is called'
        List<Blog> resultList = service.getBlogSummaries(blogList, patternTag)

        then: 'Valid Response should be received'
        resultList == []
    }

    // Method getBlog
    @ConfineMetaClassChanges([BlogService, String])
    void 'test getBlog method when blogInstance and convertToMarkdown is passed'() {
        given: 'Instance of blog, comment and blogComment'
        Blog blogInstance = getBlogInstance(1)

        Comment commentInstance = getCommentInstance(1)
        BlogComment blogCommentInstance = new BlogComment([blog: blogInstance, comment: commentInstance])
        blogCommentInstance.save()

        assert blogCommentInstance.id

        and: 'Mock services and method'
        service.metaClass.getAllTags = { ->
            []
        }

        String.metaClass.markdownToHtml = { ->
            return 'Sample text for body'
        }

        when: 'getBlog method is called'
        Map result = service.getBlog(blogInstance, true)

        then: 'Valid result map should be received'
        result.blogInstance == blogInstance
    }
}
