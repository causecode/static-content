<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'news.label', default: 'News')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
    <content tag="breadcrumb">
            <content:breadcrumb map="['active': 'News List']"/>
    </content>
    <div class="page-header">
        <h1>
            <g:message code="default.list.label" args="[entityName]" />
        </h1>
    </div>
    <table class="table table-bordered table-hover table-striped">
        <thead>
            <tr>

                <g:sortableColumn property="body" title="${message(code: 'news.body.label', default: 'Body')}" />

                <g:sortableColumn property="title" title="${message(code: 'news.title.label', default: 'Title')}" />

                <g:sortableColumn property="subTitle"
                    title="${message(code: 'news.subTitle.label', default: 'Sub Title')}" />

                <g:sortableColumn property="author" title="${message(code: 'news.author.label', default: 'Author')}" />

                <g:sortableColumn property="dateCreated"
                    title="${message(code: 'news.dateCreated.label', default: 'Date Created')}" />

                <g:sortableColumn property="lastUpdated"
                    title="${message(code: 'news.lastUpdated.label', default: 'Last Updated')}" />

            </tr>
        </thead>
        <tbody>
            <g:each in="${newsInstanceList}" var="newsInstance">
                <tr>

                    <td><g:link action="show" id="${newsInstance.id}">
                            ${fieldValue(bean: newsInstance, field: "body")}
                        </g:link></td>

                    <td>
                        ${fieldValue(bean: newsInstance, field: "title")}
                    </td>

                    <td>
                        ${fieldValue(bean: newsInstance, field: "subTitle")}
                    </td>

                    <td>
                        ${fieldValue(bean: newsInstance, field: "author")}
                    </td>

                    <td><g:formatDate date="${newsInstance.dateCreated}" /></td>

                    <td><g:formatDate date="${newsInstance.lastUpdated}" /></td>

                </tr>
            </g:each>
            <g:if test="${!newsInstanceList }">
                <tr>
                    <td>No record found. <g:link action="create">Create new</g:link>.
                    </td>
                </tr>
            </g:if>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${newsInstanceTotal}" />
    </div>
</body>
</html>