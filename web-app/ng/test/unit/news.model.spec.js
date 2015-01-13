/* global describe, it, sinon, inject, beforeEach, expect*/
'use strict';

describe('Testing NewsModel: ', function() {
    beforeEach(module('ngApp'));
    
    var NewsModel, NewsModelInstance;
    
    beforeEach( inject( function(_NewsModel_){
        NewsModel = _NewsModel_;
        NewsModelInstance = new NewsModel();
    }));
    
    it ('should return resource name initialised in NewsModel.', function() {
        expect(NewsModel.resourceName).to.equal('news');
    });

    it ('should return clazz name initialised in NewsModel.', function() {
        expect(NewsModel.getClazzName()).to.equal('NewsModel');
    });

    it ('should return column names initialised in NewsModel.', function() {
        expect(NewsModel.getColumnNames()).to.deep.equal(['title', 'subTitle', 'publish']);
    });

    it ('should return sort properties initialised in NewsModel.', function() {
        expect(NewsModel.getSortProperties()).to.deep.equal([]);
    });
    
});