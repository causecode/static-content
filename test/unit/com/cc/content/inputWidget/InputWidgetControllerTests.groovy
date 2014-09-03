package com.cc.content.inputWidget



import java.util.Date;

import org.junit.*

import grails.test.mixin.*

@TestFor(InputWidgetController)
@Mock(InputWidget)
class InputWidgetControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        params["name"] = 'someValidName'
        params["validation"] = 'sampleValidation'
        params["type"] = InputWidgetType.CHECKBOX
    }

    void testIndex() {
        controller.index()
        assert "/inputWidget/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.inputWidgetInstanceList.size() == 0
        assert model.inputWidgetInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.inputWidgetInstance != null
    }

    void testSave() {
        controller.save()

        assert model.inputWidgetInstance != null
        assert view == '/inputWidget/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/inputWidget/show/1'
        assert controller.flash.message != null
        assert InputWidget.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/inputWidget/list'

        populateValidParams(params)
        def inputWidget = new InputWidget(params)

        assert inputWidget.save() != null

        params.id = inputWidget.id

        def model = controller.show()

        assert model.inputWidgetInstance == inputWidget
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/inputWidget/list'

        populateValidParams(params)
        def inputWidget = new InputWidget(params)

        assert inputWidget.save() != null

        params.id = inputWidget.id

        def model = controller.edit()

        assert model.inputWidgetInstance == inputWidget
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/inputWidget/list'

        response.reset()

        populateValidParams(params)
        def inputWidget = new InputWidget(params)

        assert inputWidget.save() != null

        // test invalid parameters in update
        params.id = inputWidget.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/inputWidget/edit"
        assert model.inputWidgetInstance != null

        inputWidget.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/inputWidget/show/$inputWidget.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        inputWidget.clearErrors()

        populateValidParams(params)
        params.id = inputWidget.id
        params.version = -1
        controller.update()

        assert view == "/inputWidget/edit"
        assert model.inputWidgetInstance != null
        assert model.inputWidgetInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.validate()
//        controller.inputWidgetInstance = controller.inputWidgetInstanceList.get(0)
//        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/inputWidget/list'

        response.reset()

        populateValidParams(params)
        def inputWidget = new InputWidget(params)

        assert inputWidget.save() != null
        assert InputWidget.count() == 1

        params.id = inputWidget.id
        controller.delete()

        assert InputWidget.count() == 0
        assert InputWidget.get(inputWidget.id) == null
        assert response.redirectedUrl == '/inputWidget/list'
    }
}
