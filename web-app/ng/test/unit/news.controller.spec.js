/* global describe, it, sinon, inject, beforeEach, expect*/
'use strict';

describe('Testing NewsController: ', function() {
    beforeEach(module('ngApp'));
    
    var scope, ctrl, NewsModel, requsetHandler, appService, BaseModel, showScope;
    var $httpBackend, $rootScope, $state, $modal;
    
    beforeEach( inject( function(_$rootScope_, _$state_, _NewsModel_, _appService_, _BaseModel_, _$modal_){
        $rootScope = _$rootScope_;
        $state = _$state_;
        NewsModel = _NewsModel_;
        appService = _appService_;
        BaseModel = _BaseModel_;
        $modal = _$modal_;
    }));
    
    beforeEach( inject( function($rootScope, $controller, $injector) {
        scope = $rootScope.$new();
        scope.actionName = 'create';
        ctrl = $controller('NewsController', {$scope: scope});
        $httpBackend = $injector.get('$httpBackend');
    }));
    
    it('Should expect NewsModel instance initialized', function () {
        expect(scope.contentInstance).to.be.an.instanceOf(NewsModel);
   });

    describe('Testing show action', function() {
        beforeEach( inject( function($rootScope, $controller, $injector) {
            showScope = $rootScope.$new();
            showScope.actionName = 'show';
            ctrl = $controller('NewsController', {$scope: showScope});
        }));

        it('should send an HTTP GET request', function() {
            var newsModelInstance = new NewsModel();
            $httpBackend.expect('GET', '/api/v1/news').respond(200, newsModelInstance);
            $httpBackend.flush();
            $httpBackend.verifyNoOutstandingRequest();
            expect(scope.contentInstance).to.be.an.instanceOf(NewsModel);
        });    
    });
    
});