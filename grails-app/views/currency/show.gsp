

<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'currency.label', default: 'Currency')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
    <div class="page-header">
        <h1>
            <g:message code="default.show.label" args="[entityName]" />
        </h1>
    </div>
    <ol class="property-list currency">
        
				<g:if test="${currencyInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="currency.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${currencyInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${currencyInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="currency.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${currencyInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${currencyInstance?.code}">
				<li class="fieldcontain">
					<span id="code-label" class="property-label"><g:message code="currency.code.label" default="Code" /></span>
					
						<span class="property-value" aria-labelledby="code-label"><g:fieldValue bean="${currencyInstance}" field="code"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${currencyInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="currency.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${currencyInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
    </ol>
    <g:form>
        <fieldset class="form-actions">
            <g:hiddenField name="id" value="${currencyInstance?.id}" />
            <g:link class="btn btn-primary" action="edit" id="${currencyInstance?.id}">
                <g:message code="default.button.edit.label" default="Edit" />
            </g:link>
            <g:actionSubmit class="btn btn-danger" action="delete"
                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
        </fieldset>
    </g:form>
</body>
</html>