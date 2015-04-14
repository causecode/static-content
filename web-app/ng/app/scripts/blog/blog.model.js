'use strict';

/**
 * @ngdoc service
 * @name models.BlogModel
 * @requires models.BaseModel
 */
 models.factory('BlogModel', ['BaseModel', function(BaseModel) {
    var BlogModel = augment(BaseModel, function (uber) {
        var clazz;

        this.resourceName = 'blog';

        this.customActions = {
            deleteComment: {
                method: 'DELETE',
                url: '/api/v1/comment/:id'
            }
        };

        this.constructor = function(data) {
            clazz = uber.constructor.call(this, data);

            this.postConstruct();
            return clazz;
        };

        this.postConstruct = function() {
            clazz.prototype.toHTMLName = function() {
                return '<a href="#/blog/show/' + this.id + '">' + this.name + '</a>';
            };

            clazz.getColumnNames = function() {
                return ['name', 'dateCreated'];
            };

            clazz.getSortProperties = function() {
                return [];
            };
        };
    });

    // Returning the resource. This is not really a instance.
    return new BlogModel({});
}]);
