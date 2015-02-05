'use strict';

describe ("CRUD operations for Page Layout domain.", function () {

    var chai = require('chai');
    var chaiAsPromised = require('chai-as-promised');
    chai.use(chaiAsPromised);
    var expect = chai.expect;

    var layoutName

    beforeEach( function() {
        layoutName = "page layout name"
    });
    
    var PageLayout = function() {
        this.layoutName = element(by.model('pageLayoutInstance.layoutName'));
        
        this.upadateButton = element(by.cssContainingText('button', 'Update'));
        this.editButton = element(by.css('.btn-primary'));
        this.deleteButton = element(by.cssContainingText('button', 'Delete'));
        this.createButton = element(by.cssContainingText('button', 'Create'));
        
        this.alertBox = element(by.cssContainingText('button', 'OK'));
        
        this.get = function(action) {
            browser.get('#/pageLayout/' + action);
        };
        
        this.tabs = function(number) {
            for (var i=1; i<=number; i++) {
                browser.actions().sendKeys(protractor.Key.TAB).perform();
            }
        }; 
        
        this.wait = function(time) {
            browser.driver.sleep(time);
            browser.waitForAngular();
        };
    };
    
    it ("Create new Page Layuot.", function() {
        var pageLayout = new PageLayout();
        pageLayout.get('create');

        pageLayout.layoutName.sendKeys(layoutName);

        pageLayout.createButton.click().then( function() {
            browser.getCurrentUrl().then( function(url) {
                expect(url).to.contain('pageLayout/list');
            });
        });
    });
    
    it ('Edit Page Layouot instance.', function() {
        var pageLayout = new PageLayout();
        pageLayout.get('list');
        pageLayout.tabs(11);
        browser.actions().sendKeys(protractor.Key.ENTER).perform().then( function() {
            pageLayout.wait(1000);
            pageLayout.editButton.click();
            pageLayout.wait(1000);
            pageLayout.layoutName.clear();
            pageLayout.layoutName.sendKeys('Updated Layout');
            
            pageLayout.upadateButton.click().then( function() {
                browser.getCurrentUrl().then( function(url) {
                    expect(url).to.contain('pageLayout/list');
                });
            });
        });
    });
    
    it ('Delete Page Layout instance.', function() {
        var pageLayout = new PageLayout();
        pageLayout.get('list');
        // Go to first record in list
        pageLayout.tabs(11);
        
        browser.actions().sendKeys(protractor.Key.ENTER).perform().then( function() {
            pageLayout.wait(1000);
            pageLayout.deleteButton.click().then( function() {
                pageLayout.alertBox.click().then(function() {
                    browser.getCurrentUrl().then( function(url) {
                        expect(url).to.contain('pageLayout/list');
                    });
                });
            });
        });
    });
});