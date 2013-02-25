<!--  Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are not permitted. -->

<%@ page import="com.cc.blog.Blog" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="main">
    <r:require module="bootstrap"/>
    <g:set var="entityName" value="${message(code: 'blog.label', default: 'Latest Blog')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
    <style type="text/css" media="screen">
    </style>
  </head>
  <body>
    <div>
      <h1>Blog</h1>
       <sec:ifLoggedIn>
          <g:link action='create' controller='blog'>Create a Blog</g:link>
       </sec:ifLoggedIn>
    </div>
    <r:script>
    $('.pagination').wrapInner("<ul></ul>");
    $('.step').wrap("<li></li>");
    $('.prevLink').wrap("<li></li>");
    $('.nextLink').wrap("<li></li>");
    $('.currentStep').wrap("<li></li>");
    </r:script>
    
    <div class="blog_list">
      <g:each in="${blogInstanceList}" status="i" var="blogInstance">
        <div class="blogEntry summary">
        <div class="blog_title">
          <h2>${blogInstance.title }</h2>
          <h4>${blogInstance.subTitle}</h4>
          <g:if test="${blogInstance.author == 'anonymousUser'}">
            <g:set var="user" value="anonymousUser" />
          </g:if>
          <g:else>
             <g:set var="user" value="${userClass.get(blogInstance.author.toInteger()).username}" />
          </g:else>
          <small>By: <b> ${user} &nbsp;&nbsp;</b></small>|&nbsp;&nbsp;Posted on: <small>${blogInstance.dateCreated.format('dd-MM-yyyy')}</small>
          </div>
          <div class="blog_body">
            ${blogInstance.body.substring(0,139)}
            <g:link action='show' controller='blog' id='${blogInstance.id}'>...more</g:link>
          </div>
          </div>
          <hr style="border: 1px solid #DEDEDE;">
       </g:each>  
      <div class="pagination">
        <g:paginate total="${blogInstanceTotal}" />
      </div>
    </div>
  </body>
</html>
