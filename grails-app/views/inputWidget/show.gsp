

<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'inputWidget.label', default: 'InputWidget')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
    <div class="page-header">
        <h1>
            <g:message code="default.show.label" args="[entityName]" />
        </h1>
    </div>
    <ol class="property-list inputWidget">
        
				<g:if test="${inputWidgetInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="inputWidget.name.label" default="Name" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${inputWidgetInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.label}">
				<li class="fieldcontain">
					<span id="label-label" class="property-label"><g:message code="inputWidget.label.label" default="Label" /></span>
					
						<span class="property-value" aria-labelledby="label-label"><g:fieldValue bean="${inputWidgetInstance}" field="label"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.validate}">
				<li class="fieldcontain">
					<span id="validate-label" class="property-label"><g:message code="inputWidget.validate.label" default="Validate" /></span>
					
						<span class="property-value" aria-labelledby="validate-label"><g:fieldValue bean="${inputWidgetInstance}" field="validate"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.widgetKey}">
				<li class="fieldcontain">
					<span id="widgetKey-label" class="property-label"><g:message code="inputWidget.widgetKey.label" default="Widget Key" /></span>
					
						<span class="property-value" aria-labelledby="widgetKey-label"><g:fieldValue bean="${inputWidgetInstance}" field="widgetKey"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.widgetValue}">
				<li class="fieldcontain">
					<span id="widgetValue-label" class="property-label"><g:message code="inputWidget.widgetValue.label" default="Widget Value" /></span>
					
						<span class="property-value" aria-labelledby="widgetValue-label"><g:fieldValue bean="${inputWidgetInstance}" field="widgetValue"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="inputWidget.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${inputWidgetInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="inputWidget.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${inputWidgetInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.defaultValue}">
				<li class="fieldcontain">
					<span id="defaultValue-label" class="property-label"><g:message code="inputWidget.defaultValue.label" default="Default Value" /></span>
					
						<span class="property-value" aria-labelledby="defaultValue-label"><g:fieldValue bean="${inputWidgetInstance}" field="defaultValue"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.helpText}">
				<li class="fieldcontain">
					<span id="helpText-label" class="property-label"><g:message code="inputWidget.helpText.label" default="Help Text" /></span>
					
						<span class="property-value" aria-labelledby="helpText-label"><g:fieldValue bean="${inputWidgetInstance}" field="helpText"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.helpType}">
				<li class="fieldcontain">
					<span id="helpType-label" class="property-label"><g:message code="inputWidget.helpType.label" default="Help Type" /></span>
					
						<span class="property-value" aria-labelledby="helpType-label"><g:fieldValue bean="${inputWidgetInstance}" field="helpType"/></span>
					
				</li>
				</g:if>
			
    </ol>
    <g:form>
        <fieldset class="form-actions">
            <g:hiddenField name="id" value="${inputWidgetInstance?.id}" />
            <g:link class="btn btn-primary" action="edit" id="${inputWidgetInstance?.id}">
                <g:message code="default.button.edit.label" default="Edit" />
            </g:link>
            <g:actionSubmit class="btn btn-danger" action="delete"
                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
        </fieldset>
    </g:form>
</body>
</html>