package com.cc.content.navigation



import org.junit.*
import grails.test.mixin.*

@TestFor(MenuController)
@Mock(Menu)
class MenuControllerTests {

    def populateValidParams(params) {
        assert params != null
        params.name = "Home"
        params.role = "ROLE_USER"
        params.showOnlyWhenLoggedIn = true
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

    void testCreate() {
        def model = controller.create()

        assert model.menuInstance != null
    }

    void testSave() {
        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/menu/show/1'
        assert controller.flash.message != null
        assert Menu.count() == 1
    }

    void testShow() {
        populateValidParams(params)
        def menu = new Menu(params)

        assert menu.save() != null

        params.id = menu.id

        controller.validate()
        def model = controller.show()

        assert model.menuInstance == menu
    }

    void testEdit() {
        populateValidParams(params)
        def menu = new Menu(params)

        assert menu.save() != null

        params.id = menu.id

        controller.validate()
        def model = controller.edit()

        assert model.menuInstance == menu
    }

    void testUpdate() {
        populateValidParams(params)
        def menu = new Menu(params)

        assert menu.save() != null

        params.id = menu.id
        params.name = "Invalid Menu"
        params.role = "ROLE"
        params.showOnlyWhenLoggedIn = false

        controller.validate()
        controller.update()

        menu.clearErrors()

        populateValidParams(params)
        params.id = menu.id
        params.version = -1
        controller.validate()
        controller.update()

        assert view == "/menu/edit"
        assert model.menuInstance != null
        assert model.menuInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        populateValidParams(params)
        def menu = new Menu(params)

        assert menu.save() != null
        assert Menu.count() == 1

        params.id = menu.id

        controller.validate()
        controller.delete()

        assert Menu.count() == 0
        assert Menu.get(menu.id) == null
        assert response.redirectedUrl == '/menu/list'
    }
}
