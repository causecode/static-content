<%@ page import="com.cc.content.Content" %>



<div class="fieldcontain ${hasErrors(bean: contentInstance, field: 'body', 'error')} ">
	<label for="body">
		<g:message code="content.body.label" default="Body" />
		
	</label>
	<g:textField name="body" value="${contentInstance?.body}" />
</div>

<div class="fieldcontain ${hasErrors(bean: contentInstance, field: 'subTitle', 'error')} ">
	<label for="subTitle">
		<g:message code="content.subTitle.label" default="Sub Title" />
		
	</label>
	<g:textField name="subTitle" value="${contentInstance?.subTitle}" />
</div>

<div class="fieldcontain ${hasErrors(bean: contentInstance, field: 'title', 'error')} ">
	<label for="title">
		<g:message code="content.title.label" default="Title" />
		
	</label>
	<g:textField name="title" value="${contentInstance?.title}" />
</div>

