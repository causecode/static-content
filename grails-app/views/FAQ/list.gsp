<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'FAQ.label', default: 'FAQs')}" />
<title><g:message code="default.faq.list.label" args="[entityName]" /></title>
</head>
<body>
    <content tag="breadcrumb">
            <content:breadcrumb map="['active': 'FAQs']"/>
    </content>
    <div class="page-header">
        <h1>
            <g:message code="default.faq.list.label" args="[entityName]" />
        </h1>
    </div>
    <table class="table table-bordered table-hover table-striped">
        <thead>
            <tr>

                <g:sortableColumn property="body" title="${message(code: 'FAQ.body.label', default: 'Body')}" />

                <g:sortableColumn property="title" title="${message(code: 'FAQ.title.label', default: 'Title')}" />

                <g:sortableColumn property="subTitle"
                    title="${message(code: 'FAQ.subTitle.label', default: 'Sub Title')}" />

                <g:sortableColumn property="author" title="${message(code: 'FAQ.author.label', default: 'Author')}" />

                <g:sortableColumn property="dateCreated"
                    title="${message(code: 'FAQ.dateCreated.label', default: 'Date Created')}" />

                <g:sortableColumn property="lastUpdated"
                    title="${message(code: 'FAQ.lastUpdated.label', default: 'Last Updated')}" />

            </tr>
        </thead>
        <tbody>
            <g:each in="${FAQInstanceList}" var="FAQInstance">
                <tr>

                    <td><g:link action="show" id="${FAQInstance.id}">
                            ${fieldValue(bean: FAQInstance, field: "body")}
                        </g:link></td>

                    <td>
                        ${fieldValue(bean: FAQInstance, field: "title")}
                    </td>

                    <td>
                        ${fieldValue(bean: FAQInstance, field: "subTitle")}
                    </td>

                    <td>
                        ${fieldValue(bean: FAQInstance, field: "author")}
                    </td>

                    <td><g:formatDate date="${FAQInstance.dateCreated}" /></td>

                    <td><g:formatDate date="${FAQInstance.lastUpdated}" /></td>

                </tr>
            </g:each>
            <g:if test="${!FAQInstanceList }">
                <tr>
                    <td>No record found. <g:link action="create">Create new</g:link>.
                    </td>
                </tr>
            </g:if>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${FAQInstanceTotal}" />
    </div>
</body>
</html>