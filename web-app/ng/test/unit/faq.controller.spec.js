/* global describe, it, sinon, inject, beforeEach, expect*/
'use strict';

describe('Testing FAQController: ', function() {
    beforeEach(module('ngApp'));
    
    var scope, ctrl, FAQModel, requsetHandler, appService, BaseModel, showScope;
    var $httpBackend, $rootScope, $state, $modal;
    
    beforeEach( inject( function(_$rootScope_, _$state_, _FAQModel_, _appService_, _BaseModel_, _$modal_){
        $rootScope = _$rootScope_;
        $state = _$state_;
        FAQModel = _FAQModel_;
        appService = _appService_;
        BaseModel = _BaseModel_;
        $modal = _$modal_;
    }));

    beforeEach( inject( function($rootScope, $controller, $injector) {
        scope = $rootScope.$new();
        scope.actionName = 'create';
        ctrl = $controller('FAQController', {$scope: scope});
        $httpBackend = $injector.get('$httpBackend');
    }));
    
    it('Should expect FAQModel instance initialized', function () {
        expect(scope.contentInstance).to.be.an.instanceOf(FAQModel);
   });

    describe('Testing show action', function() {
        beforeEach( inject( function($rootScope, $controller, $injector) {
            showScope = $rootScope.$new();
            showScope.actionName = 'show';
            ctrl = $controller('FAQController', {$scope: showScope});
        }));

        it('should send an HTTP GET request', function() {
            $httpBackend.expect('GET', '/api/v1/FAQ').respond(200);
            $httpBackend.flush();
            $httpBackend.verifyNoOutstandingRequest();
            expect(scope.contentInstance).to.be.an.instanceOf(FAQModel);
        });
        
        it('should send an HTTP GET request', function() {
            var faqModelInstance = new FAQModel();
            $httpBackend.expect('GET', '/api/v1/FAQ').respond(200, faqModelInstance);
            $httpBackend.flush();
            expect(scope.contentInstance).to.be.an.instanceOf(FAQModel);
        });    

    });
    
});