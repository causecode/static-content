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
    <g:set var="entityName" value="${message(code: 'blog.label', default: 'Latest Blog')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
    <div class="page-header">
        <h1 class="inline">Blog</h1>
        <sec:ifAnyGranted roles="ROLE_EMPLOYEE,ROLE_CONTENT_MANAGER">
            <g:link action='create' controller='blog'>Create a Blog</g:link>
        </sec:ifAnyGranted>
    </div>
    <g:if test="${!blogInstanceList }">
        <i class="icon-frown"></i> Sorry, no blog to display.
    </g:if>

    <div class="blog-list">
        <g:each in="${blogInstanceList}" status="index" var="blogInstance">
            <div class="blog-entry summary">
                <h2>
                    <g:link action="show" id="${blogInstance?.id}">
                        ${blogInstance.title }
                    </g:link>
                </h2>
                <h4>
                    ${blogInstance.subTitle}
                </h4>
                <g:render template="/blog/templates/additionalInfo" model="[instance: blogInstance]" />
                <div class="blog-body">
                    <br>
                    <strong>Description:</strong>
                    ${blogInstance.body}
                    <g:link action="show" id='${blogInstance.id}'>...more</g:link>
                </div>
            </div>
            <hr style="border: 1px solid #DEDEDE;">
        </g:each>

        <g:set var="total" value="${blogInstanceTotal }" />
        <g:set var="limit" value="${params.offset.toInteger() + params.max.toInteger() }" />
        <g:if test="${total }">
            <span class="muted help-block" style="margin-top: -20px">
                <small>Showing: <strong> ${params.int('offset') + 1 }-${limit > total ? total : limit }</strong>
                    of <strong> ${total }</strong>
                </small>
            </span>
        </g:if>

        <ul class="pagination">
            <g:paginate action="list" total="${total}" />
        </ul>
    </div>
</body>
</html>