/* global controllers*/

'use strict';

controllers.controller('TextFormatController', ['$scope', '$state', 'TextFormatModel', 'appService', 
    function($scope, $state, TextFormatModel, appService) {
    console.log('TextFormatController executing.', $scope);
    
    if ($scope.actionName === 'create') {
        $scope.textFormatInstance = new TextFormatModel();

    } else if (['edit', 'show'].indexOf($scope.actionName) > -1) {
    	 $scope.textFormatInstance = TextFormatModel.get({id: $scope.id});
         $scope.$watch('id', function(newId, oldId) {
             if (newId && oldId && newId !== oldId) {
                 $scope.textFormatInstance = TextFormatModel.get({id: $scope.id});
             }
         });
    }
}]);
