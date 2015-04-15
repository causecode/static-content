/* global controllers*/

'use strict';
// same name as the TextFormat container inside grails
controllers.controller('TextFormatController', ['$scope', '$state', 'TextFormatModel', 'appService', 
    function($scope, $state, TextFormatModel, appService) {
    console.info('TextFormatController executing.', $scope);
    $scope.data= "hello world"
    $scope.save = function()
    {
    	
    }
    
    $scope.delete = function ()
    {
    	
    }

}]);
