package com.cc.content



import org.junit.*
import grails.test.mixin.*

@TestFor(PageLayoutController)
@Mock(PageLayout)
class PageLayoutControllerTests {

    def populateValidParams(params) {
        assert params != null
        params["layoutName"] = 'sampleLayoutName'
    }

    void testIndex() {
        controller.index()
        assert "/pageLayout/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.pageLayoutInstanceList.size() == 0
        assert model.pageLayoutInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.pageLayoutInstance != null
    }

    void testSave() {
        controller.save()

        assert model.pageLayoutInstance != null
        assert view == '/pageLayout/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/pageLayout/show/1'
        assert controller.flash.message != null
        assert PageLayout.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/pageLayout/list'

        populateValidParams(params)
        def pageLayout = new PageLayout(params)

        assert pageLayout.save() != null

        params.id = pageLayout.id

        def model = controller.show()

        assert model.pageLayoutInstance == pageLayout
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/pageLayout/list'

        populateValidParams(params)
        def pageLayout = new PageLayout(params)

        assert pageLayout.save() != null

        params.id = pageLayout.id

        def model = controller.edit()

        assert model.pageLayoutInstance == pageLayout
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/pageLayout/list'

        response.reset()

        populateValidParams(params)
        def pageLayout = new PageLayout(params)

        assert pageLayout.save() != null

        params.id = pageLayout.id
        controller.update()

        assert response.redirectedUrl == "/pageLayout/show/$pageLayout.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        pageLayout.clearErrors()

        populateValidParams(params)
        params.id = pageLayout.id
        params.version = -1
        controller.update()

        assert view == "/pageLayout/edit"
        assert model.pageLayoutInstance != null
        assert model.pageLayoutInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/pageLayout/list'

        response.reset()

        populateValidParams(params)
        def pageLayout = new PageLayout(params)

        assert pageLayout.save() != null
        assert PageLayout.count() == 1

        params.id = pageLayout.id

        controller.delete()

        assert PageLayout.count() == 0
        assert PageLayout.get(pageLayout.id) == null
        assert response.redirectedUrl == '/pageLayout/list'
    }
}
