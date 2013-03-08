<!--  Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are not permitted. -->

<%@ page import="com.cc.blog.Blog" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta http-equiv="blog-Type" blog="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <r:require module="jquery"/>
  <g:set var="entityName" value="${message(code: 'blog.label', default: 'blog')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
  <style type="text/css">
	div.comment {  
	  margin-left : 50px;
	}
	</style>
</head>

<body>
	<div class="page-header">
	    <h1>${blogInstance.title }</h1><span><h6>${blogInstance.subTitle }</h6></span>
	    <small>By: <b> ${username} &nbsp;&nbsp;</b></small>|&nbsp;&nbsp;Posted on: <small>${blogInstance.dateCreated.format('dd-MM-yyyy')}</small>
	    </div>
	    <%= blogInstance.body %>
	    <br>
	    <b> Tags: </b>
	    <g:each in="${blogInstance.tags}">
	      <g:link action="findByTag"  params="[tag:it]">${it}</g:link>
	    </g:each>
	    <sec:ifLoggedIn>
	           <g:form>
			        <fieldset class="buttons">
			          <g:hiddenField name="id" value="${blogInstance?.id}" />
			          <g:link class="edit" action="edit" id="${blogInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
			          <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
			        </fieldset>
	      	    </g:form>
	 	</sec:ifLoggedIn>
	 	<br>
	 	<a href="#commentModal" role="button" class="btn commentButton" data-toggle="modal">Comment</a>
	<g:form class="form-horizontal"> 
		<div id="commentModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="commentModalLabel" aria-hidden="true">
			<div class="modal-header control-group">
				<button type="button" class="close controls" data-dismiss="modal" aria-hidden="true">×</button>
				<label class="control-label" id="commentModalLabel"><h3>Comment</h3></label>
			</div>
			<div class="modal-body">
				<div class="control-group">
					<label class="control-label">Subject : </label>
					<div class="controls">
						<g:textField name="subject"/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">Name : </label>
					<div class="controls">
						<g:textField name="name"/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">E-Mail : </label>
					<div class="controls">
						<g:textField name="email"/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">Comment : </label>
					<div class="controls">
						<g:textArea name="commentText"/>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<div class="control-group">
					<div class="controls">
						<g:actionSubmit class="comment" class="btn btn-primary" action="comment" value="Comment"/>
						<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
						<g:hiddenField name="blog_id" value="${blogInstance?.id}" />
						<g:hiddenField name="replyCommentId"/>
					</div>
				</div>
			</div>
		</div>
	</g:form>
	<h2>Comments</h2>

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
