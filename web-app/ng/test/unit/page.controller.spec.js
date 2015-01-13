/* global describe, it, sinon, inject, beforeEach, expect*/
'use strict';

describe('Testing PageController: ', function() {
    beforeEach(module('ngApp'));
    
    var scope, ctrl, PageModel, requestHandler, appService, BaseModel;
    var $httpBackend, $rootScope, $state, $modal;
    
    beforeEach( inject( function(_$rootScope_, _$state_, _PageModel_, _appService_, _BaseModel_, _$modal_){
        $rootScope = _$rootScope_;
        $state = _$state_;
        PageModel = _PageModel_;
        appService = _appService_;
        BaseModel = _BaseModel_;
        $modal = _$modal_;
    }));

    beforeEach( inject( function($injector){
        var isPageBlocked = false;
        appService.blockPage = function(isBlocked) {
            isPageBlocked = isBlocked;
        }
        $httpBackend = $injector.get('$httpBackend');
        $httpBackend.when('GET', '/api/v1/page/action/getMetaTypeList').respond({}, {});
        $httpBackend.when('GET', '/api/v1/pageLayout/action/getPageLayoutList').respond({}, {});
    }));
    
    describe('Testing method: addForm and fetchPage', function() {
        beforeEach(inject( function($rootScope, $controller, $injector) {
            scope = $rootScope.$new();
            ctrl = $controller('PageController', {$scope: scope});
        }));
        
        it('testing addForm', function() {
            scope.contentInstance = {};
            scope.contentInstance.metaList = [];
            scope.addForm();
            expect(scope.contentInstance.metaList.length).to.equal(1);
        });    

        it('Testing fetch method: should send HTTP GET request to fetch page', function() {
            scope.pageInstance = {};
            var data = {pageInstance: {id: 1}}, pageId = 1;
            $httpBackend.expectGET('/api/v1/page/1').respond(200, data);
            scope.fetchPage(pageId); 
            $httpBackend.flush();
            expect(scope.pageInstance.id).to.equal(pageId);
        });    
    });
    
    describe('Testing action create', function() {
        var createScope, createCtrl;
        beforeEach( inject(function($rootScope, $controller, $injector) {
            createScope = $rootScope.$new();
            createScope.actionName = 'create';
            createCtrl = $controller('PageController', {$scope: createScope, $http: $httpBackend});
            $httpBackend.flush();
        }));

        it('should expect contentInstance as instance of PageModel', function() {
            expect(createScope.contentInstance).to.be.an.instanceOf(PageModel);
            expect(createScope.contentInstance.metaList.length).to.equal(0);
        });    
    });
    
    describe('Testing action edit', function() {
        var pageData = {pageLayout: {id: 2}, layoutName: "bbb"};
        
        beforeEach(inject( function($rootScope, $controller, $injector) {
            scope = $rootScope.$new();
            scope.actionName = 'edit';
            scope.id = 1;
            ctrl = $controller('PageController', {$scope: scope});
            requestHandler = $httpBackend.when('GET', '/api/v1/pageLayout/2').respond(200, pageData);
        }));

        it('should send HTTP GET request', function() {
            $httpBackend.expectGET('/api/v1/page/1').respond(200, pageData);
            $httpBackend.flush();
            $httpBackend.verifyNoOutstandingRequest();
        });
        
        it('should populate content insatance', function() {
            $httpBackend.expectGET('/api/v1/page/1').respond(200, pageData);
            $httpBackend.flush();
            expect(scope.contentInstance).to.be.an.instanceOf(PageModel);
        });    

    });
});