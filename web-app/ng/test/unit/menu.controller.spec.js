/* global describe, it, inject, beforeEach, expect*/
'use strict';

describe('Testing MenuController: ', function() {
    beforeEach(module('ngApp'));
    
    var scope, ctrl, MenuModel, appService, BaseModel;
    var $httpBackend, $rootScope, $state, $modal;
    
    beforeEach( inject( function(_$rootScope_, _$state_, _MenuModel_, _appService_, _BaseModel_, _$modal_){
        $rootScope = _$rootScope_;
        $state = _$state_;
        MenuModel = _MenuModel_;
        appService = _appService_;
        BaseModel = _BaseModel_;
        $modal = _$modal_;
    }));
    
    beforeEach( inject( function($rootScope, $controller, $injector) {
        scope = $rootScope.$new();
        scope.actionName = 'create';
        ctrl = $controller('MenuController', {$scope: scope});
        $httpBackend = $injector.get('$httpBackend');
    }));
    
    it('expect MenuModel instance is initialized', function () {
        expect(scope.menuInstance).to.be.an.instanceOf(MenuModel);
    });
    
    it('should send POST request to populate roleList', function () {
        var roleList = ['ROLE_ADMIN','ROLE_USER'];
        $httpBackend.expectPOST('/api/v1/menu/action/create').respond(200, {roleList: roleList});
        $httpBackend.flush();
        $httpBackend.verifyNoOutstandingRequest();
    });
});