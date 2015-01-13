/* global describe, it, sinon, inject, beforeEach, expect*/
'use strict';

describe('Testing PageLayoutModel: ', function() {
    beforeEach(module('ngApp'));
    
    var PageLayoutModel, PageLayoutModelInstance;
    
    beforeEach( inject( function(_PageLayoutModel_){
        PageLayoutModel = _PageLayoutModel_;
        PageLayoutModelInstance = new PageLayoutModel();
    }));
    
    it ('should return resource name initialised in PageLayoutModel.', function() {
        expect(PageLayoutModel.resourceName).to.equal('pageLayout');
    });

    it ('should return clazz name initialised in PageLayoutModel.', function() {
        expect(PageLayoutModel.getClazzName()).to.equal('PageLayoutModel');
    });

    it ('should return column names initialised in PageLayoutModel.', function() {
        expect(PageLayoutModel.getColumnNames()).to.deep.equal(['layoutName']);
    });

    it ('should return sort properties initialised in PageLayoutModel.', function() {
        expect(PageLayoutModel.getSortProperties()).to.deep.equal(['layoutName']);
    });
    
});