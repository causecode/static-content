/* global controllers*/

'use strict';

controllers.controller('MenuController', ['$scope', '$state', 'MenuModel', 'appService', '$modal',
    function($scope, $state, MenuModel, appService, $modal) {

    if ($scope.actionName === 'create') {
        $scope.menuInstance = new MenuModel();
        
    } else if (['edit', 'show'].indexOf($scope.actionName) > -1) {
        $scope.menuInstance = {};
        MenuModel.get({id: $scope.id}, function(data) {
        	$scope.menuInstance = data.menuInstance;
        });
    }
    
    MenuModel.getRoleList(null,function(data){
    	$scope.roleList = data.roleList;
    },function(){});
}]);