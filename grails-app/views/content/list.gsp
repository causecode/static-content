
<%@ page import="com.cc.content.Content" %>
<!doctype html>
<html>
  <head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'content.label', default: 'Content')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
  </head>
  <body>
    <div class="nav" role="navigation">
      <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
      </ul>
    </div>
    <div id="list-content" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <g:if test="${flash.message}">
      <div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
        <thead>
          <tr>
          
            <g:sortableColumn property="title" title="${message(code: 'content.title.label', default: 'Title')}" />
          
            <g:sortableColumn property="subTitle" title="${message(code: 'content.subTitle.label', default: 'Sub Title')}" />
          
            <g:sortableColumn property="dateCreated" title="${message(code: 'content.dateCreated.label', default: 'Date Created')}" />
          
            <g:sortableColumn property="lastUpdated" title="${message(code: 'content.lastUpdated.label', default: 'Last Updated')}" />
          
          </tr>
        </thead>
        <tbody>
        <g:each in="${contentInstanceList}" status="i" var="contentInstance">
          <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
          
            <td><g:link action="show" id="${contentInstance.id}">${fieldValue(bean: contentInstance, field: "title")}</g:link></td>
          
            <td>${fieldValue(bean: contentInstance, field: "subTitle")}</td>
          
            <td><g:formatDate date="${contentInstance.dateCreated}" /></td>
          
            <td><g:formatDate date="${contentInstance.lastUpdated}" /></td>
          
          </tr>
        </g:each>
        </tbody>
      </table>
      <div class="pagination">
        <g:paginate total="${contentInstanceTotal}" />
      </div>
    </div>
  </body>
</html>
