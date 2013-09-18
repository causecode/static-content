

<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'news.label', default: 'News')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
    <div class="page-header">
        <h1>
            <g:message code="default.show.label" args="[entityName]" />
        </h1>
    </div>
    <ol class="property-list news">

        <g:if test="${newsInstance?.body}">
            <li class="fieldcontain"><span id="body-label" class="property-label"><g:message
                        code="news.body.label" default="Body" /></span> <span class="property-value"
                aria-labelledby="body-label"><g:fieldValue bean="${newsInstance}" field="body" /></span></li>
        </g:if>

        <g:if test="${newsInstance?.title}">
            <li class="fieldcontain"><span id="title-label" class="property-label"><g:message
                        code="news.title.label" default="Title" /></span> <span class="property-value"
                aria-labelledby="title-label"><g:fieldValue bean="${newsInstance}" field="title" /></span></li>
        </g:if>

        <g:if test="${newsInstance?.subTitle}">
            <li class="fieldcontain"><span id="subTitle-label" class="property-label"><g:message
                        code="news.subTitle.label" default="Sub Title" /></span> <span class="property-value"
                aria-labelledby="subTitle-label"><g:fieldValue bean="${newsInstance}" field="subTitle" /></span></li>
        </g:if>

        <g:if test="${newsInstance?.author}">
            <li class="fieldcontain"><span id="author-label" class="property-label"><g:message
                        code="news.author.label" default="Author" /></span> <span class="property-value"
                aria-labelledby="author-label"><g:fieldValue bean="${newsInstance}" field="author" /></span></li>
        </g:if>

        <g:if test="${newsInstance?.dateCreated}">
            <li class="fieldcontain"><span id="dateCreated-label" class="property-label"><g:message
                        code="news.dateCreated.label" default="Date Created" /></span> <span class="property-value"
                aria-labelledby="dateCreated-label"><g:formatDate date="${newsInstance?.dateCreated}" /></span></li>
        </g:if>

        <g:if test="${newsInstance?.lastUpdated}">
            <li class="fieldcontain"><span id="lastUpdated-label" class="property-label"><g:message
                        code="news.lastUpdated.label" default="Last Updated" /></span> <span class="property-value"
                aria-labelledby="lastUpdated-label"><g:formatDate date="${newsInstance?.lastUpdated}" /></span></li>
        </g:if>

        <g:if test="${newsInstance?.publish}">
            <li class="fieldcontain"><span id="publish-label" class="property-label"><g:message
                        code="news.publish.label" default="Publish" /></span> <span class="property-value"
                aria-labelledby="publish-label"><g:formatBoolean boolean="${newsInstance?.publish}" /></span></li>
        </g:if>

    </ol>
    <g:form>
        <fieldset class="form-actions ">
            <g:hiddenField name="id" value="${newsInstance?.id}" />
            <g:link class="btn btn-default btn-primary" action="edit" id="${newsInstance?.id}">
                <g:message code="default.button.edit.label" default="Edit" />
            </g:link>
            <g:actionSubmit class="btn btn-default btn-danger" action="delete"
                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
        </fieldset>
    </g:form>
</body>
</html>