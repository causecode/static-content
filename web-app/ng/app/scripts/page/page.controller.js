/* global controllers*/

'use strict';

controllers.controller('PageController', ['$scope', 'PageModel','PageLayoutModel', 
        function($scope, PageModel, PageLayoutModel) {

    if (($scope.controllerName === 'page') && ($scope.actionName === 'show')) {
        $scope.pageInstance = PageModel.get({id: $scope.id});
        $scope.$watch('id', function(newId, oldId) {
            if (newId && oldId && newId !== oldId) {
                $scope.pageInstance = PageModel.get({id: $scope.id});
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