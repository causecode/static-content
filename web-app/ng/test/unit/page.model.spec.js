/* global describe, it, sinon, inject, beforeEach, expect*/
'use strict';

describe('Testing PageModel: ', function() {
    beforeEach(module('ngApp'));
    
    var PageModel, PageModelInstance;
    
    beforeEach( inject( function(_PageModel_){
        PageModel = _PageModel_;
        PageModelInstance = new PageModel();
    }));
    
    describe('Custom actions', function() {
        var $httpBackend;

        beforeEach(inject(function($injector) {
            $httpBackend = $injector.get('$httpBackend');
        }));

        it ('Testing getMetaList method request parameters', function() {
            $httpBackend.when('GET', '/api/v1/page/action/getMetaTypeList').respond({}, {});
            PageModel.getMetaList();
            $httpBackend.flush();
        });
    });
    
    it ('should return resource name initialised in PageModel.', function() {
        expect(PageModel.resourceName).to.equal('page');
    });

    it ('should return clazz name initialised in PageModel.', function() {
        expect(PageModel.getClazzName()).to.equal('PageModel');
    });

    it ('should return column names initialised in PageModel.', function() {
        expect(PageModel.getColumnNames()).to.deep.equal(['title', 'subTitle', 'publish']);
    });

    it ('should return sort properties initialised in PageModel.', function() {
        expect(PageModel.getSortProperties()).to.deep.equal([]);
    });
    
});