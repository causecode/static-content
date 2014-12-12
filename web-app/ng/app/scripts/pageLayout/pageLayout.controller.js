/* global controllers*/

'use strict';

controllers.controller('PageLayoutController', ['$scope', '$state', 'PageLayoutModel', 'appService', '$modal',
    function($scope, $state, PageLayoutModel, appService, $modal) {
    console.info('PageLayoutController executing.', $scope);


    $scope.createPageLayout = function() {
    		
    		PageLayoutModel.createPageLayout({layoutName: $scope.pageLayout.name},{
    			layoutName: $scope.pageLayout.name
    		}, function(data) {
    			appService.showAlertMessage('Page Layout created successfully.', 'info');
    		}, function(data) {
    			appService.showAlertMessage('Unable to create Page Layout.', 'danger');
    		});
    };
}]);