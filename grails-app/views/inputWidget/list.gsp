<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'inputWidget.label', default: 'Input Widget')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
    <div class="page-header">
        <h1>
            <g:message code="default.list.label" args="[entityName]" />
        </h1>
    </div>
    <table class="table table-bordered table-hover table-striped">
        <thead>
            <tr>
            
                <g:sortableColumn property="name" title="${message(code: 'inputWidget.name.label', default: 'Name')}" />
					
                <g:sortableColumn property="label" title="${message(code: 'inputWidget.label.label', default: 'Label')}" />
					
                <g:sortableColumn property="validation" title="${message(code: 'inputWidget.validation.label', default: 'Validation')}" />
					
                <g:sortableColumn property="widgetKeys" title="${message(code: 'inputWidget.widgetKeys.label', default: 'Widget Keys')}" />
					
                <g:sortableColumn property="widgetValues" title="${message(code: 'inputWidget.widgetValues.label', default: 'Widget Values')}" />
					
            </tr>
        </thead>
        <tbody>
            <g:each in="${inputWidgetInstanceList}" var="inputWidgetInstance">
                <tr>
                
                    <td><g:link action="show" id="${inputWidgetInstance.id}">${fieldValue(bean: inputWidgetInstance, field: "name")}</g:link></td>
					
                    <td>${fieldValue(bean: inputWidgetInstance, field: "label")}</td>
					
                    <td>${fieldValue(bean: inputWidgetInstance, field: "validation")}</td>
					
                    <td>${fieldValue(bean: inputWidgetInstance, field: "widgetKeys")}</td>
					
                    <td>${fieldValue(bean: inputWidgetInstance, field: "widgetValues")}</td>
                </tr>
            </g:each>
            <g:if test="${!inputWidgetInstanceList }">
                <tr>
                    <td>
                        No record found. <g:link action="create">Create new</g:link>.
                    </td>
                </tr>
            </g:if>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${inputWidgetInstanceTotal}" />
    </div>
</body>
</html>