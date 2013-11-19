package com.cc.content.meta



import org.junit.*

import com.cc.content.Content;
import com.cc.content.ContentMeta;

import grails.test.mixin.*

@TestFor(MetaController)
@Mock([Meta, Content, ContentMeta])
class MetaControllerTests {

    def populateValidParams(params) {
        assert params != null
        params.type = "KEYWORDS"
        params.value = "keywords"
    }


    void testDeleteWithId() {
        populateValidParams(params)
        def meta = new Meta(params)

        assert meta.save() != null
        assert Meta.count() == 1

        params.id = meta.id

        controller.validate()
        controller.deleteMeta()

        assert Meta.count() == 0
        assert Meta.get(meta.id) == null
        assert response.text == "dummy"
    }

    void testDeleteWithoutId() {
        populateValidParams(params)
        controller.validate()
        controller.deleteMeta()

        assert response.text == "dummy"
        assert Meta.count() == 0
    }

    void testDeleteWithIdAndContentMeta() {

        populateValidParams(params)

        Content contentInstance = new Content(body: "dummy Body" , title: "Dummy title")
        assert contentInstance.save()

        def meta = new Meta(params)

        assert meta.save() != null
        assert Meta.count() == 1

        ContentMeta.findOrSaveByContentAndMeta(contentInstance, meta)

        assert ContentMeta.count() == 1
        controller.validate()
        controller.deleteMeta()

        assert response.text == "dummy"
        assert Meta.count() == 1
        assert ContentMeta.count() == 1
    }
}
