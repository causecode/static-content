package com.cc.content.news



import org.junit.*
import grails.test.mixin.*

@TestFor(NewsController)
@Mock(News)
class NewsControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/news/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.newsInstanceList.size() == 0
        assert model.newsInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.newsInstance != null
    }

    void testSave() {
        controller.save()

        assert model.newsInstance != null
        assert view == '/news/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/news/show/1'
        assert controller.flash.message != null
        assert News.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/news/list'

        populateValidParams(params)
        def news = new News(params)

        assert news.save() != null

        params.id = news.id

        def model = controller.show()

        assert model.newsInstance == news
    }

    void testEdit() {
//        controller.edit()
//
//        assert flash.message != null
//        assert response.redirectedUrl == '/news/list'

        populateValidParams(params)
        def news = new News(params)

        assert news.save() != null

        params.id = news.id

        def model = controller.edit()

        assert model.newsInstance == news
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/news/list'

        response.reset()

        populateValidParams(params)
        def news = new News(params)

        assert news.save() != null

        // test invalid parameters in update
        params.id = news.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/news/edit"
        assert model.newsInstance != null

        news.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/news/show/$news.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        news.clearErrors()

        populateValidParams(params)
        params.id = news.id
        params.version = -1
        controller.update()

        assert view == "/news/edit"
        assert model.newsInstance != null
        assert model.newsInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/news/list'

        response.reset()

        populateValidParams(params)
        def news = new News(params)

        assert news.save() != null
        assert News.count() == 1

        params.id = news.id

        controller.delete()

        assert News.count() == 0
        assert News.get(news.id) == null
        assert response.redirectedUrl == '/news/list'
    }
}
