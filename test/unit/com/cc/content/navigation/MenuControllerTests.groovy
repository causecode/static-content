package com.cc.content.navigation



import java.util.Date;
import java.util.List;

import org.junit.*

import grails.test.mixin.*

@TestFor(MenuController)
@Mock(Menu)
class MenuControllerTests {

    def populateValidParams(params) {
        assert params != null
        params["name"] = 'sampleName'
        params["showOnlyWhenLoggedIn"] = true
        params["menuItems"] = []
    }

    void testIndex() {
        controller.index()
        assert "/menu/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.menuInstanceList.size() == 0
        assert model.menuInstanceTotal == 0
    }

    void testSave() {
        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/menu/show/1'
        assert controller.flash.message != null
        assert Menu.count() == 1
    }

    void testEdit() {
        controller.validate()
        assert flash.message != null
        assert response.redirectedUrl == '/menu/list'

        populateValidParams(params)
        def menu = new Menu(params)

        assert menu.save(flush: true) != null

        params.id = menu.id
        controller.validate()

        def model = controller.edit()

        assert model.menuInstance == menu
    }

    void testDelete() {
        controller.validate()
        assert flash.message != null
        assert response.redirectedUrl == '/menu/list'

        response.reset()

        populateValidParams(params)
        def menu = new Menu(params)

        assert menu.save(flush: true) != null
        assert Menu.count() == 1

        params.id = menu.id
        controller.validate()
        controller.delete()

        assert Menu.count() == 0
        assert Menu.get(menu.id) == null
        assert response.redirectedUrl == '/menu/list'
    }
}
