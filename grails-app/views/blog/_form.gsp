<!--  Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are not permitted. -->

<%@ page import="com.cc.blog.Blog" %>

<div class="fieldcontain ${hasErrors(bean: blogInstance, field: 'title', 'error')} required">
  <label for="title">
    <g:message code="blog.title.label" default="Title" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="title" required="" value="${blogInstance?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: blogInstance, field: 'subTitle', 'error')} required">
  <label for="subTitle">
    <g:message code="blog.subTitle.label" default="Sub Title" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="subTitle" required="" value="${blogInstance?.subTitle}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: blogInstance, field: 'body', 'error')} required">
  <label for="body">
    <g:message code="blog.body.label" default="Body" />
    <span class="required-indicator">*</span>
  </label>
  <ckeditor:editor name="body" height="300px" width="80%">
    <%= blogInstance?.body %>
  </ckeditor:editor>
</div>

<div>
  <label for="tags">
    <g:message code="blog.body.label" default="Tags" />
  </label>
  <g:set var="presentTags" value="" />
  <g:each in="${blogInstance?.tags}">
    <g:set var="presentTags" value="${presentTags +", " + it}" />
  </g:each>
  <g:textField name="tags" required="" value="${presentTags}"/>
</div>




