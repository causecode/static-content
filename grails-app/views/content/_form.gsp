<!--  Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are not permitted. -->

<%@ page import="com.cc.content.Content" %>


<div class="fieldcontain ${hasErrors(bean: contentInstance, field: 'title', 'error')} required">
  <label for="title">
    <g:message code="content.title.label" default="Title" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="title" required="" value="${contentInstance?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: contentInstance, field: 'subTitle', 'error')} required">
  <label for="subTitle">
    <g:message code="content.subTitle.label" default="Sub Title" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="subTitle" required="" value="${contentInstance?.subTitle}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: contentInstance, field: 'body', 'error')} required">
  <label for="body">
    <g:message code="content.body.label" default="Body" />
    <span class="required-indicator">*</span>
  </label>
  <ckeditor:editor name="body" height="300px" width="80%">
    <%= contentInstance?.body %>
  </ckeditor:editor>
</div>



