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
    <meta name="revisit-after" content="2 days">
    <g:set var="entityName" value="${message(code: 'blog.label', default: 'Blogs')}" />
    <title><g:message code="default.blog.list.label" args="[entityName]" /></title>
</head>
<body>
    <content tag="breadcrumb">
            <content:breadcrumb map="['active': 'Blogs']"/>
    </content>
    <div class="page-header">
        <h1 class="inline">
            <g:message code="default.blog.list.label" args="[entityName]" />
        </h1>
        <sec:access url="/blog/create">
            <g:link action='create' controller='blog'>Create a Blog</g:link>
        </sec:access>
    </div>
    <g:if test="${!blogInstanceList }">
        <i class="icon-frown"></i> Sorry, no blog to display.
    </g:if>

    <div class="blog-list">
        <g:each in="${blogInstanceList}">
            <div class="blog-entry summary">
                <h2>
                    <a href="<content:searchLink id='${it.id }' />">
                        ${it.title }
                    </a>
                </h2>
                <h4>
                    ${it.subTitle}
                </h4>
                <g:render template="/blog/templates/additionalInfo" model="[publishedDate: it.publishedDate, id: it.id]" />
                <div class="blog-body">
                    <br>
                    ${it.body}
                    <a href="<content:searchLink id='${it.id }' />">...more</a>
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
            <g:paginate action="list" total="${total}" params="[tag: params.tag]" />
        </ul>
    </div>
</body>
</html>