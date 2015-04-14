'use strict';

/**
 * @ngdoc controller
 * @name MenuController
 * 
 * @description
 * Provides CRUD operations for Menu.
 * 
 * @requires $scope
 * @requires MenuModel
 */

controllers.controller('MenuController', ['$scope', 'MenuModel', function($scope, MenuModel) {
    if ($scope.actionName === 'create') {
        $scope.menuInstance = new MenuModel();
    } else if (['edit', 'show'].indexOf($scope.actionName) > -1) {
        $scope.menuInstance = MenuModel.get({id: $scope.id});
    }

    MenuModel.getRoleList(null, function(data) {
        $scope.roleList = data.roleList;
    }, function() {});
}]);