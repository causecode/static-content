/* global describe, it, sinon, inject, beforeEach, expect*/
'use strict';

describe('Testing FAQModel: ', function() {
    beforeEach(module('ngApp'));
    
    var FAQModel, FAQModelInstance;
    
    beforeEach( inject( function(_FAQModel_){
        FAQModel = _FAQModel_;
        FAQModelInstance = new FAQModel();
    }));
    
    it ('should return resource name initialised in FAQModel.', function() {
        expect(FAQModel.resourceName).to.equal('FAQ');
    });

    it ('should return clazz name initialised in FAQModel.', function() {
        expect(FAQModel.getClazzName()).to.equal('FAQModel');
    });

    it ('should return column names initialised in FAQModel.', function() {
        expect(FAQModel.getColumnNames()).to.deep.equal(['title','subTitle','body','publish']);
    });

    it ('should return sort properties initialised in FAQModel.', function() {
        expect(FAQModel.getSortProperties()).to.deep.equal([]);
    });
    
});