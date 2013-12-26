<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'page.label', default: 'Page')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
    <content tag="breadcrumb">
            <content:breadcrumb map="['active': 'Page List']"/>
    </content>
    <div class="page-header">
        <h1>
            <g:message code="default.list.label" args="[entityName]" />
        </h1>
    </div>
    <table class="table table-bordered table-hover table-striped">
        <thead>
            <tr>
                <g:sortableColumn property="title" title="${message(code: 'page.title.label', default: 'Title')}" />
                <g:sortableColumn property="subTitle"
                    title="${message(code: 'page.subTitle.label', default: 'Sub Title')}" />
                <g:sortableColumn property="author" title="${message(code: 'page.author.label', default: 'Author')}" />
                <g:sortableColumn property="dateCreated"
                    title="${message(code: 'page.dateCreated.label', default: 'Date Created')}" />
                <g:sortableColumn property="lastUpdated"
                    title="${message(code: 'page.lastUpdated.label', default: 'Last Updated')}" />
            </tr>
        </thead>
        <tbody>
            <g:each in="${pageInstanceList}" var="pageInstance">
                <tr>
                    <td><g:link url="${pageInstance.searchLink()}">
                            ${fieldValue(bean: pageInstance, field: "title")}
                        </g:link></td>
                    <td>
                        ${fieldValue(bean: pageInstance, field: "subTitle")}
                    </td>
                    <td>
                        ${fieldValue(bean: pageInstance, field: "author")}
                    </td>
                    <td><g:formatDate date="${pageInstance.dateCreated}" /></td>
                    <td><g:formatDate date="${pageInstance.lastUpdated}" /></td>
                </tr>
            </g:each>
            <g:if test="${!pageInstanceList }">
                <tr>
                    <td colspan="5">No record found. <g:link action="create">Create new</g:link>.
                    </td>
                </tr>
            </g:if>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${pageInstanceTotal}" />
    </div>
</body>
</html>