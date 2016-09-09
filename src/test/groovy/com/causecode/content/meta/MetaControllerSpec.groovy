package com.causecode.content.meta

import com.causecode.content.ContentMeta
import com.causecode.content.ContentRevision
import com.causecode.content.ContentService
import grails.plugin.springsecurity.SpringSecurityService
import com.causecode.content.Content
import com.causecode.content.page.Page
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.*
import spock.lang.Specification

import javax.xml.bind.DatatypeConverterInterface

@TestMixin(GrailsUnitTestMixin)
@TestFor(MetaController)
@Mock([Meta, Content, Page, ContentService, ContentRevision, SpringSecurityService, ContentMeta])
class MetaControllerSpec extends Specification {
    MetaController controller
    @Shared
    Meta metaInstance
    def output

    def setup() {
        controller = new MetaController()
        metaInstance = new Meta(getContentParams(1))
        assert metaInstance.save()
        assert metaInstance.id
    }

    void "VALIDATE: Testing validate action"() {
        given: "populating parameters"
        controller.params.id = metaInstance.id
        when: "page ID Parameter not passed"
        controller.params.id = false
        controller.validate()
        then: "Should get view equals dummy"
        view == "/meta/dummy"
    }

    void "VALIDATE: Testing: metaInstance equals null and makes Ajax Request"() {
        given: "Populating parameters"
        controller.params.id = metaInstance.id
        when: "page ID Parameter  passed and makes Ajax Request"
        controller.params.id = true
        controller.request.makeAjaxRequest()
        controller.validate()
        then: "Should get required view not equals null"
        view != null
        view == "/meta/dummy"
    }

    void "VALIDATE: Testing: metaInstance equals null and has no AjaxRequest"() {
        given: "Populating parameters"
        controller.params.id = metaInstance.id
        when: "page ID Parameter passed but no Ajax request"
        controller.params.id = true
        controller.validate()
        then: "Should redirect to list page"
        flash.message != null
        response.redirectedUrl == "/meta/list"

    }
//    void "VALIDATE: Testing: metaInstance not equals null then return true"() {
//        given: "Populating Parameters"
//        controller.params.id = metaInstance.id
//        when: "page ID parameter passed"
//        controller.params.id = true
//        controller.validate()
//        then:
//
//    }

    void "DELETE: Testing Delete action"() {
        given: "Populating Parameters"
        controller.params.id = metaInstance.id
        when: "Page ID Parameters not passed "
        controller.params.id = false
        controller.deleteMeta()
        then: "Should get required view not equals null"
        view != null
        view == "/meta/dummy"
    }

    void "DELETE: Testing if pageInstance not equals null"() {
        given: "Populating parameters"
        controller.params.id = metaInstance.id
        metaInstance.save()
        when: "page ID parameter passed"
        controller.params.id = true
        controller.deleteMeta()
        then: "Should get the required view not equals null"
        view != null
        view == "/meta/dummy"
    }

    private Map getContentParams(Integer i) {
        return [
                type       : "Java",
                value      : "Javascript",
                dateCreated: '2016-05-08',
                lastUpdated: '2016-09-18'
        ]
    }
}