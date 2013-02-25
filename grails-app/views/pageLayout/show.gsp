<!--  Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are not permitted. -->

<%@ page import="com.cc.content.PageLayout" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<r:require module="bootstrap"/>
		<g:set var="entityName" value="${message(code: 'pageLayout.label', default: 'PageLayout')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-pageLayout" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-pageLayout" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list pageLayout">
			
				<g:if test="${pageLayoutInstance?.layoutFile}">
				<li class="fieldcontain">
					<span id="layoutFile-label" class="property-label"><g:message code="pageLayout.layoutFile.label" default="Layout File" /></span>
					
						<span class="property-value" aria-labelledby="layoutFile-label"><g:fieldValue bean="${pageLayoutInstance}" field="layoutFile"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${pageLayoutInstance?.layoutName}">
				<li class="fieldcontain">
					<span id="layoutName-label" class="property-label"><g:message code="pageLayout.layoutName.label" default="Layout Name" /></span>
					
						<span class="property-value" aria-labelledby="layoutName-label"><g:fieldValue bean="${pageLayoutInstance}" field="layoutName"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${pageLayoutInstance?.id}" />
					<g:link class="edit" action="edit" id="${pageLayoutInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
