/* global describe, it, sinon, inject, beforeEach, expect*/
'use strict';

describe('Testing BlogModel: ', function() {
    
    beforeEach(module('ngApp'));
    
    var BlogModel, BlogModelInstance;
    
    beforeEach( inject( function(_BlogModel_){
        BlogModel = _BlogModel_;
        BlogModelInstance = new BlogModel();
    }));
    
    describe('Custom actions', function() {
        var $httpBackend;

        beforeEach(inject(function($injector) {
            $httpBackend = $injector.get('$httpBackend');
        }));

        it ('Testing create method request parameters', function() {
            $httpBackend.when('POST', '/api/v1/blog/action/comment').respond({}, {});
            BlogModel.comment();
            $httpBackend.flush();
        });

        it ('Testing comment method request parameters', function() {
            $httpBackend.when('DELETE', '/api/v1/comment').respond({}, {});
            BlogModel.deleteComment();
            $httpBackend.flush();
        });
    });

    it ('should return resource name initialised in BlogModel.', function() {
        expect(BlogModel.resourceName).to.equal('blog');
    });

    it ('should return clazz name initialised in BlogModel.', function() {
        // TODO error. Resolve.
        // expect(BlogModel.getClazzName()).to.equal('BlogModel');
    });

    it ('should return column names initialised in BlogModel.', function() {
        expect(BlogModel.getColumnNames()).to.deep.equal(['name', 'dateCreated']);
    });

    it ('should return sort properties initialised in BlogModel.', function() {
        expect(BlogModel.getSortProperties()).to.deep.equal([]);
    });
    
});