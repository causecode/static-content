/* global controllers*/

'use strict';

controllers.controller('FAQController', ['$scope', '$state', 'FAQModel', 'appService', '$modal',
    function($scope, $state, FAQModel, appService, $modal) {
    console.info('FAQController executing.', $scope);


    $scope.createFAQ = function() {
    		
    		FAQModel.createFAQ(null,{
    			title: $scope.faq.title,
    			subTitle:$scope.faq.subTitle,
    			body:$scope.faq.body,
    			author:$scope.faq.author,
    			publish:$scope.faq.publish
    		}, function(data) {
    			appService.showAlertMessage('FAQ created successfully.', 'info');
    		}, function(data) {
    			appService.showAlertMessage('Unable to create FAQ.', 'danger');
    		});
    };
}]);