/* global controllers*/

'use strict';

controllers.controller('FAQController', ['$scope', '$state', 'FAQModel', 'appService', '$modal',
    function($scope, $state, FAQModel, appService, $modal) {
    console.info('FAQController executing.', $scope);
    
    if ($scope.actionName === 'create') {
        $scope.contentInstance = new FAQModel();
        
    } else if (['edit', 'show'].indexOf($scope.actionName) > -1) {
        $scope.contentInstance = FAQModel.get({id: $scope.id});
    }
}]);