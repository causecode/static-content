<!--  Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are not permitted. -->

<%@ page import="com.cc.page.Page" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
    <r:require module="bootstrap"/>
    <g:set var="entityName" value="${message(code: 'page.label', default: 'Page')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <h1>Page</h1>
    <sec:ifAllGranted roles="ROLE_ADMIN">
          <g:link action='create' controller='page'>Create a Page</g:link>
    </sec:ifAllGranted>
    <r:script>
    $('.pagination').wrapInner("<ul></ul>");
    $('.step').wrap("<li></li>");
    $('.prevLink').wrap("<li></li>");
    $('.nextLink').wrap("<li></li>");
    $('.currentStep').wrap("<li></li>");
    </r:script>
    <div class="page_list">
      <g:each in="${pageInstanceList}" status="i" var="pageInstance">
        <div class="pageEntry summary">
        <div class="page_title">
          <h2>${pageInstance.title }</h2>
          <h4>${pageInstance.subTitle}</h4>
        </div>
          <div class="page_body">
            ${pageInstance.body.substring(0,139)}
            <g:link action='show' controller='page' id='${pageInstance.id}'>...more</g:link>
          </div>
          </div>
          <hr style="border: 1px solid #DEDEDE;">
       </g:each>  
      <div class="pagination">
        <g:paginate maxsteps="2" total="${pageInstanceTotal}" />
      </div>
    </div>
  </body>
</html>
