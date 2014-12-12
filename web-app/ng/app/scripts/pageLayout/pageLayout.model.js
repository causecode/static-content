/* global models, augment */

'use strict';

models.factory('PageLayoutModel', ['$resource', '$http', 'BaseModel','dateFilter', function($resource, $http, BaseModel,dateFilter) {
    var PageLayoutModel = augment(BaseModel, function (uber) {
        var clazz;

        console.log('In Page layout model.. :-)');
        this.resourceName = 'pageLayout';

        this.customActions = {
            createPageLayout: {
                method: 'POST',
                url: '/api/v1/pageLayout/action/save'
            }
        };

        this.constructor = function(data) {
            clazz = uber.constructor.call(this, data);

            this.postConstruct();
            return clazz;
        };

        this.postConstruct = function() {
        	
        	clazz.getClazzName = function() {
        		return 'PageLayoutModel';
        	};
        	
        	clazz.getColumnNames = function() {
        		return ['Name'];
        	};
        	
        	clazz.getSortProperties = function() {
        		return ['Name'];
        	};
        };
    });

    // Returning the resource. This is not really a instance.
    return new PageLayoutModel({});
}]);
