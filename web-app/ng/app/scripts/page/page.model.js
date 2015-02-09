/* global models, augment */

'use strict';

/**
 * @ngdoc overview
 * @name PageModel
 * @requires BaseModel
 */
models.factory('PageModel', ['BaseModel', function(BaseModel) {
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
        };

        this.postConstruct = function() {

            clazz.prototype.toHTMLTitle = function() {
                return '<a ui-sref="urlMap.resource({ctrl: \'page\', action: \'show\', resource: ' + this.id + '})">' + 
                    this.title + '</a>  ' + 
                    '<a href="" ui-sref="urlMap.resource({ctrl: \'page\', action: \'edit\', resource: ' + 
                    this.id + '})"><i class="fa fa-edit"></i></a>';
            };

            clazz.getClazzName = function() {
                return 'PageModel';
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
    return new PageModel({});
}]);
