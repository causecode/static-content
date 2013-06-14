<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'textFormat.label', default: 'TextFormat')}" />
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
            
                <g:sortableColumn property="dateCreated" title="${message(code: 'textFormat.dateCreated.label', default: 'Date Created')}" />
					
                <g:sortableColumn property="lastUpdated" title="${message(code: 'textFormat.lastUpdated.label', default: 'Last Updated')}" />
					
                <g:sortableColumn property="name" title="${message(code: 'textFormat.name.label', default: 'Name')}" />
					
                <g:sortableColumn property="roles" title="${message(code: 'textFormat.roles.label', default: 'Roles')}" />
					
                <g:sortableColumn property="allowedTags" title="${message(code: 'textFormat.allowedTags.label', default: 'Allowed Tags')}" />
					
                <g:sortableColumn property="editor" title="${message(code: 'textFormat.editor.label', default: 'Editor')}" />
					
            </tr>
        </thead>
        <tbody>
            <g:each in="${textFormatInstanceList}" var="textFormatInstance">
                <tr>
                
                    <td><g:link action="show" id="${textFormatInstance.id}">${fieldValue(bean: textFormatInstance, field: "dateCreated")}</g:link></td>
					
                    <td><g:formatDate date="${textFormatInstance.lastUpdated}" /></td>
					
                    <td>${fieldValue(bean: textFormatInstance, field: "name")}</td>
					
                    <td>${fieldValue(bean: textFormatInstance, field: "roles")}</td>
					
                    <td>${fieldValue(bean: textFormatInstance, field: "allowedTags")}</td>
					
                    <td><g:formatBoolean boolean="${textFormatInstance.editor}" /></td>
					
                </tr>
            </g:each>
            <g:if test="${!textFormatInstanceList }">
                <tr>
                    <td>
                        No record found. <g:link action="create">Create new</g:link>.
                    </td>
                </tr>
            </g:if>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${textFormatInstanceTotal}" />
    </div>
</body>
</html>