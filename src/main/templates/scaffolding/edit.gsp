<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
<title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
    <div class="page-header">
        <h1>
            <g:message code="default.edit.label" args="[entityName]" />
        </h1>
    </div>
    <g:form class="form-horizontal" <%= multiPart ? ' enctype="multipart/form-data"' : '' %>>
        <g:hiddenField name="id" value="\${${propertyName}?.id}" />
        <g:hiddenField name="version" value="\${${propertyName}?.version}" />
        <fieldset class="form">
            <g:render template="form" />
            <div class="form-actions well form-group">
                        <div class="col-lg-offset-2">
                <g:actionSubmit class="btn btn-default btn btn-default-primary" action="update"
                    value="\${message(code: 'default.button.update.label')}" />
                <g:actionSubmit class="btn btn-default btn btn-default-danger" action="delete"
                    value="\${message(code: 'default.button.delete.label')}" formnovalidate=""
                    onclick="return confirm('\${message(code: 'default.button.delete.confirm.message')}');" />
            </div>
            </div>
        </fieldset>
    </g:form>
</body>
</html>
