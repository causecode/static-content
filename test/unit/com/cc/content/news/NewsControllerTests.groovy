package com.cc.content.news



import org.junit.*
import grails.test.mixin.*

@TestFor(NewsController)
@Mock(News)
class NewsControllerTests {

    def populateValidParams(params) {
        assert params != null
        params.title = "Dummy Title"
        params.subTitle = "Dummy SubTitle"
        params.body = "Dummy Body"
        params.author = "Laxmi"
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
        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/news/show/1'
        assert controller.flash.message != null
        assert News.count() == 1
    }

    void testShow() {
        populateValidParams(params)
        def news = new News(params)

        assert news.save() != null

        params.id = news.id

        controller.validate()
        def model = controller.show()

        assert model.newsInstance == news
    }

    void testEdit() {
        populateValidParams(params)
        def news = new News(params)

        assert news.save() != null

        params.id = news.id

        controller.validate()
        def model = controller.edit()

        assert model.newsInstance == news
    }

    void testUpdate() {
        populateValidParams(params)
        def news = new News(params)

        assert news.save() != null

        params.id = news.id
        params.title = "Dummy Invalid Title"
        params.subTitle = "Dummy Invalid SubTitle"
        params.body = "Dummy Invalid Body"

        controller.validate()
        controller.update()

        news.clearErrors()

        populateValidParams(params)
        params.id = news.id
        params.version = -1
        controller.validate()
        controller.update()

        assert view == "/news/edit"
        assert model.newsInstance != null
        assert model.newsInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        populateValidParams(params)
        def news = new News(params)

        assert news.save() != null
        assert News.count() == 1

        params.id = news.id

        controller.validate()
        controller.delete()

        assert News.count() == 0
        assert News.get(news.id) == null
        assert response.redirectedUrl == '/news/list'
    }
}
