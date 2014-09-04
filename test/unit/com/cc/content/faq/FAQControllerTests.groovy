package com.cc.content.faq



import org.junit.*

import grails.test.mixin.*

@TestFor(FAQController)
@Mock(FAQ)
class FAQControllerTests {

    def populateValidParams(params) {
        assert params != null
        params["title"] = 'sampleTitle'
        params["body"] = 'sampleBody'
        params["publish"] = true
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
        controller.validate()

        assert flash.message != null
        assert response.redirectedUrl == '/FAQ/list'

        populateValidParams(params)
        def FAQ = new FAQ(params)

        assert FAQ.save(flush: true) != null

        params.id = FAQ.id
        controller.validate()

        def model = controller.show()

        assert model.FAQInstance == FAQ
    }

    void testEdit() {
        controller.validate()
        assert flash.message != null
        assert response.redirectedUrl == '/FAQ/list'

        populateValidParams(params)
        def FAQ = new FAQ(params)

        assert FAQ.save(flush: true) != null

        params.id = FAQ.id
        controller.validate()
        def model = controller.edit()

        assert model.FAQInstance == FAQ
    }

    void testUpdate() {
        controller.validate()
        assert flash.message != null
        assert response.redirectedUrl == '/FAQ/list'

        response.reset()

        populateValidParams(params)
        def FAQ = new FAQ(params)

        assert FAQ.save(flush: true) != null

        populateValidParams(params)
        params.id = FAQ.id
        controller.validate()
        controller.update()

        assert response.redirectedUrl == "/FAQ/show/$FAQ.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        FAQ.clearErrors()

        populateValidParams(params)
        params.id = FAQ.id
        params.version = -1
        controller.update()

        assert view == "/FAQ/edit"
        assert model.FAQInstance != null
        assert model.FAQInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.validate()
        assert flash.message != null
        assert response.redirectedUrl == '/FAQ/list'

        response.reset()

        populateValidParams(params)
        def FAQ = new FAQ(params)

        assert FAQ.save() != null
        assert FAQ.count() == 1


        assert FAQ.save(flush: true) != null

        params.id = FAQ.id
        controller.validate()

        controller.delete()

        assert FAQ.count() == 0
        assert FAQ.get(FAQ.id) == null
        assert response.redirectedUrl == '/FAQ/list'
    }
}
