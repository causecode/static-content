/* global describe, it, sinon, inject, beforeEach, expect*/
'use strict';

describe('Testing MenuItemController: ', function() {
    beforeEach(module('ngApp'));
    
    var scope, ctrl, MenuItemModel, requsetHandler, appService, BaseModel;
    var $httpBackend, $rootScope, $state, $modal;
    
    beforeEach( inject( function(_$rootScope_, _$state_, _MenuItemModel_, _appService_, _BaseModel_, _$modal_){
        $rootScope = _$rootScope_;
        $state = _$state_;
        MenuItemModel = _MenuItemModel_;
        appService = _appService_;
        BaseModel = _BaseModel_;
        $modal = _$modal_;
    }));
    
    beforeEach( inject( function($rootScope, $controller) {
        scope = $rootScope.$new();
        scope.actionName = 'create';
        ctrl = $controller('MenuItemController', {$scope: scope});
        appService.showAlertMessage = function(message) {
            var alertMessage = message;
        };
    }));
    
    it('create method: MenuItemModel instance is initialized', function () {
        expect(scope.menuItemInstance).to.be.an.instanceOf(MenuItemModel);
    });    
    
    it('menuItemInstanceList', function () {
        var data = {};
        data.menuItemInstanceList = [{}];
        data.menuItemInstanceList[0].childItems = [{}];
        
        scope.createMenuItemInstanceList(data);
        expect(scope.menuItemInstanceList[0]).to.be.an.instanceOf(MenuItemModel);
        
        scope.createMenuItemModelInstance(data);
        expect(scope.menuItemInstanceList[0].childItems[0]).to.be.an.instanceOf(MenuItemModel);
        
    });
    
    it('Testing $scope.open method', function () {
        var menuItemInstance = {};
        scope.open(menuItemInstance);
        expect(scope.menuItemInstance).to.be.an.instanceOf(MenuItemModel);
    });
    
    it('Testing $scope.createMenuItem method', function () {
        scope.tempId = 0;
        scope.menuItemInstanceList = [];
        scope.buttonDisabled = false;
        scope.menuItemInstance = {};
        scope.modalInstance = $modal.open({
            templateUrl: 'views/menuItem/editMenuItemOverlay.html',
            scope: scope
        });
        scope.modalInstance.close = function(){
            return; 
        }
        scope.createMenuItem();
        expect(scope.menuItemInstanceList[0].tempId).to.equal(1);
        expect(scope.tempId).to.equal(1);
        expect(scope.buttonDisabled).to.equal(true);
    });
    
    it('Testing $scope.getIndexOfMenuItem method', function () {
        scope.menuItemInstanceList = [{}];
        scope.menuItemInstanceList[0].tempId = 1;
        var index = scope.getIndexOfMenuItem(1);
        expect(index).to.equal(0);
    });
    
    it('Testing $scope.enableMenuItemButton method', function () {
        scope.buttonDisabled = true;
        scope.enableMenuItemButton();
        expect(scope.buttonDisabled).to.equal(false);
    });
    
    it('Testing $scope.disableMenuItemButton method', function () {
        scope.buttonDisabled = false;
        scope.disableMenuItemButton();
        expect(scope.buttonDisabled).to.equal(true);
    });
    
    
    describe('Testing save method', function() {
        beforeEach( inject( function($injector) {
            $httpBackend = $injector.get('$httpBackend');
            $httpBackend.when('POST', '/api/v1/menuItem').respond(200, {message: 'Ok.', id: 0});
        }));

        it('Should expect menuItemInstance.menuId equal menuInstance.id', function () {
            scope.menuInstance = {id:1};
            var menuItemInstance = {parentId:12};
            menuItemInstance.menuId = 0;
            var parent = {id:12};
            menuItemInstance = new MenuItemModel(menuItemInstance);
            scope.save(menuItemInstance, parent);
            $httpBackend.flush();
            expect(menuItemInstance.menuId).to.equal(scope.menuInstance.id)
        });
        
        it('should send an HTTP POST request to save menu item', function() {
            $httpBackend.verifyNoOutstandingExpectation();
            $httpBackend.verifyNoOutstandingRequest();
        }); 
    });
    
    describe('Testing update method', function() {
        beforeEach( inject( function($injector) {
            $httpBackend = $injector.get('$httpBackend');
            $httpBackend.when('PUT', '/api/v1/menuItem').respond(200, {message: 'Ok.', id: 0});
            scope.menuItemInstance = new MenuItemModel();
            scope.modalInstance = {};
            scope.modalInstance.close = function(){
                return; 
            }
            scope.update();
            $httpBackend.flush();
        }));
        
        it('should send an HTTP POST request to update menu item', function() {
            $httpBackend.verifyNoOutstandingExpectation();
            $httpBackend.verifyNoOutstandingRequest();
        }); 
    });
    
    describe('Testing remove method', function() {
        var parentMenuItemInstance;
        var menuItemInstance; 
        beforeEach( inject( function($injector) {
            scope.menuItemInstanceList = [];
            menuItemInstance = {tempId:12};
            parentMenuItemInstance = {id:12, childItems:[]};
            parentMenuItemInstance.childItems[0] = menuItemInstance;
            menuItemInstance = new MenuItemModel(menuItemInstance);
            parentMenuItemInstance = new MenuItemModel(parentMenuItemInstance);
            scope.menuItemInstanceList.push(parentMenuItemInstance);
            $httpBackend = $injector.get('$httpBackend');
            $httpBackend.expect('DELETE', '/api/v1/menuItem').respond(200);
        }));

        
        it('Testing $scope.remove method', function () {
           scope.remove(menuItemInstance, parentMenuItemInstance);
           expect(parentMenuItemInstance.childItems.length).to.equal(0)
        });
        
        it('should send an HTTP DELETE request to menu item', function() {
            scope.remove(parentMenuItemInstance);
            $httpBackend.verifyNoOutstandingRequest();
        }); 
    });
    
    describe('Testing REORDER method', function() {
        beforeEach( inject( function($injector) {
            $httpBackend = $injector.get('$httpBackend');
            $httpBackend.when('POST', '/api/v1/menuItem/action/reorder').respond(200, {message: 'Ok.', id: 0});
            scope.reorder();
            $httpBackend.flush();
        }));
        
        it('should send an HTTP REORDER request to menu item', function() {
            $httpBackend.verifyNoOutstandingExpectation();
            $httpBackend.verifyNoOutstandingRequest();
        }); 
    });
    
});