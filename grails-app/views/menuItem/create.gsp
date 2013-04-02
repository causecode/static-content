<%@ page import="com.cc.navigation.MenuItem" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<r:require modules="bootstrap, jquery"/>
		<g:set var="entityName" value="${message(code: 'menuItem.label', default: 'MenuItem')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#create-menuItem" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="create-menuItem" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
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
			<g:form name="mainForm" action="save" >
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<g:if test="${menuItemInstance?.childItems}">
     				<g:each in="${menuItemInstance?.childItems}">
    					<br><div id="childrenDiv" method="post" class="form-inline">
    					<label for="childTitle">Child Title<span class="required-indicator">*</span></label>
						<g:textField name="childTitle" class="required" value="${it.title}"/>
						<label for="childUrl">Child Url<span class="required-indicator">*</span></label>
						<g:textField name="childUrl"  class="required url" value="${it.url}"/>
						</div><br>
					</g:each>
				</g:if>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Submit')}" />
				</fieldset>
			</g:form>
		</div>
		<script type="text/javascript">
			$("#addChildren").click ( function () {
				var data = '<br><div id="childrenDiv" method="post" class="form-inline">'+
		    	'<label for="childTitle">Child Title<span class="required-indicator">*</span></label>'+
				'<g:textField class="required" name="childTitle" value=" "/>'+
				'<label for="childUrl">Child Url<span class="required-indicator">*</span></label>'+
				'<g:textField class="required url" name="childUrl" value=" "/>'+
				'</div><br>';
				$("#clildMainDiv").append(data);
			});
		</script>
	</body>
</html>
