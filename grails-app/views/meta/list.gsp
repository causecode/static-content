<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'meta.label', default: 'Meta')}" />
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
            
                <g:sortableColumn property="type" title="${message(code: 'meta.type.label', default: 'Type')}" />
					
                <g:sortableColumn property="value" title="${message(code: 'meta.value.label', default: 'Value')}" />
					
                <g:sortableColumn property="dateCreated" title="${message(code: 'meta.dateCreated.label', default: 'Date Created')}" />
					
                <g:sortableColumn property="lastUpdated" title="${message(code: 'meta.lastUpdated.label', default: 'Last Updated')}" />
					
            </tr>
        </thead>
        <tbody>
            <g:each in="${metaInstanceList}" var="metaInstance">
                <tr>
                
                    <td><g:link action="show" id="${metaInstance.id}">${fieldValue(bean: metaInstance, field: "type")}</g:link></td>
					
                    <td>${fieldValue(bean: metaInstance, field: "value")}</td>
					
                    <td><g:formatDate date="${metaInstance.dateCreated}" /></td>
					
                    <td><g:formatDate date="${metaInstance.lastUpdated}" /></td>
					
                </tr>
            </g:each>
            <g:if test="${!metaInstanceList }">
                <tr>
                    <td>
                        No record found. <g:link action="create">Create new</g:link>.
                    </td>
                </tr>
            </g:if>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${metaInstanceTotal}" />
    </div>
</body>
</html>