<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'page.label', default: 'Page')}" />
<title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
    <content tag="breadcrumb">
            <content:breadcrumb map="['/page/list': 'Page List', 'active':'Create Page']"/>
    </content>
    <div class="page-header">
        <h1>
            <g:message code="default.create.label" args="[entityName]" />
        </h1>
    </div>
    <g:form action="save" class="form-horizontal">
        <fieldset>
            <g:render template="/content/form" model="[contentInstance: pageInstance]" plugin="content" />
            <div class="form-actions">
                <g:submitButton name="create" class="btn btn-default btn-primary"
                    value="${message(code: 'default.button.create.label')}" />
            </div>
        </fieldset>
    </g:form>
</body>
</html>