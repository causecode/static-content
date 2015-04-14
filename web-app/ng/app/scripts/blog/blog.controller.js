'use strict';

/**
 * @ngdoc controller
 * @name controllers.BlogController
 * 
 * @description
 * Provides CRUD operations for Blog.
 * 
 * @requires $scope
 * @requires $state
 * @requires $modal
 * @requires BlogModel
 * @requires appService
 */
controllers.controller('BlogController', ['$scope', '$state', 'BlogModel', 'appService', '$modal',
      function($scope, $state, BlogModel, appService, $modal) {
    console.info('BlogController executing.', $scope);

    $scope.commentData = {};
    $scope.currentPage = 1;
    $scope.offset = 0;

    /**
     * @ngdoc $scope
     * @methodOf controllers.BlogController
     * @name controllers.BlogController#$scope.fetchBlog
     * 
     * @param {Number} blogId id of Blog instance. 
     * 
     * @description
     * Fetches blog data for the given id of the Blog instance.
     * 
     * @returns {*} Blog data which includes Blog instance, comments and tags on that blog.
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
     * @methodOf controllers.BlogController
     * @name changePage
     * 
     * @param {Number} toPage Page number.
     * 
     * @description
     * Used to perform pagination in Blog list page. Update offset value and fetch specified page records.
     */
    $scope.changePage = function(toPage) {
        $scope.offset = $scope.itemsPerPage * toPage;
        $scope.list(toPage);
    };

    /**
     * @ngdoc method
     * @methodOf controllers.BlogController
     * @name list
     * 
     * @param {Number} [toPage] page number. Default value is 1.
     * 
     * @description
     * Fetches list of blog's for the given page number with filter parameters.
     * Filter parameters passed are query, month and tag filter and pagination parameters.
     * 
     * @returns {*} Update scope with Blog list page data which includes Blog list, total blog's count, all month 
     * and tag filter list.
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
     * @methodOf controllers.BlogController
     * @name filtersBlogs
     * 
     * @param {Object} monthFilter month to filter blog's list.
     * @param {Object} tag tags to filter blog's list.
     * @param {Object} queryFilter query to filter blog's list.
     * 
     * @description
     * Filters list of blog's based on month, tag and query filter.
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
     * @methodOf controllers.BlogController
     * @name deleteBlogComment
     * 
     * @param {String} blogId id of Blog instance.
     * @param {String} commentId id of Comment instance.
     * 
     *  @description
     *  Deletes a comment on Blog.
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
     * @methodOf controllers.BlogController 
     * @name removeComment
     * 
     * @param {Object} comments Array of all the comments on blog.
     * @param {String} commentId ID of Comment instance that is to be deleted.
     * 
     * @description
     * Removes a comment with the given id, from all available comments. 
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
     * @methodOf controllers.BlogController
     * @name showCommentOverlay
     * 
     * @param {Object} blogId Id of Blog instance.
     * 
     * @description
     * Opens a modal (pop up window), that allows user to enter comment. 
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
     * @methodOf controllers.BlogController
     * @name addComment
     * 
     * @param {Object} comments Array of all comments
     * 
     * @description
     * Adds newly added comment to the comments list.
     * 1. If a comment is added to another comment as reply, then it will be added inside parent comment.
     * 2. However when a comment is added to a blog then comment is simply added to Blog as parent comment.
     */
    function addComment(comments) {
        // If comment on another comment
        if ($scope.commentData.commentId) {
            if (comments && comments.length === 0) { return []; }
            // Checking for each comment and its child comments in comments list
            // to find parent comment of newly added comment.
            angular.forEach(comments, function(comment, index) {  //jshint ignore:line
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
     * @methodOf controllers.BlogController
     * @name comment
     * 
     * @description
     * Adds a comment on either a blog or as a comment reply.
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

    // If action is show, fetch a blog with the given id.
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