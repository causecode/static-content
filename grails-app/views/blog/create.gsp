<!--  /*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */ -->
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'blog.label')}" />
    <title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
    <content tag="breadcrumb">
            <content:breadcrumb map="['/blog/list': 'Blogs', 'active':'Create Blog']"/>
    </content>
    <div class="page-header">
        <h1>
            <g:message code="default.create.label" args="[entityName]" />
        </h1>
    </div>
    <g:form action="save" class="form-horizontal">
        <fieldset>
            <g:render template="/content/form" model="[contentInstance: blogInstance]" plugin="content" />
            <div class="form-actions">
                <g:submitButton name="create" class="btn btn-primary"
                    value="${message(code: 'default.button.create.label')}" />
            </div>
        </fieldset>
    </g:form>
</body>
</html>