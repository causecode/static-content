<%@ page import="com.cc.blog.Blog" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta http-equiv="blog-Type" blog="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <g:set var="entityName" value="${message(code: 'blog.label', default: 'blog')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
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
</body>

