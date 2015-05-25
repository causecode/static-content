/* global controllers*/

'use strict';

controllers.controller('BlogController', ['$scope', '$state', 'BlogModel', 'appService', '$modal', 'PageModel', '$timeout',
        '$location', function($scope, $state, BlogModel, appService, $modal, PageModel, $timeout, $location) {
    console.info('BlogController executing.', $scope);

    $scope.commentData = {};
    $scope.currentPage = 1;
    $scope.offset = 0;

    $scope.fetchBlog = function(blogId) {
        appService.blockPage(true);

        BlogModel.get({id: blogId}, function(blogData) {
            if (blogData.blogInstance) {
                $scope.blogInstance = new BlogModel(blogData.blogInstance);
                /*
                 * TODO: Remove this duplicate scope variable dependency.
                 * Copying same blogInstance into another scope variable to reuse
                 * existing partials added for creating contentInstnace.
                 */
                $scope.contentInstance = $scope.blogInstance;
                $scope.contentInstance.metaList = [];
                $scope.commentData.id = blogData.blogInstance.id;
            }
            $scope.comments = blogData.comments;
            $scope.instanceList = blogData.blogInstanceList;
            $scope.tagList = blogData.tagList;
            appService.blockPage(false);
        });
    };

    $scope.addForm = function() {
        $scope.contentInstance.metaList.push({});
    };

    PageModel.getMetaList(null, function(data){
        $scope.metaList = data.metaTypeList;
    }, function() {});

    /*
     * Initializing response returned by query ajax call from paged list directive in 
     * current controllers scope.
     */
    $scope.initialiseGetListResponse = function(data) {
        if (data) {
            $scope.instanceList = data.instanceList;
            $scope.monthFilterList = data.monthFilterList;
            $scope.tagList = data.tagList;
        }
    };

    $scope.filterBlogs = function(monthFilter, tag, queryFilter) {
        $scope.monthFilter = monthFilter !== '' ? monthFilter : $scope.monthFilter;
        $scope.tag = tag !== '' ? tag : $scope.tag;
        $scope.queryFilter = queryFilter !== '' ? queryFilter : $scope.queryFilter;
        $location.search({monthFilter: $scope.monthFilter, tag: $scope.tag, queryFilter: $scope.queryFilter});
        // Redirecting to List page if filters applied on show page.
        if ($scope.actionName === 'show') {
            $state.go('urlMap', {ctrl: 'blog', action: 'list'});
        }
    };

    $scope.deleteBlogComment = function(blogId, commentId) {
        BlogModel.deleteComment({id: commentId, blogId: blogId}, function() {
            appService.showAlertMessage('Comment deleted successfully.', 'info');
            $scope.comments = removeComment($scope.comments, commentId);
        }, function() {
            appService.showAlertMessage('Unable to delete Comment.', 'danger');
        });
    };

    function removeComment(comments, commentId) {
        if (comments.length === 0) { return []; }
        angular.forEach(comments, function(comment, index) {
            if (comment.id.toString() === commentId) {
                comments.splice(index, 1);
                return comments;
            }
            comment.comments = removeComment(comment.comments, commentId);
        });
        return comments;
    }

    $scope.showCommentOverlay = function(blogId, commentId) {
        $scope.commentData.commentId = commentId;
        $scope.commentData.id = blogId;
        $scope.commentModalInstance = $modal.open({
            scope: $scope,
            templateUrl: 'views/blog/add-comment.html',
            size: 'sm',
            keyboard: false,
            backdrop: 'static'
        });

        $scope.commentModalInstance.result.then(function () {
            $scope.commentData = {};
        }, function () {
            $scope.commentData = {};
            console.log('Modal dismissed at: ' + new Date());
        });
    };

    function addComment(comments) {
        if ($scope.commentData.commentId) {
            if (comments && comments.length === 0) { return []; }
            angular.forEach(comments, function(comment, index) {  // jshint ignore:line
                if (comment.id.toString() === $scope.commentData.commentId) {
                    if (comment.comments) {
                        comment.comments.push($scope.commentData);
                    } else {
                        comment.comments = [$scope.commentData];
                    }
                }
                comment.comments = addComment(comment.comments);
            });
        } else {
            $scope.comments.push($scope.commentData);
        }
        return comments;
    }

    $scope.comment = function() {
        $scope.commentData.id = $scope.blogInstance.id;
        BlogModel.comment($scope.commentData, null, function() {
            $scope.comments = addComment($scope.comments);
            appService.showAlertMessage('Comment added successfully.', 'info', {element: '.modal .alert', makeStrong: false});
            // Auto hide the modal after 5 seconds
            $timeout(function() {
                $scope.commentModalInstance.dismiss();
            }, 5000);
        }, function(resp) {
            appService.showAlertMessage(resp.data.message, 'danger', {element: '.modal .alert', makeStrong: false});
        });
    };

    if (($scope.controllerName === 'blog') && (['edit', 'show'].indexOf($scope.actionName) > -1)) {
        $scope.fetchBlog($scope.id);
        $scope.$watch('id', function(newId, oldId) {
            if (newId && oldId && newId !== oldId) {
                $scope.fetchBlog($scope.id);
            }
        });
    } else if ($scope.actionName === 'create') {
        $scope.contentInstance = new BlogModel();
        $scope.contentInstance.metaList = [];
    }
}]);