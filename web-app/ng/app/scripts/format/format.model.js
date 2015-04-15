'use strict'

models.factory('TextFormatModel' ['BaseModel', function(BaseModel) {
	  var FormatModel = augment(BaseModel, function (uber) {
	        var clazz;

	        this.resourceName = 'textFormat';		//name of the REST resource which the Server will recognize

	        this.constructor = function(data) {
	            clazz = uber.constructor.call(this, data);

	            this.postConstruct();
	            return clazz;
	        };


	        this.postConstruct = function() {
	            /**/
	            clazz.getColumnNames = function() {
	                return ['name', 'roles', 'allowed_tags', 'editor'];	
	            };

	            clazz.getSortProperties = function() {
	                return [];
	            };
	        };
	    });
	  return new TextFormatModel({});
  }]);
