/* global models, augment */

'use strict';

models.factory('FAQModel', ['$resource', '$http', 'BaseModel','dateFilter', function($resource, $http, BaseModel,dateFilter) {
    var FAQModel = augment(BaseModel, function (uber) {
        var clazz;

        this.resourceName = 'FAQ';

        this.constructor = function(data) {
            clazz = uber.constructor.call(this, data);
            this.postConstruct();
            return clazz;
        };
        
        this.postConstruct = function() {
        
            clazz.prototype.toHTMLTitle = function(scope) {
                return '<a ui-sref="urlMap.resource({ctrl: \'faq\', action: \'show\', resource: ' + this.id + '})">' + this.title + '</a>';
            };
            
            clazz.getClazzName = function() {
                return 'FAQModel';
            };
        
            clazz.getColumnNames = function() {
                return ['title','subTitle','body','publish'];
            };
        
            clazz.getSortProperties = function() {
                return [];
            };
        };
    });

    // Returning the resource. This is not really a instance.
    return new FAQModel({});
}]);
