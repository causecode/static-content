/* global controllers*/

'use strict';

controllers.controller('BlogController', ['$scope', '$state', 'BlogModel', 'appService', '$modal', 'PageModel', '$timeout',
        '$location', '$window', '$http', 'fileService', '$rootScope', 'SecurityService', function($scope, $state, BlogModel, appService, $modal, PageModel, $timeout, $location, $window, $http, fileService, $rootScope, securityService) {
    console.info('BlogController executing.', $scope);

    $scope.commentData = {};
    $scope.currentPage = 1;
    $scope.offset = 0;
    $scope.isBlogCtrl = true;

    $scope.fetchBlog = function(blogId) {

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
                $scope.blogImgSrc = blogData.blogImgSrc;
            }
            $scope.comments = blogData.comments;
            $scope.instanceList = blogData.blogInstanceList;
            $scope.tagList = blogData.tagList;
            
            // Setting meta tags
            var keywords = [];
            var descriptions = [];

            blogData.metaList.forEach(function (meta) {
                if (meta.type == "keywords") {
                    keywords.push(meta.value)
                } else if (meta.type == "description") {
                    descriptions.push(meta.value)
                }
            });

            // Setting blog meta tags
            document.querySelector( 'meta[name="description"]' ).setAttribute("content", descriptions[0] ? descriptions[0] : '')
            document.querySelector( 'meta[property="og:description"]' ).setAttribute("content", descriptions[0] ? descriptions[0] : '')
            document.querySelector( 'meta[name="keywords"]' ).setAttribute("content", keywords.toString())

            /*
             * Async load Prettify API. Loading two scripts since "prettify.js" doesn't include the "prettify.css"
             * And "run_prettify.js" doesn't provide the "prettyPrint()" global method.
             */
            if (!$window.PR_SHOULD_USE_CONTINUATION) {
                var script = document.createElement('script');
                script.type = 'text/javascript';
                script.src = 'https://cdn.rawgit.com/google/code-prettify/master/loader/prettify.js';
                document.body.appendChild(script);

                var script2 = document.createElement('script');
                script2.type = 'text/javascript';
                script2.src = 'https://cdn.rawgit.com/google/code-prettify/master/loader/run_prettify.js';
                document.body.appendChild(script2);
            } else {
                /*
                 * Prettify library modify HTML content but AngularJS takes few milliseconds to bind data to HTML (blogInstance in this case).
                 * Hence adding few milliseconds wait for that.
                 */ 
                $timeout(function() {
                    $window.PR.prettyPrint();
                }, 500);
            }
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
    $scope.initializeGetListResponse = function(data) {
        if (data) {
            $scope.instanceList = data.instanceList;
            $scope.instanceList.forEach(function(instance) {
                instance.encodedTitle = instance.title.replace(/\s+/g, '-').toLowerCase();
            });
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

    $scope.isBlogEditable = function() {
        return ($scope.userInstance && (securityService.ifAnyGranted($scope.userRoles, 'ROLE_CONTENT_MANAGER,ROLE_ADMIN') ||
                $scope.blogInstance.author === $scope.userInstance.username))
    }

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
            }, 2000);
        }, function(resp) {
            appService.showAlertMessage(resp.data.message, 'danger', {element: '.modal .alert', makeStrong: false});
        });
    };

    $scope.auth = function() {
        if($scope.actionName == 'create') {
            $http.get('/api/v1/blog/action/create');
        } else if ($scope.actionName == 'edit') {
            $http.get('/api/v1/blog/action/update');
        }
    }

    $scope.onFileSelect = function($files) {
        $scope.selectedFile = $files[0];
    };

    $scope.saveBlogPost = function(blogContent) {
        var blogPostRef = blogContent;
        fileService.uploadFile($scope.selectedFile).then(function(data) {
            blogPostRef.blogImgFilePath = data.filepath,
            blogPostRef.$save();
        }, function(data) {
            $scope.selectedFile = null;
        }); 
    };

    $scope.updateBlogPost = function(blogContent) {
        if($scope.selectedFile) {
            var blogPostRef = blogContent;
            fileService.uploadFile($scope.selectedFile).then(function(data) {
            blogPostRef.blogImgFilePath = data.filepath,
            blogPostRef.$update();
            }, function(data) {
                $scope.selectedFile = null;
            });
        } else {
            blogContent.$update()
        }
    }
    
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

    $scope.showMarkdownHelp = function() {
        $window.open("http://daringfireball.net/projects/markdown/syntax");
    };
}]);