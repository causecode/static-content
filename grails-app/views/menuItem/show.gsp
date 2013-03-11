<!--  /*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */ -->

<%@ page import="com.cc.navigation.MenuItem" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<r:require module="bootstrap"/>
		<g:set var="entityName" value="${message(code: 'menuItem.label', default: 'MenuItem')}" />
		<title><g:message code="" args="[entityName]" /></title>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="Home"/></a></li>
				<li><g:link class="list" action="list"><g:message code="List of MenuItems" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="Create New MenuItem" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-menuItem" class="content scaffold-show" role="main">
			<h1><g:message code="" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list menuItem"  style="list-style: none;">
			
				<g:if test="${menuItemInstance?.title}">
				<li class="fieldcontain">
					<span id="title-label" class="property-label"><g:message code="menuItem.title.label" default="Title : " /></span>
					
						<span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${menuItemInstance}" field="title"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${menuItemInstance?.url}">
				<li class="fieldcontain">
					<span id="url-label" class="property-label"><g:message code="menuItem.url.label" default="Url : " /></span>
					
						<span class="property-value" aria-labelledby="url-label"><g:fieldValue bean="${menuItemInstance}" field="url"/></span>
					
				</li>
				</g:if>
				
				<g:if test="${menuItemInstance?.parent}">
				<li class="fieldcontain">
					<span id="parent-label" class="property-label"><g:message code="menuItem.parent.label" default="Parent : " /></span>
					
						<span class="property-value" aria-labelledby="parent-label"><g:link controller="menuItem" action="show" id="${menuItemInstance?.parent?.id}">${menuItemInstance?.parent?.title}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${menuItemInstance?.childItems}">
				<li class="fieldcontain">
					<span id="childItem-label" class="property-label"><g:message code="menuItem.childItem.label" default="Child Items : " /></span>
					
						<g:each in="${menuItemInstance.childItems}" var="c">
						<span class="property-value" aria-labelledby="childItem-label"><g:link controller="menuItem" action="show" id="${c.id}">${c?.title}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
				
				<g:if test="${menuItemInstance?.menu}">
				<li class="fieldcontain">
					<span id="menu-label" class="property-label"><g:message code="menuItem.menu.label" default=" Menu : " /></span>
					
						<g:each in="${menuItemInstance.menu}" var="c">
						<span class="property-value" aria-labelledby="menu-label"><g:link controller="menu" action="show" id="${c.id}">${c?.name}</g:link></span>
						</g:each>
					
				</li>
				</g:if>
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${menuItemInstance?.id}" />
					<g:link class="edit" action="edit" id="${menuItemInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
