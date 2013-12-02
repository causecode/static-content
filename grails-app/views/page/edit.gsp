<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'page.label', default: 'Page')}" />
<title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
    <content tag="breadcrumb">
            <content:breadcrumb map="['/page/list': 'Page List', 'active':'Edit Page']"/>
    </content>
    <div class="page-header">
        <h1>
            <g:message code="default.edit.label" args="[entityName]" />
        </h1>
    </div>
    <g:form class="form-horizontal jquery-form">
        <g:hiddenField name="id" value="${pageInstance?.id}" />
        <g:hiddenField name="version" value="${pageInstance?.version}" />
        <fieldset>
            <g:render template="/content/form" model="[contentInstance: pageInstance]" plugin="content" />
            <div class="form-actions">
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
