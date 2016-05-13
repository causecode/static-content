/* global models, augment */

'use strict';

models.factory('NewsModel', ['BaseModel', function(BaseModel) {
    var NewsModel = augment(BaseModel, function (uber) {
        var clazz;

        this.resourceName = 'news';

        this.constructor = function(data) {
            clazz = uber.constructor.call(this, data);
            this.postConstruct();
            return clazz;
        };

        this.postConstruct = function() {
        
            clazz.prototype.toHTMLTitle = function() {
                return '<a ui-sref="urlMap.resource({ctrl: \'news\', action: \'show\', resource: ' + this.id + '})">' + this.title + '</a>';
            };
            
            clazz.getClazzName = function() {
                return 'NewsModel';
            };
        
            clazz.getColumnNames = function() {
                return ['title', 'subTitle', 'publish'];
            };
        
            clazz.getSortProperties = function() {
                return [];
            };
        };
    });

    // Returning the resource. This is not really a instance.
    return new NewsModel({});
}]);
