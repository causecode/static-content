<!-- /*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */ -->

<html lang="en">
<head>
    <meta http-equiv="blog-Type" blog="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'blog.label')}" />
    <title>${blogInstance.title }</title>
    <style type="text/css">
        div.comment {
            margin-left: 50px;
        }
    </style>
</head>

<body>
    <div class="page-header">
        <h1>
            ${blogInstance.title }
        </h1>
        <h4>
            ${blogInstance.subTitle}
        </h4>
        <g:render template="/blog/templates/additionalInfo" model="[instance: blogInstance]" />
    </div>
    <%= blogInstance.body %>
    <g:if test="${blogInstance.tags }">
        <b>Tags: </b>
        <g:each in="${blogInstance.tags}" var="tag">
            <g:link action="findByTag" params="[tag: tag]">${tag}</g:link>
        </g:each>
    </g:if>
    <content:canEdit>
        <g:form>
            <fieldset>
                <div class="form-actions">
                    <g:hiddenField name="id" value="${blogInstance?.id}" />
                    <g:link class="edit btn btn-default btn-primary" action="edit" id="${blogInstance?.id}">
                        <g:message code="default.button.edit.label" default="Edit" />
                    </g:link>
                    <g:actionSubmit class="delete btn btn-default btn-danger" action="delete"
                        value="${message(code: 'default.button.delete.label')}"
                        onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
                </div>
            </fieldset>
        </g:form>
    </content:canEdit>

    <div class="page-header">
        <h2 class="inline">Comments</h2>
        &nbsp;&nbsp;<a href="#comment-overlay" class="commentButton" data-toggle="modal" data-comment-id="">Add</a>
    </div>

    <g:each in="${comments}">
        <g:render template="/blog/templates/comments" model="[commentInstance: it]" />
    </g:each>
    <g:if test="${!comments }">
        Sorry, no comments to display.
    </g:if>

    <g:render template="/blog/templates/commentOverlay" plugin="comment" />
</body>
</html>
