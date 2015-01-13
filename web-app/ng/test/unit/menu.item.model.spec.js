/* global describe, it, sinon, inject, beforeEach, expect*/
'use strict';

describe('Testing MenuItemModel: ', function() {
    beforeEach(module('ngApp'));
    
    var MenuItemModel, MenuItemModelInstance;
    
    beforeEach( inject( function(_MenuItemModel_){
        MenuItemModel = _MenuItemModel_;
        MenuItemModelInstance = new MenuItemModel();
    }));
    
    describe('Custom actions', function() {
        var $httpBackend;

        beforeEach(inject(function($injector) {
            $httpBackend = $injector.get('$httpBackend');
        }));

        it ('Testing saveMenuItem method request parameters', function() {
            $httpBackend.when('POST', '/api/v1/menuItem/action/save').respond({}, {});
            MenuItemModel.saveMenuItem();
            $httpBackend.flush();
        });

        it ('Testing reorder method request parameters', function() {
            $httpBackend.when('POST', '/api/v1/menuItem/action/reorder').respond({}, {});
            MenuItemModel.reorder();
            $httpBackend.flush();
        });
    });
    
    it ('should return resource name initialised in MenuItemModel.', function() {
        expect(MenuItemModel.resourceName).to.equal('menuItem');
    });

    it ('should return clazz name initialised in MenuItemModel.', function() {
        expect(MenuItemModel.getClazzName()).to.equal('MenuItemModel');
    });

    it ('should return column names initialised in MenuItemModel.', function() {
        expect(MenuItemModel.getColumnNames()).to.deep.equal(['title', 'subTitle', 'body', 'publish']);
    });

    it ('should return sort properties initialised in MenuItemModel.', function() {
        expect(MenuItemModel.getSortProperties()).to.deep.equal([]);
    });
    
});