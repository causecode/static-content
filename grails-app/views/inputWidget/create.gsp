<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'inputWidget.label', default: 'InputWidget')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
    <r:require modules="inputWidget"/>
</head>
<body>
    <div class="page-header">
        <h1>
            <g:message code="default.create.label" args="[entityName]" />
        </h1>
    </div>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span6">
            <g:form action="save"  class="form-horizontal">
                <fieldset>
                    <g:render template="form" />
                    <div class="form-actions">
                        <g:submitButton name="create" class="btn btn-primary"
                            value="${message(code: 'default.button.create.label')}" />
                    </div>
                </fieldset>
            </g:form>
            </div>
            <div class="span6 thumbnail">
                <g:render template="previewInputWidget" />
            </div>
        </div>
    </div>
</body>
</html>