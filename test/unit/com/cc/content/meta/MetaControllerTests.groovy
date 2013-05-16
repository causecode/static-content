package com.cc.content.meta



import org.junit.*
import grails.test.mixin.*

@TestFor(MetaController)
@Mock(Meta)
class MetaControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/meta/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.metaInstanceList.size() == 0
        assert model.metaInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.metaInstance != null
    }

    void testSave() {
        controller.save()

        assert model.metaInstance != null
        assert view == '/meta/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/meta/show/1'
        assert controller.flash.message != null
        assert Meta.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/meta/list'

        populateValidParams(params)
        def meta = new Meta(params)

        assert meta.save() != null

        params.id = meta.id

        def model = controller.show()

        assert model.metaInstance == meta
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/meta/list'

        populateValidParams(params)
        def meta = new Meta(params)

        assert meta.save() != null

        params.id = meta.id

        def model = controller.edit()

        assert model.metaInstance == meta
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/meta/list'

        response.reset()

        populateValidParams(params)
        def meta = new Meta(params)

        assert meta.save() != null

        // test invalid parameters in update
        params.id = meta.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/meta/edit"
        assert model.metaInstance != null

        meta.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/meta/show/$meta.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        meta.clearErrors()

        populateValidParams(params)
        params.id = meta.id
        params.version = -1
        controller.update()

        assert view == "/meta/edit"
        assert model.metaInstance != null
        assert model.metaInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/meta/list'

        response.reset()

        populateValidParams(params)
        def meta = new Meta(params)

        assert meta.save() != null
        assert Meta.count() == 1

        params.id = meta.id

        controller.delete()

        assert Meta.count() == 0
        assert Meta.get(meta.id) == null
        assert response.redirectedUrl == '/meta/list'
    }
}
