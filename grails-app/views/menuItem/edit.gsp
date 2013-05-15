<!--  /*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */  -->

<%@ page import="com.cc.navigation.MenuItem" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<r:require modules="bootstrap, jquery"/>
		<g:set var="entityName" value="${message(code: 'menuItem.label', default: 'MenuItem')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#edit-menuItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="edit-menuItem" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${menuItemInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${menuItemInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form name="mainForm" method="post" >
				<g:hiddenField name="id" value="${menuItemInstance?.id}" />
				<g:hiddenField name="version" value="${menuItemInstance?.version}" />
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<g:if test="${menuItemInstance?.childItems}">
     				<g:each in="${menuItemInstance?.childItems}">
     				<g:if test="${it.id == null}">
    					<g:render template="childItemForm" model="['childItem':it]"/>
					</g:if>
					</g:each>
					
				</g:if>
				<h3>Children Items</h3>
				<g:if test="${menuItemInstance?.childItems}">
     				<g:each in="${menuItemInstance?.childItems}">
     				<g:if test="${it.id != null}">
    					<g:link action="show" id="${it.id}">${it.title}</g:link>
    					<g:link action="deleteChild" id="${it.id}">X</g:link><br>
    				</g:if>
					</g:each>
				</g:if>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
		<div id="childrenDiv" style="display: none;">
				<g:render template="childItemForm"/>
		</div>
		<script type="text/javascript">
			$("#addChildren").click ( function () {
				var data = $('#childrenDiv').html();
				$("#clildMainDiv").append(data);
			});
		</script>
	</body>
</html>
