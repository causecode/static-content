<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'currency.label', default: 'Currency')}" />
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
            
                <g:sortableColumn property="dateCreated" title="${message(code: 'currency.dateCreated.label', default: 'Date Created')}" />
					
                <g:sortableColumn property="lastUpdated" title="${message(code: 'currency.lastUpdated.label', default: 'Last Updated')}" />
					
                <g:sortableColumn property="code" title="${message(code: 'currency.code.label', default: 'Code')}" />
					
                <g:sortableColumn property="name" title="${message(code: 'currency.name.label', default: 'Name')}" />
					
            </tr>
        </thead>
        <tbody>
            <g:each in="${currencyInstanceList}" var="currencyInstance">
                <tr>
                
                    <td><g:link action="show" id="${currencyInstance.id}">${fieldValue(bean: currencyInstance, field: "dateCreated")}</g:link></td>
					
                    <td><g:formatDate date="${currencyInstance.lastUpdated}" /></td>
					
                    <td>${fieldValue(bean: currencyInstance, field: "code")}</td>
					
                    <td>${fieldValue(bean: currencyInstance, field: "name")}</td>
					
                </tr>
            </g:each>
            <g:if test="${!currencyInstanceList }">
                <tr>
                    <td>
                        No record found. <g:link action="create">Create new</g:link>.
                    </td>
                </tr>
            </g:if>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${currencyInstanceTotal}" />
    </div>
</body>
</html>