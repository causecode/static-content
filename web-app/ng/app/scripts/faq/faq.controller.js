/* global controllers*/

'use strict';

controllers.controller('FAQController', ['$scope', 'FAQModel', 'appService', '$modal',
    function($scope, FAQModel, appService, $modal) {
    if ($scope.actionName === 'create') {
        $scope.contentInstance = new FAQModel();
    } else if (['edit', 'show'].indexOf($scope.actionName) > -1) {
        $scope.contentInstance = FAQModel.get({id: $scope.id});
    }
}]);