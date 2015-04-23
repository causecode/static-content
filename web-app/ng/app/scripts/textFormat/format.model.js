'use strict'

models.factory('TextFormatModel', ['BaseModel', function(BaseModel) {
    var TextFormatModel = augment(BaseModel, function (uber) {
        var clazz;

        this.resourceName = 'textFormat';

        this.constructor = function(data) {
            clazz = uber.constructor.call(this, data);
            this.postConstruct();
            return clazz;
        };

        this.customActions = {
                getEditorType: {
                    url: '/api/v1/textFormat/action/getEditorType',
                    method : 'GET',
                    isArray : false //since we want the Server to return an Object, not an Array
                }
        };

        this.postConstruct = function() {

            clazz.prototype.toHTMLName = function() {
                return '<a ui-sref="urlMap.resource({ctrl: \'textFormat\', action: \'show\', resource: ' + this.id + '})">' + 
                this.name + '</a>  ' + 
                '<a href="" ui-sref="urlMap.resource({ctrl: \'textFormat\', action: \'edit\', resource: ' + 
                this.id + '})"><i class="fa fa-edit"></i></a>';
            };

            clazz.getClazzName = function() {
                return 'TextFormatModel';
            };


            clazz.getColumnNames = function() {
                return ['name', 'roles', 'allowedTags', 'editor'];	// col name(same as Domain properties) will be ultimately converted to space separated name in the View 
            };

            clazz.getSortProperties = function() {
                return [];
            };
        };
    });
    return new TextFormatModel({});
}]);
