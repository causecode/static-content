package com.cc.content.navigation



import org.junit.*
import grails.test.mixin.*

@TestFor(MenuItemController)
@Mock(MenuItem)
class MenuItemControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
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
        controller.save()

        assert model.menuItemInstance != null
        assert view == '/menuItem/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/menuItem/show/1'
        assert controller.flash.message != null
        assert MenuItem.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/menuItem/list'

        populateValidParams(params)
        def menuItem = new MenuItem(params)

        assert menuItem.save() != null

        params.id = menuItem.id

        def model = controller.show()

        assert model.menuItemInstance == menuItem
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/menuItem/list'

        populateValidParams(params)
        def menuItem = new MenuItem(params)

        assert menuItem.save() != null

        params.id = menuItem.id

        def model = controller.edit()

        assert model.menuItemInstance == menuItem
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/menuItem/list'

        response.reset()

        populateValidParams(params)
        def menuItem = new MenuItem(params)

        assert menuItem.save() != null

        // test invalid parameters in update
        params.id = menuItem.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/menuItem/edit"
        assert model.menuItemInstance != null

        menuItem.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/menuItem/show/$menuItem.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        menuItem.clearErrors()

        populateValidParams(params)
        params.id = menuItem.id
        params.version = -1
        controller.update()

        assert view == "/menuItem/edit"
        assert model.menuItemInstance != null
        assert model.menuItemInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/menuItem/list'

        response.reset()

        populateValidParams(params)
        def menuItem = new MenuItem(params)

        assert menuItem.save() != null
        assert MenuItem.count() == 1

        params.id = menuItem.id

        controller.delete()

        assert MenuItem.count() == 0
        assert MenuItem.get(menuItem.id) == null
        assert response.redirectedUrl == '/menuItem/list'
    }
}
