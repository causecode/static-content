<html>
<head>
    <meta name="layout" content="${pageInstance.pageLayout?.layoutName ?: 'main' }" />
    <content:renderMetaTags contentInstance="${pageInstance }" />
    <title>${pageInstance.title }</title>
</head>
<body>
    <content tag="breadcrumb">
        <content:breadcrumb map="['/page/list':'Page List','active':(pageInstance.title)]"/>
    </content>
    <div class="page-header">
        <h1>
            ${pageInstance.title }
        </h1>
         <g:if test="${pageInstance.subTitle }">
            <h4 class="page-subtitle">
                ${pageInstance.subTitle }
            </h4>
        </g:if>
    </div>
    <%= pageInstance.body %>

    <sec:access url="/page/create">
        <g:form>
            <fieldset class="form-actions">
                <g:hiddenField name="id" value="${pageInstance.id}" />
                <g:link class="btn btn-primary" action="edit" id="${pageInstance.id}">
                    <g:message code="default.button.edit.label" />
                </g:link>
                <g:actionSubmit class="btn btn-danger" action="delete"
                    value="${message(code: 'default.button.delete.label')}"
                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
            </fieldset>
        </g:form>
    </sec:access>
</body>
</html>