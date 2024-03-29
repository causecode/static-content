<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="\${message(code: '${domainClass.propertyName}.label', default: '${className}')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
    <div class="page-header">
        <h1>
            <g:message code="default.create.label" args="[entityName]" />
        </h1>
    </div>
    <g:form action="save" <%= multiPart ? ' enctype="multipart/form-data"' : '' %> class="form-horizontal">
        <fieldset>
            <g:render template="form" />
            <div class="form-actions well form-group">
                        <div class="col-lg-offset-2">
                <g:submitButton name="create" class="btn btn-default btn btn-default-primary"
                    value="\${message(code: 'default.button.create.label')}" />
            </div>
            </div>
        </fieldset>
    </g:form>
</body>
</html>