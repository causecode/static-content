package com.cc.content.faq



import org.junit.*
import grails.test.mixin.*

@TestFor(FAQController)
@Mock(FAQ)
class FAQControllerTests {

    def populateValidParams(params) {
        assert params != null
    }

    void testIndex() {
        controller.index()
        assert "/FAQ/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.FAQInstanceList.size() == 0
        assert model.FAQInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.FAQInstance != null
    }

    void testSave() {
        controller.save()

        assert model.FAQInstance != null
        assert view == '/FAQ/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/FAQ/show/1'
        assert controller.flash.message != null
        assert FAQ.count() == 1
    }

    void testShow() {
        populateValidParams(params)
        def FAQ = new FAQ(params)

        assert FAQ.save() != null

        params.id = FAQ.id

        controller.validate()
        def model = controller.show()

        assert model.FAQInstance == FAQ
    }

    void testEdit() {
        populateValidParams(params)
        def FAQ = new FAQ(params)

        assert FAQ.save() != null

        params.id = FAQ.id

        controller.validate()
        def model = controller.edit()

        assert model.FAQInstance == FAQ
    }

    void testUpdate() {
        populateValidParams(params)
        def FAQ = new FAQ(params)

        assert FAQ.save() != null

        params.id = FAQ.id

        controller.validate()
        controller.update()

        assert view == "/FAQ/edit"
        assert model.FAQInstance != null

        FAQ.clearErrors()

        populateValidParams(params)
        controller.validate()
        controller.update()

        assert response.redirectedUrl == "/FAQ/show/$FAQ.id"
        assert flash.message != null

        response.reset()
        FAQ.clearErrors()

        populateValidParams(params)
        params.id = FAQ.id
        params.version = -1
        controller.validate()
        controller.update()

        assert view == "/FAQ/edit"
        assert model.FAQInstance != null
        assert model.FAQInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        populateValidParams(params)
        def FAQ = new FAQ(params)

        assert FAQ.save() != null
        assert FAQ.count() == 1

        params.id = FAQ.id

        controller.validate()
        controller.delete()

        assert FAQ.count() == 0
        assert FAQ.get(FAQ.id) == null
        assert response.redirectedUrl == '/FAQ/list'
    }
}
