

<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'textFormat.label', default: 'TextFormat')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
    <div class="page-header">
        <h1>
            <g:message code="default.show.label" args="[entityName]" />
        </h1>
    </div>
    <ol class="property-list textFormat">
        
				<g:if test="${textFormatInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="textFormat.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${textFormatInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${textFormatInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="textFormat.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${textFormatInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${textFormatInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="textFormat.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${textFormatInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${textFormatInstance?.roles}">
				<li class="fieldcontain">
					<span id="roles-label" class="property-label"><g:message code="textFormat.roles.label" default="Roles" /></span>
					
						<span class="property-value" aria-labelledby="roles-label"><g:fieldValue bean="${textFormatInstance}" field="roles"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${textFormatInstance?.allowedTags}">
				<li class="fieldcontain">
					<span id="allowedTags-label" class="property-label"><g:message code="textFormat.allowedTags.label" default="Allowed Tags" /></span>
					
						<span class="property-value" aria-labelledby="allowedTags-label"><g:fieldValue bean="${textFormatInstance}" field="allowedTags"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${textFormatInstance?.editor}">
				<li class="fieldcontain">
					<span id="editor-label" class="property-label"><g:message code="textFormat.editor.label" default="Editor" /></span>
					
						<span class="property-value" aria-labelledby="editor-label"><g:formatBoolean boolean="${textFormatInstance?.editor}" /></span>
					
				</li>
				</g:if>
			
    </ol>
    <g:form>
        <fieldset class="form-actions">
            <g:hiddenField name="id" value="${textFormatInstance?.id}" />
            <g:link class="btn btn-primary" action="edit" id="${textFormatInstance?.id}">
                <g:message code="default.button.edit.label" default="Edit" />
            </g:link>
            <g:actionSubmit class="btn btn-danger" action="delete"
                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
        </fieldset>
    </g:form>
</body>
</html>