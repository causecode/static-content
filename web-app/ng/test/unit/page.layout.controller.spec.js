/* global describe, it, sinon, inject, beforeEach, expect*/
'use strict';

describe('Testing PageLayoutController: ', function() {
    beforeEach(module('ngApp'));
    
    var scope, ctrl, PageLayoutModel, requsetHandler, appService, BaseModel;
    var $httpBackend, $rootScope, $state, $modal;
    
    beforeEach( inject( function(_$rootScope_, _$state_, _PageLayoutModel_, _appService_, _BaseModel_, _$modal_){
        $rootScope = _$rootScope_;
        $state = _$state_;
        PageLayoutModel = _PageLayoutModel_;
        appService = _appService_;
        BaseModel = _BaseModel_;
        $modal = _$modal_;
    }));
    
    beforeEach( inject( function($rootScope, $controller, $injector) {
        scope = $rootScope.$new();
        scope.actionName = 'create';
        ctrl = $controller('PageLayoutController', {$scope: scope});
        $httpBackend = $injector.get('$httpBackend');
    }));
    
    it('create method: PageLayoutModel instance is initialized', function () {
        expect(scope.pageLayoutInstance).to.be.an.instanceOf(PageLayoutModel);
    });
    
    describe('Testing show action', function() {
        var showScope, showCtrl
        beforeEach( inject( function($rootScope, $controller, $injector) {
            showScope = $rootScope.$new();
            showScope.actionName = 'show';
            showCtrl = $controller('PageLayoutController', {$scope: showScope});
        }));

        it('should send an HTTP GET request', function() {
            $httpBackend.expect('GET', '/api/v1/pageLayout').respond(200, {});
            $httpBackend.verifyNoOutstandingRequest();
            expect(showScope.pageLayoutInstance).to.be.an.instanceOf(PageLayoutModel);
        });    
    });
});