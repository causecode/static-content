<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'inputWidget.label', default: 'InputWidget')}" />
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
					
                <g:sortableColumn property="validate" title="${message(code: 'inputWidget.validate.label', default: 'Validate')}" />
					
                <g:sortableColumn property="widgetKey" title="${message(code: 'inputWidget.widgetKey.label', default: 'Widget Key')}" />
					
                <g:sortableColumn property="widgetValue" title="${message(code: 'inputWidget.widgetValue.label', default: 'Widget Value')}" />
					
                <g:sortableColumn property="dateCreated" title="${message(code: 'inputWidget.dateCreated.label', default: 'Date Created')}" />
					
            </tr>
        </thead>
        <tbody>
            <g:each in="${inputWidgetInstanceList}" var="inputWidgetInstance">
                <tr>
                
                    <td><g:link action="show" id="${inputWidgetInstance.id}">${fieldValue(bean: inputWidgetInstance, field: "name")}</g:link></td>
					
                    <td>${fieldValue(bean: inputWidgetInstance, field: "label")}</td>
					
                    <td>${fieldValue(bean: inputWidgetInstance, field: "validate")}</td>
					
                    <td>${fieldValue(bean: inputWidgetInstance, field: "widgetKey")}</td>
					
                    <td>${fieldValue(bean: inputWidgetInstance, field: "widgetValue")}</td>
					
                    <td><g:formatDate date="${inputWidgetInstance.dateCreated}" /></td>
					
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