/* global controllers*/

'use strict';

controllers.controller('PageController', ['$scope', 'PageModel','PageLayoutModel', '$state', 'appService', '$location',
        function($scope, PageModel, PageLayoutModel, $state, appService, $location) {

    var subject = $location.search().subject;

    function getPageModelInstance() {
        PageModel.get({id: $scope.id, subject: subject}, function(pageInstance) {
            $scope.pageInstance = pageInstance;
        }, function(resp) {
            // Redirecting to Home state with alert on Error.
            $state.go('home');
            appService.showAlertMessage(resp.data.message, 'danger');
        });
    }

    if (($scope.controllerName === 'page') && ($scope.actionName === 'show')) {
        getPageModelInstance();
        $scope.$watch('id', function(newId, oldId) {
            if (newId && oldId && newId !== oldId) {
                getPageModelInstance();
            }
        });
    }

    if ($scope.actionName === 'create') {
        $scope.contentInstance = new PageModel();
        $scope.contentInstance.metaList = [];
    } else if (['edit'].indexOf($scope.actionName) > -1) {
        PageModel.get({id: $scope.id}, function(pageData) {
            $scope.contentInstance = pageData;
            $scope.contentInstance.metaList = [];
            if (pageData.pageLayout) {
                PageLayoutModel.get({id: pageData.pageLayout.id}, function(pageLayoutInstance) {
                    $scope.contentInstance.pageLayout = pageLayoutInstance.id;
                });
            }
        });
    }

    PageModel.getMetaList(null, function(data){
        $scope.metaList = data.metaTypeList;
    },function() {});

    PageLayoutModel.getPageLayoutList(null, function(data){
        $scope.pageLayoutList = data.pageLayoutList;
    },function() {});

    $scope.addForm = function() {
        $scope.contentInstance.metaList.push({});
    };
}]);