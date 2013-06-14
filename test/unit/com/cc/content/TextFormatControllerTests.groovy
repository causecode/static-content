package com.cc.content



import org.junit.*
import grails.test.mixin.*

@TestFor(TextFormatController)
@Mock(TextFormat)
class TextFormatControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/textFormat/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.textFormatInstanceList.size() == 0
        assert model.textFormatInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.textFormatInstance != null
    }

    void testSave() {
        controller.save()

        assert model.textFormatInstance != null
        assert view == '/textFormat/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/textFormat/show/1'
        assert controller.flash.message != null
        assert TextFormat.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/textFormat/list'

        populateValidParams(params)
        def textFormat = new TextFormat(params)

        assert textFormat.save() != null

        params.id = textFormat.id

        def model = controller.show()

        assert model.textFormatInstance == textFormat
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/textFormat/list'

        populateValidParams(params)
        def textFormat = new TextFormat(params)

        assert textFormat.save() != null

        params.id = textFormat.id

        def model = controller.edit()

        assert model.textFormatInstance == textFormat
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/textFormat/list'

        response.reset()

        populateValidParams(params)
        def textFormat = new TextFormat(params)

        assert textFormat.save() != null

        // test invalid parameters in update
        params.id = textFormat.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/textFormat/edit"
        assert model.textFormatInstance != null

        textFormat.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/textFormat/show/$textFormat.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        textFormat.clearErrors()

        populateValidParams(params)
        params.id = textFormat.id
        params.version = -1
        controller.update()

        assert view == "/textFormat/edit"
        assert model.textFormatInstance != null
        assert model.textFormatInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/textFormat/list'

        response.reset()

        populateValidParams(params)
        def textFormat = new TextFormat(params)

        assert textFormat.save() != null
        assert TextFormat.count() == 1

        params.id = textFormat.id

        controller.delete()

        assert TextFormat.count() == 0
        assert TextFormat.get(textFormat.id) == null
        assert response.redirectedUrl == '/textFormat/list'
    }
}
