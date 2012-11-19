<%@ page import="com.cc.content.Content" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'content.label', default: 'Content')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
    <ckeditor:resources/>
  </head>
  <body>
    <div id="create-content">
      <h1><g:message code="default.create.label" args="[entityName]" /></h1>
      <g:form action="save" >
        <fieldset class="form">
          <g:render template="form"/>
        </fieldset>
        <fieldset class="buttons">
          <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
        </fieldset>
      </g:form>
    </div>
  </body>
</html>
