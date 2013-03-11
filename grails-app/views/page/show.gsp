<!--  /*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */ -->

<%@ page import="com.cc.page.Page" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="${layout?:'main'}">
    <r:require module="bootstrap"/>
    <g:set var="entityName" value="${message(code: 'page.label', default: 'Page')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="page-header">
    <h1>${pageInstance.title }</h1><span><h6>${pageInstance.subTitle }</h6></span>
    
    </div>
    <%= pageInstance.body %>
    <sec:ifLoggedIn>
       <g:form>
        <fieldset class="buttons">
          <g:hiddenField name="id" value="${pageInstance?.id}" />
          <g:link class="edit" action="edit" id="${pageInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
          <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
      </g:form>
  </sec:ifLoggedIn>
  </body>
</html>
