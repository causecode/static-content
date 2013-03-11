<!--  /*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */ -->

<%@ page import="com.cc.content.Content" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="main">
    <r:require module="bootstrap"/>
    <g:set var="entityName" value="${message(code: 'content.label', default: 'Content')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
    <ckeditor:resources />
  </head>
  <body>
    <div id="edit-content" class="content scaffold-edit" role="main">
      <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
      <div class="message" role="status">${flash.message}</div>
      </g:if>
      <g:hasErrors bean="${contentInstance}">
      <ul class="errors" role="alert">
        <g:eachError bean="${contentInstance}" var="error">
        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
        </g:eachError>
      </ul>
      </g:hasErrors>
      <g:form method="post" >
        <g:hiddenField name="id" value="${contentInstance?.id}" />
        <g:hiddenField name="version" value="${contentInstance?.version}" />
        <fieldset class="form">
          <g:render template="form"/>
        </fieldset>
        <fieldset class="buttons">
          <g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
          <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
