'use strict';

/**
 * @ngdoc controller
 * @name PageLayoutController
 * @description 
 * Provides CRUD operations for Page Layout.
 * @requires $scope
 * @requires PageLayoutModel
 */
controllers.controller('PageLayoutController', ['$scope', 'PageLayoutModel', function($scope, $state, PageLayoutModel) {
    if ($scope.actionName === 'create') {
        $scope.pageLayoutInstance = new PageLayoutModel();
    } else if (['edit', 'show'].indexOf($scope.actionName) > -1) {
        $scope.pageLayoutInstance = PageLayoutModel.get({id: $scope.id});
    }
}]);