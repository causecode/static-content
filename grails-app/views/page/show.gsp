<html>
<head>
    <meta name="layout" content="${pageInstance.pageLayout?.layoutName ?: 'main' }" />
    <content:renderMetaTags contentInstance="${pageInstance }" />
    <title>${pageInstance.title }</title>
</head>
<body>
    <div class="page-header">
        <h1>
            ${pageInstance.title }
            <small>${pageInstance.subTitle }</small>
        </h1>
    </div>
    <%= pageInstance.body %>

    <content:canEdit>
        <g:form>
            <fieldset class="form-actions">
                <g:hiddenField name="id" value="${pageInstance?.id}" />
                <g:link class="btn btn-primary" action="edit" id="${pageInstance?.id}">
                    <g:message code="default.button.edit.label" />
                </g:link>
                <g:actionSubmit class="btn btn-danger" action="delete"
                    value="${message(code: 'default.button.delete.label')}"
                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
            </fieldset>
        </g:form>
    </content:canEdit>
</body>
</html>