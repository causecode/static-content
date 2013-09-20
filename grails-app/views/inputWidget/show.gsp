

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
			
				<g:if test="${inputWidgetInstance?.validation}">
				<li class="fieldcontain">
					<span id="validation-label" class="property-label"><g:message code="inputWidget.validation.label" default="Validation" /></span>
					
						<span class="property-value" aria-labelledby="validation-label"><g:fieldValue bean="${inputWidgetInstance}" field="validation"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.widgetKeys}">
				<li class="fieldcontain">
					<span id="widgetKeys-label" class="property-label"><g:message code="inputWidget.widgetKeys.label" default="Widget Keys" /></span>
					
						<span class="property-value" aria-labelledby="widgetKeys-label"><g:fieldValue bean="${inputWidgetInstance}" field="widgetKeys"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.widgetValues}">
				<li class="fieldcontain">
					<span id="widgetValues-label" class="property-label"><g:message code="inputWidget.widgetValues.label" default="Widget Values" /></span>
					
						<span class="property-value" aria-labelledby="widgetValues-label"><g:fieldValue bean="${inputWidgetInstance}" field="widgetValues"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.defaultSelected}">
				<li class="fieldcontain">
					<span id="defaultSelected-label" class="property-label"><g:message code="inputWidget.defaultSelected.label" default="Default Selected" /></span>
					
						<span class="property-value" aria-labelledby="defaultSelected-label"><g:fieldValue bean="${inputWidgetInstance}" field="defaultSelected"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.noSelected}">
				<li class="fieldcontain">
					<span id="noSelected-label" class="property-label"><g:message code="inputWidget.noSelected.label" default="No Selected" /></span>
					
						<span class="property-value" aria-labelledby="noSelected-label"><g:fieldValue bean="${inputWidgetInstance}" field="noSelected"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.minChar}">
				<li class="fieldcontain">
					<span id="minChar-label" class="property-label"><g:message code="inputWidget.minChar.label" default="Min Char" /></span>
					
						<span class="property-value" aria-labelledby="minChar-label"><g:fieldValue bean="${inputWidgetInstance}" field="minChar"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.maxChar}">
				<li class="fieldcontain">
					<span id="maxChar-label" class="property-label"><g:message code="inputWidget.maxChar.label" default="Max Char" /></span>
					
						<span class="property-value" aria-labelledby="maxChar-label"><g:fieldValue bean="${inputWidgetInstance}" field="maxChar"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.minValueRange}">
				<li class="fieldcontain">
					<span id="minValueRange-label" class="property-label"><g:message code="inputWidget.minValueRange.label" default="Min Value Range" /></span>
					
						<span class="property-value" aria-labelledby="minValueRange-label"><g:fieldValue bean="${inputWidgetInstance}" field="minValueRange"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${inputWidgetInstance?.maxValueRange}">
				<li class="fieldcontain">
					<span id="maxValueRange-label" class="property-label"><g:message code="inputWidget.maxValueRange.label" default="Max Value Range" /></span>
					
						<span class="property-value" aria-labelledby="maxValueRange-label"><g:fieldValue bean="${inputWidgetInstance}" field="maxValueRange"/></span>
					
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
			
				<g:if test="${inputWidgetInstance?.type}">
				<li class="fieldcontain">
					<span id="type-label" class="property-label"><g:message code="inputWidget.type.label" default="Type" /></span>
					
						<span class="property-value" aria-labelledby="type-label"><g:fieldValue bean="${inputWidgetInstance}" field="type"/></span>
					
				</li>
				</g:if>
			
    </ol>
    <g:form>
        <fieldset class="form-actions well form-group">
            <div class="col-lg-offset-2">
                <g:hiddenField name="id" value="${inputWidgetInstance?.id}" />
                <g:link class="btn btn-default btn btn-default-primary" action="edit" id="${inputWidgetInstance?.id}">
                    <g:message code="default.button.edit.label" default="Edit" />
                </g:link>
                <g:actionSubmit class="btn btn-default btn btn-default-danger" action="delete"
                    value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
            </div>
        </fieldset>
    </g:form>
</body>
</html>