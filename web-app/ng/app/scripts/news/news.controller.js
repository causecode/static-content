/* global controllers*/

'use strict';

controllers.controller('NewsController', ['$scope', 'NewsModel', function($scope, NewsModel) {

    if ($scope.actionName === 'create') {
        $scope.contentInstance = new NewsModel();
    } else if (['edit', 'show'].indexOf($scope.actionName) > -1) {
        $scope.contentInstance = NewsModel.get({id: $scope.id});
    }
}]);