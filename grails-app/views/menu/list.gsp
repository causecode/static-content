<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'menu.label', default: 'Menu')}" />
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
                <g:sortableColumn property="name" title="${message(code: 'menu.name.label', default: 'Name')}" />
                <g:sortableColumn property="dateCreated" title="${message(code: 'menu.dateCreated.label', default: 'Date Created')}" />
                <g:sortableColumn property="lastUpdated" title="${message(code: 'menu.lastUpdated.label', default: 'Last Updated')}" />
            </tr>
        </thead>
        <tbody>
            <g:each in="${menuInstanceList}" var="menuInstance">
                <tr>
                    <td><g:link action="show" id="${menuInstance.id}">${fieldValue(bean: menuInstance, field: "name")}</g:link></td>
                    <td><g:formatDate date="${menuInstance.dateCreated}" /></td>
                    <td><g:formatDate date="${menuInstance.lastUpdated}" /></td>
                </tr>
            </g:each>
            <g:if test="${!menuInstanceList }">
                <tr>
                    <td>
                        No record found. <g:link action="create">Create new</g:link>.
                    </td>
                </tr>
            </g:if>
        </tbody>
    </table>
    <div class="pager">
        <g:paginate total="${menuInstanceTotal}" />
    </div>
</body>
</html>