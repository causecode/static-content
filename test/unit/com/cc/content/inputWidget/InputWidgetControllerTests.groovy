package com.cc.content.inputWidget



import grails.test.mixin.*

import org.junit.*

@TestFor(InputWidgetController)
@Mock(InputWidget)
class InputWidgetControllerTests {

    def populateValidParams(params) {
        assert params != null
        params.name = "Dummy name "
        params.defaultValue = "Dummy defaultValue"
        params.helpText = "Dummy helpText"

        params.type = InputWidgetType.TEXT_FIELD
        params.helpType = InputWidgetHelpType.PLACEHOLDER
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
        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/inputWidget/show/1'
        assert controller.flash.message != null
        assert InputWidget.count() == 1
    }

    void testShow() {
        populateValidParams(params)
        def inputWidget = new InputWidget(params)

        assert inputWidget.save() != null

        params.id = inputWidget.id

        controller.validate()
        def model = controller.show()

        assert model.inputWidgetInstance == inputWidget
    }

    void testEdit() {
        populateValidParams(params)
        def inputWidget = new InputWidget(params)

        assert inputWidget.save() != null

        params.id = inputWidget.id

        controller.validate()
        def model = controller.edit()

        assert model.inputWidgetInstance == inputWidget
    }

    void testUpdate() {
        populateValidParams(params)
        def inputWidget = new InputWidget(params)

        assert inputWidget.save() != null

        params.id = inputWidget.id
        params.name = "Dummy Invalid name "
        params.defaultValue = "Dummy Invalid defaultValue"
        params.helpText = "Dummy Invalid helpText"

        params.type = InputWidgetType.TEXT_FIELD
        params.helpType = InputWidgetHelpType.PLACEHOLDER

        controller.validate()
        controller.update()

        inputWidget.clearErrors()

        populateValidParams(params)
        params.id = inputWidget.id
        params.version = -1
        controller.validate()
        controller.update()

        assert view == "/inputWidget/edit"
        assert model.inputWidgetInstance != null
        assert model.inputWidgetInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        populateValidParams(params)
        def inputWidget = new InputWidget(params)

        assert inputWidget.save() != null
        assert InputWidget.count() == 1

        params.id = inputWidget.id

        controller.validate()
        controller.delete()

        assert InputWidget.count() == 0
        assert InputWidget.get(inputWidget.id) == null
        assert response.redirectedUrl == '/inputWidget/list'
    }
}
