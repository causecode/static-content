<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'news.label', default: 'News')}" />
<title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
    <content tag="breadcrumb">
            <content:breadcrumb map="['/news/list': 'News List', 'active':'Edit News']"/>
    </content>
    <div class="page-header">
        <h1>
            <g:message code="default.edit.label" args="[entityName]" />
        </h1>
    </div>
    <g:form class="form-horizontal">
        <g:hiddenField name="id" value="${newsInstance?.id}" />
        <g:hiddenField name="version" value="${newsInstance?.version}" />
        <fieldset class="form">
            <g:render template="form" />
            <div class="form-actions ">
                <g:actionSubmit class="btn btn-default btn-primary" action="update"
                    value="${message(code: 'default.button.update.label')}" />
                <g:actionSubmit class="btn btn-default btn-danger" action="delete"
                    value="${message(code: 'default.button.delete.label')}" formnovalidate=""
                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
            </div>
        </fieldset>
    </g:form>
</body>
</html>
