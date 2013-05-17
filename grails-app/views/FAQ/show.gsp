

<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'FAQ.label', default: 'FAQ')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
    <div class="page-header">
        <h1>
            <g:message code="default.show.label" args="[entityName]" />
        </h1>
    </div>
    <ol class="property-list FAQ">
        
				<g:if test="${FAQInstance?.body}">
				<li class="fieldcontain">
					<span id="body-label" class="property-label"><g:message code="FAQ.body.label" default="Body" /></span>
					
						<span class="property-value" aria-labelledby="body-label"><g:fieldValue bean="${FAQInstance}" field="body"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${FAQInstance?.title}">
				<li class="fieldcontain">
					<span id="title-label" class="property-label"><g:message code="FAQ.title.label" default="Title" /></span>
					
						<span class="property-value" aria-labelledby="title-label"><g:fieldValue bean="${FAQInstance}" field="title"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${FAQInstance?.subTitle}">
				<li class="fieldcontain">
					<span id="subTitle-label" class="property-label"><g:message code="FAQ.subTitle.label" default="Sub Title" /></span>
					
						<span class="property-value" aria-labelledby="subTitle-label"><g:fieldValue bean="${FAQInstance}" field="subTitle"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${FAQInstance?.author}">
				<li class="fieldcontain">
					<span id="author-label" class="property-label"><g:message code="FAQ.author.label" default="Author" /></span>
					
						<span class="property-value" aria-labelledby="author-label"><g:fieldValue bean="${FAQInstance}" field="author"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${FAQInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="FAQ.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${FAQInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${FAQInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="FAQ.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${FAQInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${FAQInstance?.publish}">
				<li class="fieldcontain">
					<span id="publish-label" class="property-label"><g:message code="FAQ.publish.label" default="Publish" /></span>
					
						<span class="property-value" aria-labelledby="publish-label"><g:formatBoolean boolean="${FAQInstance?.publish}" /></span>
					
				</li>
				</g:if>
			
    </ol>
    <g:form>
        <fieldset class="form-actions">
            <g:hiddenField name="id" value="${FAQInstance?.id}" />
            <g:link class="btn btn-primary" action="edit" id="${FAQInstance?.id}">
                <g:message code="default.button.edit.label" default="Edit" />
            </g:link>
            <g:actionSubmit class="btn btn-danger" action="delete"
                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
        </fieldset>
    </g:form>
</body>
</html>