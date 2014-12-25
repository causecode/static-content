/* global models, augment */

'use strict';

models.factory('PageModel', ['$resource', '$http', 'BaseModel', function($resource, $http, BaseModel) {
    var PageModel = augment(BaseModel, function (uber) {
        var clazz;

        this.resourceName = 'page';

        this.constructor = function(data) {
            clazz = uber.constructor.call(this, data);

            this.postConstruct();
            return clazz;
        };

        this.customActions = {
        	getMetaList :{
        		method: 'GET',
        		url: '/api/v1/page/action/getMetaTypeList'
        	}
        }
        
        this.postConstruct = function() {
        	
        	clazz.getClazzName = function() {
        		return 'PageModel';
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
    return new PageModel({});
}]);
