'use strict';

controllers.controller('PageLayoutController', ['$scope', 'PageLayoutModel', function($scope, $state, PageLayoutModel) {

    if ($scope.actionName === 'create') {
        $scope.pageLayoutInstance = new PageLayoutModel();
    } else if (['edit', 'show'].indexOf($scope.actionName) > -1) {
        $scope.pageLayoutInstance = PageLayoutModel.get({id: $scope.id});
    }
}]);