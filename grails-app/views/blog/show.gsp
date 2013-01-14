<%@ page import="com.cc.blog.Blog" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta http-equiv="blog-Type" blog="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <r:require modules="bootstrap, jquery"/>
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
 <a href="#myModal" role="button" class="btn" data-toggle="modal">Comment</a>
<g:form> 
<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">

	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		<h3 id="myModalLabel">Comment</h3>
	</div>
	<div class="modal-body">

		Subject: <g:textField name="subject"/><br>
		Name:<g:textField name="name"/><br>
		E-Mail:<g:textField name="email"/><br>
		Comment:<g:textArea name="commentText"/><br>

	</div>
	<div class="modal-footer">
		<button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
		<g:hiddenField name="blog_id" value="${blogInstance?.id}" />
		<g:hiddenField name="replyCommentId"/>
		<g:actionSubmit class="comment" action="comment" value="Comment"/>
	</div>
</div>
</g:form>
<h2>Comments</h2>

<g:each in="${comments}">
	<div data-comment = "${it.id}">
	<p>${it.subject}</p>
    <p>${it.name} | Date:<g:formatDate format="MM-dd-yyyy" date="${it.dateCreated}"/> 
    <p>${it.commentText}</p>
    <a href="#myModal" role="button" class="btn" data-toggle="modal">Reply</a>
    <g:nestedComment commentId="${it.id}"></g:nestedComment>
    <hr>
    </div>
</g:each>

<script type="text/javascript">
	$(".btn").on("click", function() { 
 		var replyCommentId = $(this).closest("div").attr("data-comment");
   		$("input[type=hidden][name=replyCommentId]").val(replyCommentId);
	});
		</script>
</body>
</html>
