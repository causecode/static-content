<!--  /*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */  -->

<%@ page import="com.cc.content.PageLayout" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'pageLayout.label', default: 'PageLayout')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-pageLayout" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-pageLayout" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="layoutFile" title="${message(code: 'pageLayout.layoutFile.label', default: 'Layout File')}" />
					
						<g:sortableColumn property="layoutName" title="${message(code: 'pageLayout.layoutName.label', default: 'Layout Name')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${pageLayoutInstanceList}" status="i" var="pageLayoutInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${pageLayoutInstance.id}">${fieldValue(bean: pageLayoutInstance, field: "layoutFile")}</g:link></td>
					
						<td>${fieldValue(bean: pageLayoutInstance, field: "layoutName")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${pageLayoutInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
