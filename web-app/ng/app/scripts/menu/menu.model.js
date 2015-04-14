'use strict';

/**
 * @ngdoc service
 * @name models.MenuModel
 * @requires $http
 * @requires BaseModel
 */
models.factory('MenuModel', ['BaseModel', function(BaseModel) {
    var MenuModel = augment(BaseModel, function (uber) {
        var clazz;

        this.resourceName = 'menu';

        this.constructor = function(data) {
            clazz = uber.constructor.call(this, data);
            this.postConstruct();
            return clazz;
        };

        this.customActions = {
            getRoleList: {
                method: 'POST',
                url: '/api/v1/menu/action/create'
            }
        };

        this.postConstruct = function() {

            clazz.prototype.toHTMLName = function() {
                return '<a ui-sref="urlMap.resource({ctrl: \'menu\', action: \'show\', resource: ' + this.id + '})">' + this.name + '</a>';
            };

            clazz.getClazzName = function() {
                return 'MenuModel';
            };

            clazz.getColumnNames = function() {
                return ['name', 'dateCreated', 'lastUpdated'];
            };

            clazz.getSortProperties = function() {
                return [];
            };
        };
    });

    // Returning the resource. This is not really a instance.
    return new MenuModel({});
}]);
