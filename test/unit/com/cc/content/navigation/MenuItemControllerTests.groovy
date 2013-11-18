package com.cc.content.navigation



import org.junit.*
import grails.test.mixin.*

@TestFor(MenuItemController)
@Mock(MenuItem, Menu)
class MenuItemControllerTests {

    Menu menu

    void setUp() {
        menu = new Menu(name: "Menu", roles: "ROLE_USER", showOnlyWhenLoggedIn: true)
        assert menu.save()
    }
    def populateValidParams(params) {
        assert params != null
        params.title = "MenuItem1"
        params.url = "/"
        params.roles = "ROLE_USER"
        params.showOnlyWhenLoggedIn = true
        params['menu'] = [:]
        params['menu'].id = menu.id
    }

    void testIndex() {
        controller.index()
        assert "/menuItem/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.menuItemInstanceList.size() == 0
        assert model.menuItemInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.menuItemInstance != null
    }

    void testSave() {
        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/menuItem/show/1'
        assert controller.flash.message != null
        assert MenuItem.count() == 1
    }

    void testShow() {
        populateValidParams(params)
        def menuItem = new MenuItem(params)

        assert menuItem.save() != null

        params.id = menuItem.id

        controller.validate()
        def model = controller.show()

        assert model.menuItemInstance == menuItem
    }

    void testEdit() {
        populateValidParams(params)
        def menuItem = new MenuItem(params)

        assert menuItem.save() != null

        params.id = menuItem.id

        controller.validate()
        def model = controller.edit()

        assert model.menuItemInstance == menuItem
    }

    void testUpdate() {
        populateValidParams(params)
        def menuItem = new MenuItem(params)

        assert menuItem.save() != null

        params.id = menuItem.id
        params.title = "Invalid MenuItem1"
        params.url = ""
        params.roles = ""
        params.showOnlyWhenLoggedIn = false

        controller.validate()
        controller.update()

        assert view == "/menuItem/edit"
        assert model.menuItemInstance != null

        menuItem.clearErrors()

        populateValidParams(params)
        controller.validate()
        controller.update()

        assert response.redirectedUrl == "/menuItem/show/$menuItem.id"
        assert flash.message != null

        response.reset()
        menuItem.clearErrors()

        populateValidParams(params)
        params.id = menuItem.id
        params.version = -1
        controller.validate()
        controller.update()

        assert view == "/menuItem/edit"
        assert model.menuItemInstance != null
        assert model.menuItemInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        populateValidParams(params)
        def menuItem = new MenuItem(params)

        assert menuItem.save() != null
        assert MenuItem.count() == 1

        params.id = menuItem.id

        controller.validate()
        controller.delete()

        assert MenuItem.count() == 0
        assert MenuItem.get(menuItem.id) == null
        assert response.redirectedUrl == '/menuItem/list'
    }
}
