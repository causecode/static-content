/* global models, augment */

'use strict';

models.factory('MenuItemModel', ['$resource', '$http', 'BaseModel','dateFilter', function($resource, $http, BaseModel,dateFilter) {
    var MenuItemModel = augment(BaseModel, function (uber) {
        var clazz;

        this.resourceName = 'menuItem';

        this.constructor = function(data) {
            clazz = uber.constructor.call(this, data);

            this.postConstruct();
            return clazz;
        };
        
        this.customActions = {
        		saveMenuItem: {
        			method: 'POST',
        			url: '/api/v1/menuItem/action/save'
        		},
        		reorder: {
        			method: 'POST',
        			url: '/api/v1/menuItem/action/reorder'
        		}
        };

        this.postConstruct = function() {
        	
        	clazz.getClazzName = function() {
        		return 'MenuItemModel';
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
    return new MenuItemModel({});
}]);
