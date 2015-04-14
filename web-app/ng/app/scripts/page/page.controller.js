'use strict';

/**
 * @ngdoc controller
 * @name PageController
 * 
 * @description
 * Provides CRUD operations for Page.
 * 
 * @requires $scope
 * @requires PageModel
 * @requires PageLayoutModel
 * @requires appService
 */
controllers.controller('PageController', ['$scope', 'PageModel','PageLayoutModel', 'appService', '$http', '$state',
    function($scope, PageModel, PageLayoutModel, appService, $http, $state) {

    /**
     * @ngdoc method
     * @methodOf PageController
     * @name fetchPage
     * 
     * @param {Number} pageId ID of Page instance 
     * 
     * @description 
     * Fetch page instance with given pageId.
     */
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

    // Get meta list
    PageModel.getMetaList(null,function(data){
        $scope.metaList = data.metaTypeList;
    },function(){});

    // Get page layout list
    PageLayoutModel.getPageLayoutList(null,function(data){
        $scope.pageLayoutList = data.pageLayoutList;
    },function(){});

    /**
     * @ngdoc method
     * @methodOf PageController
     * @name addForm
     * 
     * @description 
     * Creates an Object and push it into metaList, so a new select box and text box will appear where user can 
     * submit meta information.
     */
    $scope.addForm = function() {
        $scope.contentInstance.metaList.push({});
    };

    // If action is show, then fetch page with given id and show.
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
}]);