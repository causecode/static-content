<%@ page import="com.cc.page.Page" %>

<div class="fieldcontain ${hasErrors(bean: pageInstance, field: 'title', 'error')} required">
  <label for="title">
    <g:message code="page.title.label" default="Title" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="title" required="" value="${pageInstance?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pageInstance, field: 'subTitle', 'error')} required">
  <label for="subTitle">
    <g:message code="page.subTitle.label" default="Sub Title" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="subTitle" required="" value="${pageInstance?.subTitle}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pageInstance, field: 'body', 'error')} required">
  <label for="body">
    <g:message code="page.body.label" default="Body" />
    <span class="required-indicator">*</span>
  </label>
  <ckeditor:editor name="body" height="300px" width="80%">
    <%= pageInstance?.body %>
  </ckeditor:editor>
</div>

<div>
	<g:message code="page.body.pageLayout" default="PageLayout" />
	<g:select id="pageLayout" name="pageLayout" from="${com.cc.content.PageLayout.list()}" optionKey = "id" optionValue = "layoutName" value = "${pageInstance?.pageLayout?.id}"/>
</div>

