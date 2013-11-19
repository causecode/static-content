package com.cc.content


import grails.test.mixin.*

import org.junit.*

@TestFor(ContentRevisionController)
@Mock([Content, ContentRevision])
class ContentRevisionControllerTests {

    void populateValidParams(params) {
        assert params != null
        params.title = "Dummy Title"
        params.subTitle = "Dummy SubTitle"
        params.body = "Dummy Body"

    }

    Content createContent() {
        Content contentInstance = new Content(title: "Dummy Title", subTitle: "Dummy SubTitle", body: "Dummy Body",
        author:"Laxmi")

        assert contentInstance.save() != null
        return contentInstance
    }

    void testShow() {
        populateValidParams(params)
        def contentRevision = new ContentRevision(params)
        contentRevision.revisionOf = createContent()

        assert contentRevision.save() != null

        params.id = contentRevision.id

        def model = controller.show()

        assert model.contentRevisionInstance == contentRevision
    }

    void testDelete() {
        populateValidParams(params)
        def contentRevision = new ContentRevision(params)
        contentRevision.revisionOf = createContent()

        assert contentRevision.save() != null
        assert ContentRevision.count() == 1

        params.id = contentRevision.id

        controller.delete()

        assert ContentRevision.count() == 0
        assert ContentRevision.get(contentRevision.id) == null
    }

    void testLoad() {
        populateValidParams(params)
        def contentRevision = new ContentRevision(params)
        contentRevision.revisionOf = createContent()

        assert contentRevision.save() != null
        assert ContentRevision.count() == 1

        params.id = contentRevision.id

        def model = controller.load()

        assert "Dummy Title" == response.json.title
        assert "Dummy SubTitle" == response.json.subTitle
        assert "Dummy Body" == response.json.body
    }
}
