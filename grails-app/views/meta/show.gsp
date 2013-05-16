

<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'meta.label', default: 'Meta')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
    <div class="page-header">
        <h1>
            <g:message code="default.show.label" args="[entityName]" />
        </h1>
    </div>
    <ol class="property-list meta">
        
				<g:if test="${metaInstance?.type}">
				<li class="fieldcontain">
					<span id="type-label" class="property-label"><g:message code="meta.type.label" default="Type" /></span>
					
						<span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${metaInstance}" field="type"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${metaInstance?.value}">
				<li class="fieldcontain">
					<span id="value-label" class="property-label"><g:message code="meta.value.label" default="Value" /></span>
					
						<span class="property-value" aria-labelledby="value-label"><g:fieldValue bean="${metaInstance}" field="value"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${metaInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="meta.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${metaInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${metaInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="meta.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${metaInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
    </ol>
    <g:form>
        <fieldset class="form-actions">
            <g:hiddenField name="id" value="${metaInstance?.id}" />
            <g:link class="btn btn-primary" action="edit" id="${metaInstance?.id}">
                <g:message code="default.button.edit.label" default="Edit" />
            </g:link>
            <g:actionSubmit class="btn btn-danger" action="delete"
                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
        </fieldset>
    </g:form>
</body>
</html>