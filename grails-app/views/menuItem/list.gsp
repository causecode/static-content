<!--  Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are not permitted. -->

<%@ page import="com.cc.navigation.MenuItem" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<r:require module="bootstrap"/>
		<g:set var="entityName" value="${message(code: 'menuItem.label', default: 'MenuItem')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-menuItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-menuItem" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="url" title="${message(code: 'menuItem.url.label', default: 'Url')}" />
					
						<th><g:message code="menuItem.parent.label" default="Parent" /></th>
					
						<g:sortableColumn property="title" title="${message(code: 'menuItem.title.label', default: 'Title')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${menuItemInstanceList}" status="i" var="menuItemInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${menuItemInstance.id}">${fieldValue(bean: menuItemInstance, field: "url")}</g:link></td>
					
						<td>${fieldValue(bean: menuItemInstance, field: "parent")}</td>
					
						<td>${fieldValue(bean: menuItemInstance, field: "title")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${menuItemInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
