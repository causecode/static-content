<%@ page import="com.cc.content.Content" %>
<!DOCTYPE html>
<html lang="en">

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <r:require module="bootstrap"/>
  <g:set var="entityName" value="${message(code: 'content.label', default: 'Content')}" />
  <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
  <div class="page-header">
    <h1>${contentInstance.title }</h1><span><h6>${contentInstance.subTitle }</h6></span>
    </div>
    <%= contentInstance.body %>
    <sec:ifLoggedIn>
        <g:form>
        <fieldset class="buttons">
          <g:hiddenField name="id" value="${contentInstance?.id}" />
          <g:link class="edit" action="edit" id="${contentInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
          <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
      </g:form>
      </sec:ifLoggedIn>
</body>

</html>
