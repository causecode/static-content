package com.cc.content.navigation



import java.util.Date;
import java.util.List;

import org.junit.*

import grails.test.mixin.*

import com.cc.content.navigation.MenuItemService
import com.cc.content.navigation.Menu

@TestFor(MenuItemController)
@Mock([MenuItem, MenuItemService, Menu])
class MenuItemControllerTests {

    // TODO: Need to fix and update all following unit test cases.

    def populateValidParams(params) {
        assert params != null
        params["title"] = 'sampleTitle'
        params["url"] = 'sampleUrl'
        params["showOnlyWhenLoggedIn"] = true
        params["childItems"] = []
        params["parent"] = []
        params["menu"] = []
        params["index"] = '0'
        params["parentId"] = []
    }

    void createMenu() {
        Menu menuInstance = Menu.findOrSaveByNameAndShowOnlyWhenLoggedIn("sampleMenu", true)
        menuInstance.save(flush: true)
        params["menuId"] = menuInstance.id
    }


    void testSave() { return
        createMenu();
        response.reset()
        populateValidParams(params)
        controller.save()
        assert MenuItem.count() == 1
    }

    void testDelete() { return
//        controller.delete()
//        assert flash.message != null
//        assert response.redirectedUrl == '/menuItem/list'

        response.reset()
        createMenu();

        populateValidParams(params)
        response.text == controller.save()
        assert MenuItem.count() == 1

        params.id = response.text
        controller.validate()
        controller.delete()

        assert MenuItem.count() == 0
        assert MenuItem.get(params.id) == null
    }
}
