'use strict';

describe ("CRUD operations for FAQ domain.", function () {

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
    
    var FaqPage = function() {
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
            browser.get('#/faq/' + action);
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


    it ("Create new FAQ.", function() {
        browser.get("#/faq/create");
        var faqPage = new FaqPage();
        
        faqPage.titleInput.sendKeys(title);
        faqPage.subTitleInput.sendKeys(subTitle);
        faqPage.tabs(1);
        browser.actions().sendKeys(body).perform();
        faqPage.authorInput.sendKeys(author);
        faqPage.isPublished.click();

        faqPage.createButton.click().then( function() {
            browser.getCurrentUrl().then( function(url) {
                expect(url).to.contain('faq/list');
            });
        });
    });
   
    it ("List FAQ.", function() {
        browser.get("#/faq/create");

        element(by.linkText('Cancel')).click().then( function() {
            browser.getCurrentUrl().then( function(url) {
                expect(url).to.contain('faq/list');
            });
        });
    });

    it ('Edit FAQ instance.', function() {
        var faqPage = new FaqPage();
        faqPage.get('list');
        faqPage.tabs(11);
        browser.actions().sendKeys(protractor.Key.ENTER).perform().then( function() {
            faqPage.wait(1000);
            faqPage.editButton.click();
            faqPage.wait(1000);
            faqPage.titleInput.clear();
            faqPage.titleInput.sendKeys('Updated Title');
            
            faqPage.upadateButton.click().then( function() {
                browser.getCurrentUrl().then( function(url) {
                    expect(url).to.contain('faq/list');
                });
            });
        });
     });
    
    it ('Delete FAQ instance.', function() {
        var faqPage = new FaqPage();
        faqPage.get('list');
        // Go to first record in list
        faqPage.tabs(11);
        
        browser.actions().sendKeys(protractor.Key.ENTER).perform().then( function() {
            faqPage.wait(1000);
            faqPage.deleteButton.click().then( function() {
                faqPage.alertBox.click().then(function() {
                    browser.getCurrentUrl().then( function(url) {
                        expect(url).to.contain('faq/list');
                    });
                });
            });
        });
    });
    
    
});