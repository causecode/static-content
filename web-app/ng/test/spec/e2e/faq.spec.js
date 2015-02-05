'use strict';

describe ("CRUD operations for Lead domain.", function () {

    var chai = require('chai');
    var chaiAsPromised = require('chai-as-promised');
    chai.use(chaiAsPromised);
    var expect = chai.expect;

    var title, subTitle, body, author

    beforeEach( function() {
        title = "How to apply";
        subTitle = "How to apply online";
        author = "author";
        body = "<p>body<p>";
    });

    it ("Create new FAQ.", function() {
        browser.get("#/faq/create");

        element(by.model('contentInstance.title')).sendKeys(title);
        element(by.model('contentInstance.subTitle')).sendKeys(subTitle);
        browser.actions().sendKeys(protractor.Key.TAB).perform();
        browser.actions().sendKeys('body of FAQ').perform();
        element(by.model('contentInstance.author')).sendKeys(author);

        element(by.cssContainingText('button', 'Create')).click().then( function() {
            browser.getCurrentUrl().then( function(url) {
                expect(url).to.contain('faq/list');
//                expect(element(by.id('alert-message')).getText()).to.eventually.contain('Record saved successfully.');
            });
        });
    });
    
    it ("Editng FAQ.", function() {
        browser.get("#/faq/edit/1");

        element(by.model('contentInstance.title')).sendKeys(title);
        element(by.model('contentInstance.subTitle')).sendKeys(subTitle);
        element(by.model('contentInstance.author')).sendKeys(author);
        element(by.model('contentInstance.body')).sendKeys(body);

        element(by.cssContainingText('button', 'Update')).click().then( function() {
            browser.getCurrentUrl().then( function(url) {
                expect(url).to.contain('faq/list');
            });
        });
        /*expect(element(by.id('alert-message')).getText()).to.eventually.contain('Record saved successfully.');*/
    });

});