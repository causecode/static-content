'use strict';

/**
 * @ngdoc controller
 * @name BlogController
 * @requires $scope
 * @requires $state
 * @requires BlogModel
 * @requires appService
 * @requires $modal
 */
controllers.controller('BlogController', ['$scope', '$state', 'BlogModel', 'appService', '$modal',
      function($scope, $state, BlogModel, appService, $modal) {
    console.info('BlogController executing.', $scope);

    $scope.commentData = {};
    $scope.currentPage = 1;
    $scope.offset = 0;

    /**
     * @ngdoc method
     * @methodOf BlogController
     * @name fetchBlog
     * 
     *  @param {Number} blogId id of Blog instance 
     *  
     *  @description
     *  Fetches blog data for given id of Blog instance. 
     */
    $scope.fetchBlog = function(blogId) {
        appService.blockPage(true);

        BlogModel.get({id: blogId}, function(blogData) {
            if (blogData.blogInstance) {
                $scope.blogInstance = blogData.blogInstance;
                $scope.commentData.id = blogData.blogInstance.id;
            }
            $scope.comments = blogData.comments;
            $scope.blogInstanceList = blogData.blogInstanceList;
            $scope.tagList = blogData.tagList;
            appService.blockPage(false);
        });
    };

    /**
     * @ngdoc method
     * @methodOf BlogController
     * @name changePage
     * 
     * @param {Object} toPage Page number
     * 
     * @description
     * Move to given page of blog list page.
     */
    $scope.changePage = function(toPage) {
        $scope.offset = $scope.itemsPerPage * toPage;
        $scope.list(toPage);
    };

    /**
     * @ngdoc method
     * @mthodOf BlogController
     * @name list
     * 
     * @param {Object} [toPage] page number. Default value is 1.
     * 
     * @description
     * Fetches list of blog for given page number.
     */
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

    /**
     * @ngdoc method
     * @methodOf BlogController
     * @name filtersBlogs
     * 
     * @param {Object} monthFilter
     * @param {Object} tag 
     * @param {Object} queryFilter
     * 
     * @description
     * Filters list of blog based on monthFilter, tag and queryFilter
     */
    $scope.filterBlogs = function(monthFilter, tag, queryFilter) {
        $state.go('urlMap', {ctrl: 'blog', action: 'list'});
        $scope.monthFilter = monthFilter !== '' ? monthFilter : $scope.monthFilter;
        $scope.tag = tag !== '' ? tag : $scope.tag;
        $scope.queryFilter = queryFilter !== '' ? queryFilter : $scope.queryFilter;
        $scope.list();
    };

    /**
     * @ngdoc method
     * @methodOf BlogController
     * @name deleteBlogComment
     * 
     * @param {String} blogId id of BlogInstance.
     * @param {String} commentId id of Comment instance.
     * 
     *  @description
     *  Deletes a comment on blog.
     */
    $scope.deleteBlogComment = function(blogId, commentId) {
        BlogModel.deleteComment({id: commentId, blogId: blogId}, function() {
            appService.showAlertMessage('Comment deleted successfully.', 'info');
            $scope.comments = removeComment($scope.comments, commentId);
        }, function() {
            appService.showAlertMessage('Unable to delete Comment.', 'danger');
        });
    };

    /**
     * @ngdoc method
     * @methodOf BlogController 
     * @name remocveComment
     * 
     * @param {Object} comments Array of all the comments on blog.
     * @param {String} comment id of Comment instance that is to be deleted.
     * 
     * @description
     * Removes a comment with given id, from all available comments. 
     */
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

    /**
     * @ngdoc method
     * @methodOf BlogController
     * @name showCommentOverlay
     * 
     * @param {Object} blogId id of Blog instance.
     * 
     * @description
     * Opens a pop up window, that allows user to enter comment. 
     */
    $scope.showCommentOverlay = function(blogId, commentId) {
        $scope.commentData.commentId = commentId;
        $scope.commentData.id = blogId;
        var commentModalInstance = $modal.open({
            scope: $scope,
            templateUrl: 'views/modals/add-comment.html',
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

    /**
     * @ngdoc method
     * @methodOf BlogController
     * @name addComment
     * 
     * @param {Object} comments Array of all comments
     * 
     * @description
     * Adds newly added comment to the comments list.
     * If a comment is added to another comment, then it will be 
     * added to 'comments' of the parent comment. However when a 
     * comment is added to a blog then comment is simply added to
     * 'comments'.
     */
    function addComment(comments) {
        // If comment on another comment
        if ($scope.commentData.commentId) {
            if (comments && comments.length === 0) { return []; };
            // Checking for each comment and its child comments in comments list
            // to find parent comment of newly added comment.
            angular.forEach(comments, function(comment, index) {
                if (comment.id.toString() === $scope.commentData.commentId) {
                    comment.comments.push($scope.commentData);
                    return;
                }
                comment.comments = addComment(comment.comments);
            });
        }
        // When commented on blog.
        else {
            $scope.comments.push($scope.commentData);
        }
        return comments;
    }

    /**
     * @ngdoc method
     * @methodOf BlogController
     * @name comment
     * 
     * @description
     * Adds a comment on either blog or another comment.
     */
    $scope.comment = function() {
        $scope.commentData.id = $scope.blogInstance.id;
        BlogModel.comment($scope.commentData, function() {
            $scope.comments = addComment($scope.comments);
            appService.showAlertMessage('Comment added successfully.', 'info', {element: '.modal .alert', makeStrong: false});
        }, function(resp) {
            appService.showAlertMessage(resp.data.message, 'danger', {element: '.modal .alert', makeStrong: false});
        });
    };

    // If action is show blog with given id.
    if (($scope.controllerName === 'blog') && ($scope.actionName === 'show')) {
        $scope.fetchBlog($scope.id);
        $scope.$watch('id', function(newId, oldId) {
            if (newId && oldId && newId !== oldId) {
                $scope.fetchBlog($scope.id);
            }
        });
    } else if ($scope.actionName === 'list') {
        $scope.list();
    }
}]);