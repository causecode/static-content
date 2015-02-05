'use strict';

describe ("CRUD operations for Menu domain.", function () {

    var chai = require('chai');
    var chaiAsPromised = require('chai-as-promised');
    chai.use(chaiAsPromised);
    var expect = chai.expect;

    var name

    beforeEach( function() {
        name = "Home";
    });
    
    var MenuPage = function() {
        this.menuItemInstaceList = element.all(by.repeater('menuItemInstance in menuItemInstanceList'));
        this.titleInput = element(by.model('menuItemInstance.title'));
        this.menuItemInstane = element(by.css('.as-sortable-item-handle'));
        
        this.updateButton = element(by.cssContainingText('button', 'Update'));
        this.editMenuItemButtons = element.all(by.css('.update-menu-item'));
        
        this.get = function(id) {
            id = 2;
            browser.get('#/menu/show/' + id);
        };
    };

    it ('Should create MenuItemInstance', function() {
        browser.get('#/menu/show/2');
        
        var title = "Contact";
        var url = "www.causecode.com/contact";
        var menuItemInstanceCount;
        var menuItemInstanceList = element.all(by.repeater('menuItemInstance in menuItemInstanceList'));
        menuItemInstanceList.count().then(function(count) {
            menuItemInstanceCount = count; 
        });
        element(by.id('createMenuItem')).click().then( function() {
            element(by.model('menuItemInstance.title')).sendKeys(title);
            element(by.model('menuItemInstance.url')).sendKeys(url);
            element(by.cssContainingText('option', 'ROLE_ADMIN')).click();  
            element(by.model('menuItemInstance.showOnlyWhenLoggedIn')).click();
            element(by.cssContainingText('button', 'Create')).click().then(function() {
                menuItemInstanceList.count().then(function(count) {
                    expect(count).to.equal(menuItemInstanceCount + 1);
                });
            });
        });
    });
    
    it ('Should delete MenuItemInstance', function() {
        var menuItemInstanceCount;
        var deleteButton = element(by.css('.delete-menu-item'));
        var menuItemInstanceList = element.all(by.repeater('menuItemInstance in menuItemInstanceList'));
        
        browser.get('#/menu/show/2');
        
        menuItemInstanceList.count().then(function(count) {
            menuItemInstanceCount = count; 
        });
        
        deleteButton.click().then(function() {
            element(by.cssContainingText('button', 'OK')).click().then( function() {
                menuItemInstanceList.count().then(function(count) {
                    expect(count).to.equal(menuItemInstanceCount - 1);
                });
            });
        });
    });
    
    it ('Should update MenuItemInstance', function() {
        var updatedTitle = "Updated Title";
        var menuPage = new MenuPage();
        menuPage.get(2);
         
        menuPage.editMenuItemButtons.first().click().then( function() {
            menuPage.titleInput.clear();
            menuPage.titleInput.sendKeys(updatedTitle);
            menuPage.updateButton.click().then(function() {
                menuPage.menuItemInstane.getText().then( function(title) {
                    expect(title).to.equal(updatedTitle);
                });
            });
        });
    });
});