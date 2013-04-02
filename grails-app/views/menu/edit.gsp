<%@ page import="com.cc.navigation.Menu" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<r:require modules="bootstrap, jquery"/>
		<g:set var="entityName" value="${message(code: 'menu.label', default: 'Menu')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#edit-menu" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="edit-menu" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${menuInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${menuInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form method="post" >
				<g:hiddenField name="id" value="${menuInstance?.id}" />
				<g:hiddenField name="version" value="${menuInstance?.version}" />
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<h3>Menu Items</h3>
				<g:if test="${menuInstance?.menuItem}">
     				<g:each in="${menuInstance.menuItem}">
    					<g:link controller="menuItem" action="show" id="${it.id}">${it.title}</g:link>
    					<g:link action="deleteChild" id="${it.id}">X</g:link><br>
					</g:each>
				</g:if>
				<fieldset class="buttons">
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate="" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
		<script type="text/javascript">
			$("#addChildren").click ( function () {
				$("#toggleDiv").show();
				$("#addChildren").hide();
			});
		</script>
		<script>
			jQuery(document).ready(function() { 
					 $("#toggleDiv").hide();
			});
		</script>
	</body>
</html>
