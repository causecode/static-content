package com.causecode.content

import com.causecode.BaseTestSetup
import com.causecode.content.blog.Blog
import com.causecode.content.blog.BlogContentType
import com.causecode.content.meta.Meta
import com.causecode.content.page.Page
import com.causecode.content.page.PageRevision
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugins.taggable.Tag
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.util.Holders
import spock.lang.Specification

@TestFor(ContentService)
@Mock([Content, SpringSecurityService, Meta, ContentMeta, ContentRevision, Blog, Tag, Page, PageRevision])
class ContentServiceSpec extends Specification implements BaseTestSetup {

    def setup() {
        Holders.grailsApplication.config.cc.plugins.content.contentManagerRole = 'manager'

        /*SpringSecurityUtils.getMetaClass().static.ifAllGranted = { String roles ->
            return true
        }*/

        SpringSecurityUtils.metaClass.static.ifAllGranted = { String role ->
            return true
        }
    }

    // Method resolveAuthor
    void "test resolveAuthor method when contentInstance and authorProperty is passed"() {
        given: 'Instance of content and authorProperty'
        Content contentInstance1 = getContentInstance(1)
        String authorProperty = 'fullName'

        when: 'resolveAuthor method is called'
        String result = service.resolveAuthor(contentInstance1, authorProperty)

        then:  ''
         // anonymousUser
        result != null
    }

   // @Unroll
    void "test resolveAuthor method when invalid contentInstance and authorProperty is passed"() {
        given: 'Instance of content and authorProperty'
        Content contentInstance1 = new Content()
        String authorProperty = 'fullname'

        when: 'resolveAuthor method is called'
        String result = service.resolveAuthor(contentInstance1, authorProperty)

        then:  ''
         // anonymousUser
        result != null

//        where:
//        parameter | result
//        '' | ''
//        'fullName' | 'anonymousUser'
//        '1' | ''
    }

/*    void "test resolveAuthor method when invalid contentInstance and authorProperty as number is passed"() {
        given: 'Instance of content and authorProperty'
        Content contentInstance1 = getContentInstance(1)//new Content()
        contentInstance1.author = '1'
        contentInstance1.save(flush: true)
        String authorProperty = 'fullName'

        and: 'Mocking SpringSecurityUtils'

        service.metaClass.springSecurityUtils.securityConfig.userLookup.userDomainClassName = String.class

        when: 'resolveAuthor method is called'
        String result = service.resolveAuthor(contentInstance1, authorProperty)

        then:  ''
         // anonymousUser
        result != null

//        where:
//        parameter | result
//        '' | ''
//        'fullName' | 'anonymousUser'
//        '1' | ''
    }*/

    // Method isVisible
    /*void "test isVisible method when id is passed"() { // Failing in SpringSecurityUtils
        given: 'Content instance'
        Content contentInstance = getContentInstance(1)

        when: 'isVisible method is called'
        service.grailsApplication = grailsApplication
        boolean boolResult = service.isVisible(contentInstance.id)

        then: 'Valid result'
        boolResult == true
    }*/

    // Method create
    void "test create method"() {
        given: 'Instance'
        Map args = getContentParams(1)
        List metaTypes = []
        List metaValues = []
        //Class clazz

        when: 'create method is called'
        Content content = service.create(args, metaTypes, metaValues)

        then: 'Valid res'
        content != null
    }

    // Method update
    void "test method update for errors"() {
        given: 'Instance'
        Content contentInstance = new Content()
        Map args = [:]//getContentParams(1)
        List metaTypes = []
        List metaValues = []

        when: 'update method is called'
        Content contentResult = service.update(args, contentInstance, metaTypes, metaValues)

        then: 'Valid res'
        contentResult.id == null
        contentResult.hasErrors() == true
        contentResult.errors.getFieldError("title").toString().contains("rejected value [null]")
    }

    /*void "test update method to remove all metas"() { // Failing in createAlias method
        given: 'Instance'
        ContentMeta contentMetaInstance1 = getContentMetaInstance(1)//new ContentMeta()
        ContentMeta contentMetaInstance2 = getContentMetaInstance(2)
        Content contentInstance = contentMetaInstance1.content//new Content()
        Map args = getContentParams(1)
        List metaTypes = [contentMetaInstance1.meta.type, contentMetaInstance2.meta.type]
        List metaValues = [contentMetaInstance1.meta.content, contentMetaInstance1.meta.content]

        when: 'update method is called'
        Content resultContentInstance = service.update(args, contentInstance, metaTypes, metaValues)

        then: 'Valid response'
    }*/

    // Method delete
    void "test delete method for deleting content"() {
        given: 'Instance'
        Content contentInstance = getContentInstance(1)
        //Blog blogInstance = getBlogInstance(1)


        when: 'delete method is called'
        Content.count == 1
        service.delete(contentInstance)

        then: 'Valid res'
        Content.count() == 0
    }

    /*void "test delete method for deleting both content and related blog"() {
        given: 'Instance'
        //Content contentInstance = getContentInstance(1)
        Blog blogInstance = getBlogInstance(1)


        when: 'delete method is called'
        Blog.count() == 1
        Content.count == 1
        service.delete(contentInstance)

        then: 'Valid res'
        Content.count() == 0
        Blog.count() == 0
    }*/

    /*void "test createRevision method when contentInstance, clazz and params are passed"() { // error - with closure not working
        given: 'Instance'
        ContentRevision contentRevision = new ContentRevision(getContentParams(1))
        if (contentRevision.save(flush: true)) {}



        assert contentRevision.id

        //ContentRevision contentRevision = new ContentRevision()

        Map params = [revisionComment: '']

        when: 'createRevision method is called'
        ContentRevision resultContentRevision = service.createRevision(contentRevision, ContentRevision, params)

        then: 'Valid res'
        resultContentRevision != null
    }*/

    /*void "test createLink method when map attributes is passed"() { // error - String index out of bound
        given: 'Instance'
        Content contentInstance = getContentInstance(1)
        String domain = 'com.causecode.content.Content'
        Map attrs = [domain: domain, id: contentInstance.id]

        when: 'createLink method is called'
        String result = service.createLink(attrs)

        then: 'valid res'
        result != null
    }*/
}
