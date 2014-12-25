/* global controllers*/

'use strict';

controllers.controller('PageLayoutController', ['$scope', '$state', 'PageLayoutModel', 'appService', '$modal',
    function($scope, $state, PageLayoutModel, appService, $modal) {
    
    if ($scope.actionName === 'create') {
        $scope.pageLayoutInstance = new PageLayoutModel();
    } else if (['edit', 'show'].indexOf($scope.actionName) > -1) {
        $scope.pageLayoutInstance = PageLayoutModel.get({id: $scope.id});
    }
}]);