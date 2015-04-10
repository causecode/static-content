/* global controllers*/

'use strict';

controllers.controller('BlogController', ['$scope', '$state', 'BlogModel', 'appService', '$modal',
    function($scope, $state, BlogModel, appService, $modal) {
    console.info('BlogController executing.', $scope);

    $scope.commentData = {};
    $scope.currentPage = 1;
    $scope.offset = 0;

    $scope.fetchBlog = function(blogId) {
        appService.blockPage(true);

        BlogModel.get({id: blogId}, function(blogData) {
            if (blogData.blogInstance) {
                $scope.blogInstance = new BlogModel(blogData.blogInstance);
                $scope.contentInstance = $scope.blogInstance;
                $scope.commentData.id = blogData.blogInstance.id;
            }
            $scope.comments = blogData.comments;
            $scope.blogInstanceList = blogData.blogInstanceList;
            $scope.tagList = blogData.tagList;
            appService.blockPage(false);
        });
    };

    $scope.changePage = function(toPage) {
        $scope.offset = $scope.itemsPerPage * toPage;
        $scope.list(toPage);
    };

    $scope.list = function(toPage) {
        toPage = toPage ? toPage : 1;
        var params = {
            max: $scope.itemsPerPage,
            offset: $scope.offset,
            monthFilter: encodeURIComponent($scope.monthFilter),
            tag: encodeURIComponent($scope.tag),
            queryFilter: encodeURIComponent($scope.queryFilter)
        };
        $scope.blogListPromise = BlogModel.query(params, function(data) {
            $scope.currentPage = toPage;
            $scope.blogInstanceList = data.blogInstanceList;
            $scope.blogInstanceTotal = data.blogInstanceTotal;
            $scope.monthFilterList = data.monthFilterList;
            $scope.tagList = data.tagList;
        });
    };

    $scope.filterBlogs = function(monthFilter, tag, queryFilter) {
        $state.go('urlMap', {ctrl: 'blog', action: 'list'});
        $scope.monthFilter = monthFilter !== '' ? monthFilter : $scope.monthFilter;
        $scope.tag = tag !== '' ? tag : $scope.tag;
        $scope.queryFilter = queryFilter !== '' ? queryFilter : $scope.queryFilter;
        $scope.list();
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
        var commentModalInstance = $modal.open({
            scope: $scope,
            templateUrl: 'views/blog/add-comment.html',
            size: 'sm',
            keyboard: false,
            backdrop: 'static'
        });

        commentModalInstance.result.then(function () {
            $scope.commentData = {};
        }, function () {
            console.log('Modal dismissed at: ' + new Date());
        });
    };

    function addComment(comments) {
        if ($scope.commentData.commentId) {
            if (comments && comments.length === 0) { return []; }
            angular.forEach(comments, function(comment, index) {  // jshint ignore:line
                if (comment.id.toString() === $scope.commentData.commentId) {
                    comment.comments.push($scope.commentData);
                    return;
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
        BlogModel.comment($scope.commentData, function() {
            $scope.comments = addComment($scope.comments);
            appService.showAlertMessage('Comment added successfully.', 'info', {element: '.modal .alert', makeStrong: false});
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
    } else if ($scope.actionName === 'list') {
        $scope.list();
    } else if ($scope.actionName === 'create') {
        $scope.contentInstance = new BlogModel();
        $scope.contentInstance.metaList = [];
    } else if ($scope.actionName === 'edit') {
        $scope.contentInstance = BlogModel.get({id: $scope.id});
    }
}]);