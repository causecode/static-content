<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'menuItem.label', default: 'MenuItem')}" />
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
                <g:sortableColumn property="parent" title="${message(code: 'menuItem.parent.label', default: 'Parent')}" />
                <g:sortableColumn property="menu" title="${message(code: 'menuItem.menu.label', default: 'Menu')}" />
                <g:sortableColumn property="dateCreated" title="${message(code: 'menuItem.dateCreated.label', default: 'Date Created')}" />
                <g:sortableColumn property="lastUpdated" title="${message(code: 'menuItem.lastUpdated.label', default: 'Last Updated')}" />
                <g:sortableColumn property="title" title="${message(code: 'menuItem.title.label', default: 'Title')}" />
                <g:sortableColumn property="url" title="${message(code: 'menuItem.url.label', default: 'Url')}" />
            </tr>
        </thead>
        <tbody>
            <g:each in="${menuItemInstanceList}" var="menuItemInstance">
                <tr>
                    <td><g:link action="show" id="${menuItemInstance.id}">${fieldValue(bean: menuItemInstance, field: "parent.title")}</g:link></td>
                    <td>${fieldValue(bean: menuItemInstance, field: "title")}</td>
                    <td><g:formatDate date="${menuItemInstance.dateCreated}" /></td>
                    <td><g:formatDate date="${menuItemInstance.lastUpdated}" /></td>
                    <td>${fieldValue(bean: menuItemInstance, field: "title")}</td>
                    <td>${fieldValue(bean: menuItemInstance, field: "url")}</td>
                </tr>
            </g:each>
            <g:if test="${!menuItemInstanceList }">
                <tr>
                    <td>
                        No record found. <g:link action="create">Create new</g:link>.
                    </td>
                </tr>
            </g:if>
        </tbody>
    </table>
    <div class="pager">
        <g:paginate total="${menuItemInstanceTotal}" />
    </div>
</body>
</html>