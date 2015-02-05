'use strict';

describe ("CRUD operations for Page domain.", function () {

    var chai = require('chai');
    var chaiAsPromised = require('chai-as-promised');
    chai.use(chaiAsPromised);
    var expect = chai.expect;

    var title, subTitle, body, author, metaValue

    beforeEach( function() {
        title = "page title";
        subTitle = "page subtitle";
        body = "page body";
        author = "author";
        metaValue = "metaValue";
    });
    
    var Page = function() {
        this.title = element(by.model('contentInstance.title'));
        this.subTitle = element(by.model('contentInstance.subTitle'));
        this.publish = element(by.model('contentInstance.publish'));
        this.addMetaTagsButton = element(by.css('.fa-plus')); 
        this.createButton = element(by.cssContainingText('button', 'Create'));
        
        this.upadateButton = element(by.cssContainingText('button', 'Update'));
        this.editButton = element(by.css('.btn-primary'));
        this.deleteButton = element(by.cssContainingText('button', 'Delete'));
        this.createButton = element(by.cssContainingText('button', 'Create'));
        
        this.alertBox = element(by.cssContainingText('button', 'OK'));

        this.addKey = function(key) {
            browser.actions().sendKeys(key).perform();
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
        
        this.get = function(action) {
            browser.get('#/page/' + action);
        };
    };

    it ("Create new Page.", function() {
        browser.get("#/page/create");
        var page = new Page();
        page.title.sendKeys(title);
        page.subTitle.sendKeys(subTitle);
        page.tabs(1);
        page.addKey(body);
        
        // Add meta tags
        page.addMetaTagsButton.click().then( function() {
            page.publish.click();
            page.tabs(1);
            element(by.cssContainingText('option', 'keywords')).click();
            page.tabs(1);
            page.addKey('Meta tags');
        });
        
        page.createButton.click().then( function() {
            browser.getCurrentUrl().then( function(url) {
                expect(url).to.contain('page/list');
            });
        });
    });
    
    it ('Edit Page instance.', function() {
        var page = new Page();
        page.get('list');
        page.tabs(11);
        browser.actions().sendKeys(protractor.Key.ENTER).perform().then( function() {
            page.wait(1000);
            page.editButton.click();
            page.wait(1000);
            page.titleInput.clear();
            page.titleInput.sendKeys('Updated Title');
            
            page.upadateButton.click().then( function() {
                browser.getCurrentUrl().then( function(url) {
                    expect(url).to.contain('page/list');
                });
            });
        });
    });
    
    it ('Delete Page instance.', function() {
        var page = new Page();
        page.get('list');
        // Go to first record in list
        page.tabs(11);
        
        browser.actions().sendKeys(protractor.Key.ENTER).perform().then( function() {
            page.wait(1000);
            page.deleteButton.click().then( function() {
                page.alertBox.click().then(function() {
                    browser.getCurrentUrl().then( function(url) {
                        expect(url).to.contain('page/list');
                    });
                });
            });
        });
    });
});