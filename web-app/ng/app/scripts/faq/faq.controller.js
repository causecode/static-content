'use strict';

/**
 * @ngdoc controller
 * @name FAQController
 * 
 * @description
 * Provides CRUD operations for FAQ.
 * 
 * @requires $scope
 * @requires FAQModel
 */
controllers.controller('FAQController', ['$scope', 'FAQModel', function($scope, FAQModel) {
    if ($scope.actionName === 'create') {
        $scope.contentInstance = new FAQModel();
    } else if (['edit', 'show'].indexOf($scope.actionName) > -1) {
        $scope.contentInstance = FAQModel.get({id: $scope.id});
    }
}]);