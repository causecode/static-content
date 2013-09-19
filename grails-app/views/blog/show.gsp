<!-- /*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */ -->

<%@ page import="com.cc.blog.Blog"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta http-equiv="blog-Type" blog="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<r:require module="jquery" />
<g:set var="entityName" value="${message(code: 'blog.label', default: 'blog')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
<style type="text/css">
div.comment {
    margin-left: 50px;
}
</style>
</head>

<body>
    <div class="page-header">
        <h1>
            ${blogInstance.title }
        </h1>
        <span><h6>
                ${blogInstance.subTitle }
            </h6></span> <small>By: <b> ${username} &nbsp;&nbsp;
        </b></small>|&nbsp;&nbsp;Posted on: <small> ${blogInstance.dateCreated.format('dd-MM-yyyy')}
        </small>
    </div>
    <%= blogInstance.body %>
    <br>
    <b> Tags: </b>
    <g:each in="${blogInstance.tags}">
        <g:link action="findByTag" params="[tag:it]">
            ${it}
        </g:link>
    </g:each>
    <sec:ifLoggedIn>
        <g:form>
            <fieldset>
                <div class="form-actions">
                    <g:hiddenField name="id" value="${blogInstance?.id}" />
                    <g:link class="edit btn btn-default btn-primary" action="edit" id="${blogInstance?.id}">
                        <g:message code="default.button.edit.label" default="Edit" />
                    </g:link>
                    <g:actionSubmit class="delete btn btn-default btn-danger" action="delete"
                        value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                        onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                </div>
            </fieldset>
        </g:form>
    </sec:ifLoggedIn>
    <br>
    <div id="commentModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <label class="modal-title inline " id="commentModalLabel"><h3>Comment</h3></label>
                </div>
                <g:form name="comment-modal-form" class="form-horizontal block-error jquery-form">
                    <fieldset form="comment-modal-form">
                        <div class="modal-body">
                            <div class="form-group">
                                <label class="control-label col-sm-3">Subject </label>
                                <div class="col-lg-7">
                                    <g:textField name="subject" class="form-control" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-3">Name </label>
                                <div class="col-lg-7">
                                    <g:textField name="name" class="form-control" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-3">E-Mail </label>
                                <div class="col-lg-7">
                                    <g:textField name="email" class="form-control" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-sm-3">Comment </label>
                                <div class="col-lg-7">
                                    <g:textArea name="commentText" class="form-control" />
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <div class="form-group">
                                <g:actionSubmit class="comment" class="btn btn-default btn-primary" action="comment"
                                    value="Comment" />
                                <button class="btn btn-default" data-dismiss="modal" aria-hidden="true">Close</button>
                                <g:hiddenField name="blog_id" value="${blogInstance?.id}" />
                                <g:hiddenField name="replyCommentId" />
                            </div>
                        </div>
                    </fieldset>
                </g:form>
            </div>
        </div>
    </div>
    <h2 class="inline">Comments</h2>
    <a href="#commentModal" role="" class="commentButton" data-toggle="modal">&nbsp;&nbsp;Add</a>
    

    <g:each in="${comments}">
        <g:render template='comments' model="['it':it]"></g:render>
    </g:each>

    <script type="text/javascript">
        $("a.commentButton").on("click", function() { 
            var replyCommentId = $(this).closest("div").attr("data-comment");
            $("input[type=hidden][name=replyCommentId]").val(replyCommentId);
        });
    </script>
</body>
</html>
