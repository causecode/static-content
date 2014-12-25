/* global controllers*/

'use strict';

controllers.controller('NewsController', ['$scope', '$state', 'NewsModel', 'appService', '$modal',
    function($scope, $state, NewsModel, appService, $modal) {
	
    if ($scope.actionName === 'create') {
        $scope.contentInstance = new NewsModel();
        
    } else if (['edit', 'show'].indexOf($scope.actionName) > -1) {
        $scope.contentInstance = NewsModel.get({id: $scope.id});
    }
}]);