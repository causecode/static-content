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
        this.updateButton = element(by.cssContainingText('button', 'Update'));
        this.titleInput = element(by.model('menuItemInstance.title'));
        this.editMenuItemButtons = element.all(by.css('.update-menu-item'));
        
        this.get = function(id) {
            id = 2;
            browser.get('#/menu/show/' + id);
        };
    };


    it ("Create new Menu.", function() {
        browser.get("#/menu/create");

        element(by.model('menuInstance.name')).sendKeys(name);
        element(by.cssContainingText('option', 'ROLE_ADMIN')).click();  
        element(by.model('menuInstance.showOnlyWhenLoggedIn')).click();
        element(by.cssContainingText('button', 'Create')).click().then( function() {
            browser.getCurrentUrl().then( function(url) {
                expect(url).to.contain('menu/list');
            });
        });
    });
    
    xit ('Edit Menu instance.', function() {
        // get any instance id
        browser.get('#/menu/edit/4');

        element(by.model('contentInstance.title')).clear();
        element(by.model('contentInstance.title')).sendKeys('Updated Title');
        
        element(by.cssContainingText('button', 'Update')).click().then( function() {
            browser.getCurrentUrl().then( function(url) {
                expect(url).to.contain('menu/list');
            });
        });
    });
    
});