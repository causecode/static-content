/* global models, augment */

'use strict';

models.factory('MenuModel', ['$resource', '$http', 'BaseModel', function($resource, $http, BaseModel) {
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
        	
        	clazz.getClazzName = function() {
        		return 'MenuModel';
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
    return new MenuModel({});
}]);
