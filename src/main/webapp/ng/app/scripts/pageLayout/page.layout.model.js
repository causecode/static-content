/* global models, augment */

'use strict';

models.factory('PageLayoutModel', ['BaseModel', function(BaseModel) {
    var PageLayoutModel = augment(BaseModel, function (uber) {
        var clazz;

        this.resourceName = 'pageLayout';

        this.constructor = function(data) {
            clazz = uber.constructor.call(this, data);
            this.postConstruct();
            return clazz;
        };

        this.customActions = {
            getPageLayoutList: {
                method: 'GET',
                url: '/api/v1/pageLayout/action/getPageLayoutList'
            }
        };

        this.postConstruct = function() {

            clazz.prototype.toHTMLLayoutName = function() {
                var html = ' <a href="#/pageLayout/show/' + this.id + '">' + this.layoutName + '</a>';
                return html;
            };

            clazz.getClazzName = function() {
                return 'PageLayoutModel';
            };

            clazz.getColumnNames = function() {
                return ['layoutName'];
            };

            clazz.getSortProperties = function() {
                return ['layoutName'];
            };
        };
    });

    // Returning the resource. This is not really a instance.
    return new PageLayoutModel({});
}]);
