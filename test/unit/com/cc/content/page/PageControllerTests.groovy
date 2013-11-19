package com.cc.content.page

import grails.test.mixin.*
import grails.test.mixin.support.*

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.junit.*

import com.cc.content.Content
import com.cc.content.ContentRevision
import com.cc.content.ContentService
import com.cc.content.PageLayout

@TestMixin(GrailsUnitTestMixin)
@TestFor(PageController)
@Mock([Page, PageRevision, Content, PageLayout])
class PageControllerTests {

    def contentServiceMock
    def SpringSecurityUtilsMock
    def contentServiceForRivisionMock

    void setUp() {
        // Setup logic here
        def contentService = mockFor(ContentService)
        contentServiceMock.demand.contentManager { -> false }   // mocking content service method
        controller.contentService = contentServiceMock.createMock()

        def SpringSecurityUtils = mockFor(SpringSecurityUtils)
        SpringSecurityUtilsMock.demand.ifNotGranted { role -> true}
        controller.SpringSecurityUtils = SpringSecurityUtilsMock.createMock()
    }

    def populateValidParams(params) {
        assert params != null
        params.title = "Dummy Title"
        params.subTitle = "Dummy SubTitle"
        params.body = "Dummy Body"
        params.publish = true
        PageLayout pageLayoutInstance = createPageLayout()
        params['pageLayout'] = [:]
        params['pageLayout'].id = pageLayoutInstance.id
    }

    PageLayout createPageLayout() {
        PageLayout pageLayoutInstance = new PageLayout(layoutName:"Dummy layout")

        assert pageLayoutInstance.save() != null
        assert PageLayout.count() == 1
        assert PageLayout.get(1) == pageLayoutInstance
    }

    Content createContent() {
        populateValidParams(params)
        Content contentInstance = new Content(params)

        assert contentInstance.save() != null
        return contentInstance
    }

    void testIndex() {
        controller.index()
        assert "/page/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.pageInstanceList.size() == 0
        assert model.pageInstanceTotal == 0

    }

    void testCreate() {
        def model = controller.create()

        assert model.pageInstance != null
    }

    void testSave() {

        Content contentInstance = createContent()
        def contentService = mockFor(ContentService)
        contentServiceMock.demand.create { -> contentInstance }   // mocking content service method
        controller.contentService = contentServiceMock.createMock()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/page/show/1'
        assert controller.flash.message != null
        assert Page.count() == 1
    }

    void testShow() {
        populateValidParams(params)
        Page page = new Page(params)

        assert page.save() != null

        params.id = page.id

        controller.validate()
        def model = controller.show()

        assert model.pageInstance.id == page.id
    }

    void testEdit() {

        populateValidParams(params)
        Page page = new Page(params)

        assert page.save() != null

        params.id = page.id

        populateValidParams(params)
        def contentRevision = new ContentRevision(params)

        assert contentRevision.save() != null
        assert ContentRevision.count() == 1

        contentRevision.revisionOf = page.id

        controller.validate()
        controller.edit()

        assert model.pageInstance.id == page.id
        assert contentRevision.count() == 1
    }

    void testDelete() {

        populateValidParams(params)
        Page page = new Page(params)

        assert page.save() != null

        params.id = page.id

        def contentService = mockFor(ContentService)
        contentServiceMock.demand.delete { -> true }   // mocking content service method
        controller.contentService = contentServiceMock.createMock()

        populateValidParams(params)
        controller.delete()

        assert response.redirectedUrl == '/page/list'
        assert controller.flash.message != null
        assert Page.count() == 0
    }

    void testUpdateWithoutRevision() {

        populateValidParams(params)
        Page page = new Page(params)

        assert page.save() != null

        params.id = page.id

        Content contentInstance = createContent()

        def contentService = mockFor(ContentService)
        contentServiceMock.demand.update { -> contentInstance }   // mocking content service method
        controller.contentService = contentServiceMock.createMock()

        controller.validate()
        controller.update()

        assert response.redirectedUrl == '/page/show'
        assert controller.flash.message == "Page updated successfully"
        assert Page.count() == 1
    }

    ContentRevision createContentRevision() {
        populateValidParams(params)
        ContentRevision contentRevisionInstance = new ContentRevision(params)

        assert contentRevisionInstance.save() != null
        return contentRevisionInstance
    }

    void testUpdateWithRevision() {

        populateValidParams(params)
        Page page = new Page(params)

        assert page.save() != null

        params.id = page.id

        params.createRevision = true

        Content contentInstance = createContent()

        def contentService = mockFor(ContentService)
        contentServiceMock.demand.update { -> contentInstance }   // mocking content service method
        controller.contentService = contentServiceMock.createMock()

        ContentRevision contentRevisionInstance = createContentRevision()

        def contentServiceForRivision = mockFor(ContentService)
        contentServiceForRivisionMock.demand.createRevision { -> contentRevisionInstance }   // mocking content service method
        controller.contentService = contentServiceForRivisionMock.createMock()

        controller.validate()
        controller.update()

        assert response.redirectedUrl == '/page/show'
        assert controller.flash.message == "Revision created successfully"
        assert Page.count() == 1
    }
}
