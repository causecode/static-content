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
        body = "<p>body<p>";
        author = "author";
    });

    it ("Create new News.", function() {
        browser.get("#/news/create");

        element(by.model('contentInstance.title')).sendKeys(title);
        element(by.model('contentInstance.subTitle')).sendKeys(subTitle);
        element(by.model('contentInstance.author')).sendKeys(author);
        element(by.model('contentInstance.body')).sendKeys(body);

        element(by.cssContainingText('button', 'Create')).click().then( function() {
            browser.getCurrentUrl().then( function(url) {
                expect(url).to.contain('news/list');
            });
        });
        expect(element(by.id('alert-message')).getText()).to.eventually.contain('News Created');
    });

});