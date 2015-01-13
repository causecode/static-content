/* global describe, it, sinon, inject, beforeEach, expect*/
'use strict';

describe('Testing BlogController: ', function() {
    beforeEach(module('ngApp'));
    
    var scope, ctrl, BlogModel, requsetHandler, appService, BaseModel;
    var $httpBackend, $rootScope, $state, $modal;
    
    beforeEach( inject( function(_$rootScope_, _$state_, _BlogModel_, _appService_, _BaseModel_, _$modal_){
        $rootScope = _$rootScope_;
        $state = _$state_;
        BlogModel = _BlogModel_;
        appService = _appService_;
        BaseModel = _BaseModel_;
        $modal = _$modal_;
    }));
    
    beforeEach( inject( function($rootScope, $controller) {
        scope = $rootScope.$new();
        scope.actionName = 'create';
        ctrl = $controller('BlogController', {$scope: scope});
        appService.showAlertMessage = function(message) {
            scope.alertMessage = message;
        }
        appService.blockPage = function() {
            return;
        }
    }));
   
    describe('Testing delete blog comment and remove comment methods', function() {
        var blogId = 1, commentId = '1';
        var comment = {id:1, comments:[]}
        beforeEach( inject( function($injector) {
            scope.comments = [];
            scope.comments.push(comment);
            $httpBackend = $injector.get('$httpBackend');
            $httpBackend.when('DELETE', '/api/v1/comment/1?blogId=1').respond(200, {message: 'Ok.'});
        }));

        it('Should send DELETE request to BlogConroller', function() {
            scope.deleteBlogComment(blogId, commentId);
            $httpBackend.flush();
            $httpBackend.verifyNoOutstandingExpectation();
            $httpBackend.verifyNoOutstandingRequest();
        }); 

        it('expect lenght of comments zero after deletion of comment', function() {
            scope.deleteBlogComment(blogId, commentId);
            $httpBackend.flush();
            expect(scope.comments.length).to.equal(0);
        }); 
    });
    
    describe('Testing comment method', function() {
        beforeEach( inject( function($injector) {
            scope.comments = [];
            scope.blogInstance = {id: 1};
            scope.commentData = {id:undefined, commets:[]};
            $httpBackend = $injector.get('$httpBackend');
            $httpBackend.expect('POST', '/api/v1/blog/action/comment?id=1').respond(200);
        }));

        // When comment is added to Blog
        it('Should add comment to comments', function() {
            scope.comment();
            $httpBackend.flush();
            expect(scope.comments.length).to.equal(1);
        }); 

        // When comment is added to another comment
        it('Should add comment to another comment', function() {
            scope.comments.push({id: 1});
            scope.commentData = {id:2, commentId:1, comments: []}
            scope.comment();
            $httpBackend.flush();
            expect(scope.comments.length).to.equal(1);
        }); 
    });
    
    describe('Testing change page', function() {
        beforeEach( inject( function($rootScope, $controller) {
            scope.list = function() {
            }
        }));
        it('Should change offset', function() {
            scope.itemsPerPage = 10;
            var toPage = 2;
            scope.changePage(toPage);
            expect(scope.offset).to.equal(20);
        }); 
    });
    
    describe('Testing fetchBlog method', function() {
        var blogId = 1;
        var blogData = {blogInstance: {id: 1}};
        
        beforeEach(inject( function($injector) {
            $httpBackend = $injector.get('$httpBackend');
        }));
        
        it('should send HTTP GET request to fetch blog', function() {
            $httpBackend.expectGET('/api/v1/blog/1').respond(200, blogData);
            scope.fetchBlog(blogId); 
            $httpBackend.flush();
            $httpBackend.verifyNoOutstandingRequest();
        });    
        
        it('should fetch blog into blogInstance', function() {
            $httpBackend.when('GET', '/api/v1/blog/1').respond(200, blogData);
            scope.fetchBlog(blogId); 
            $httpBackend.flush();
            expect(scope.blogInstance.id).to.equal(blogId);
        });    
    });
    
    describe('Testing show action', function() {
        beforeEach(inject( function($injector, $controller) {
            scope.controllerName = "blog";
            scope.actionName = "show";
            ctrl = $controller('BlogController', {$scope: scope});
            $httpBackend = $injector.get('$httpBackend');
        }));
        
        it('should call fetchBlog method', function() {
            var blogData = {blogInstance: {id: 1}};
            $httpBackend.expectGET('/api/v1/blog').respond(200, blogData);
            $httpBackend.flush();
            $httpBackend.verifyNoOutstandingRequest();
        }); 
    });
});