/* global controllers*/

'use strict';

controllers.controller('PageController', ['$scope', '$http', '$state', 'appService','PageModel','PageLayoutModel', function($scope, $http, $state, appService, PageModel, PageLayoutModel) {

    $scope.fetchPage = function(pageId) {
        appService.blockPage(true);

        $http({
            method : 'GET',
            url : '/api/v1/page/action/show?id=' + pageId
        }).success(function(data) {
            if (data.pageInstance) {
                $scope.pageInstance = data.pageInstance;
            } else {
                $state.go('home');
                console.error('Unable to fetch page.');
            }
            appService.blockPage(false);
        });
    };

    if (($scope.controllerName === 'page') && ($scope.actionName === 'show')) {
        $scope.fetchPage($scope.id);
        $scope.$watch('id', function(newId, oldId) {
            if (newId && oldId && newId !== oldId) {
                $scope.fetchPage($scope.id);
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
            PageLayoutModel.get({id: pageData.pageLayout.id}, function(pageLayoutInstance) {
                $scope.contentInstance.pageLayout = pageLayoutInstance.layoutName;
                console.info("page is", pageData.publish);
            });
        });
    }
    
	PageModel.getMetaList(null,function(data){
    	$scope.metaList = data.metaTypeList;
    },function(){});
    
    PageLayoutModel.getPageLayoutList(null,function(data){
    	$scope.pageLayoutList = data.pageLayoutList;
    },function(){});
    
    $scope.init = function() {
    }
    
    $scope.addForm = function() {
    	$scope.contentInstance.metaList.push({});
    }
}]);