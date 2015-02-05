'use strict';

describe ("CRUD operations for News domain.", function () {

    var chai = require('chai');
    var chaiAsPromised = require('chai-as-promised');
    chai.use(chaiAsPromised);
    var expect = chai.expect;

    var title, subTitle, body, author

    beforeEach( function() {
        title = "How to apply";
        subTitle = "How to apply online";
        author = "author";
        body = "body";
    });
    
    var NewsPage = function() {
        this.list = element.all(by.repeater('instance in instaceList'));
        
        this.titleInput = element(by.model('contentInstance.title'));
        this.subTitleInput = element(by.model('contentInstance.subTitle'));
        this.authorInput = element(by.model('contentInstance.author'));
        this.isPublished = element(by.model('contentInstance.publish'));
        
        this.upadateButton = element(by.cssContainingText('button', 'Update'));
        this.editButton = element(by.css('.btn-primary'));
        this.deleteButton = element(by.cssContainingText('button', 'Delete'));
        this.createButton = element(by.cssContainingText('button', 'Create'));
        
        this.alertBox = element(by.cssContainingText('button', 'OK'));
        
        this.get = function(action) {
            browser.get('#/news/' + action);
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

    it ("Create new News.", function() {
        var newsPage = new NewsPage();
        newsPage.get('create');
        newsPage.titleInput.sendKeys(title);
        newsPage.subTitleInput.sendKeys(subTitle);
        newsPage.tabs(1);
        browser.actions().sendKeys(body).perform();
        newsPage.authorInput.sendKeys(author);
        newsPage.isPublished.click();

        newsPage.createButton.click().then( function() {
            browser.getCurrentUrl().then( function(url) {
                expect(url).to.contain('news/list');
            });
        });
    });
   
    it ("List News.", function() {
        browser.get("#/news/create");

        element(by.linkText('Cancel')).click().then( function() {
            browser.getCurrentUrl().then( function(url) {
                expect(url).to.contain('news/list');
            });
        });
    });

    it ('Edit News instance.', function() {
        var newsPage = new NewsPage();
        newsPage.get('list');
        newsPage.tabs(11);
        browser.actions().sendKeys(protractor.Key.ENTER).perform().then( function() {
            newsPage.wait(1000);
            newsPage.editButton.click();
            newsPage.wait(1000);
            newsPage.titleInput.clear();
            newsPage.titleInput.sendKeys('Updated Title');
            
            newsPage.upadateButton.click().then( function() {
                browser.getCurrentUrl().then( function(url) {
                    expect(url).to.contain('news/list');
                });
            });
        });
     });
    
    it ('Delete News instance.', function() {
        var newsPage = new NewsPage();
        newsPage.get('list');
        // Go to first record in list
        newsPage.tabs(11);
        
        browser.actions().sendKeys(protractor.Key.ENTER).perform().then( function() {
            newsPage.wait(1000);
            newsPage.deleteButton.click().then( function() {
                newsPage.alertBox.click().then(function() {
                    browser.getCurrentUrl().then( function(url) {
                        expect(url).to.contain('news/list');
                    });
                });
            });
        });
    });
    
});