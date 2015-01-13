/* global describe, it, sinon, inject, beforeEach, expect*/
'use strict';

describe('Testing MenuModel: ', function() {
    beforeEach(module('ngApp'));
    
    var MenuModel, MenuModelInstance;
    
    beforeEach( inject( function(_MenuModel_){
        MenuModel = _MenuModel_;
        MenuModelInstance = new MenuModel();
    }));
    
    describe('Custom actions', function() {
        var $httpBackend;

        beforeEach(inject(function($injector) {
            $httpBackend = $injector.get('$httpBackend');
        }));

        it ('Testing create method request parameters', function() {
            $httpBackend.when('POST', '/api/v1/menu/action/create').respond({}, {});
            MenuModel.getRoleList();
            $httpBackend.flush();
        });
    });
    
    it ('should return resource name initialised in MenuModel.', function() {
        expect(MenuModel.resourceName).to.equal('menu');
    });

    it ('should return clazz name initialised in MenuModel.', function() {
        expect(MenuModel.getClazzName()).to.equal('MenuModel');
    });

    it ('should return column names initialised in MenuModel.', function() {
        expect(MenuModel.getColumnNames()).to.deep.equal(['name','dateCreated','lastUpdated']);
    });

    it ('should return sort properties initialised in MenuModel.', function() {
        expect(MenuModel.getSortProperties()).to.deep.equal([]);
    });
    
});