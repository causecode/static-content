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
    <content:renderMetaTags contentInstance="${blogInstance }" />
    <title>${blogInstance.title }</title>
    <style type="text/css">
        .comment .comment-info {
            margin-bottom: 6px;
        }
        .comment.nested {
            margin-left: 50px;
        }
        .comment a i.icon-reply {
            font-size: 76%;
        }
    </style>
</head>

<body>
    <div class="page-header">
        <h1 class="inline">
            ${blogInstance.title }
        </h1>
        <content:canEdit>
            <g:link action="edit" id="${blogInstance.id}" class="clear-hover"><i class="icon-edit"></i></g:link>
            <g:link action="delete" id="${blogInstance.id }"  class="clear-hover"><i class="icon-trash"></i></g:link>
        </content:canEdit>
        <g:if test="${blogInstance.subTitle }">
            <h4>
                ${blogInstance.subTitle}
            </h4>
        </g:if>
        <g:render template="/blog/templates/additionalInfo" model="[id: blogInstance.id,
            dateCreated: blogInstance.dateCreated]" />
    </div>
    <div class="blog-body">
        <%= blogInstance.body %>
    </div>
    <g:if test="${blogInstance.tags }">
        <b>Tags: </b>
        <g:each in="${blogInstance.tags}" var="tag">
            <g:link action="findByTag" params="[tag: tag]">${tag}</g:link>
        </g:each>
    </g:if>

    <div class="page-header">
        <h2 class="inline">Comments</h2>
        &nbsp;&nbsp;
        <a href="#comment-overlay" class="commentButton" data-toggle="modal" data-comment-id="">Add</a>
    </div>

    <ul class="media-list">
        <g:each in="${comments}" var="commentInstance">
            <content:comment commentInstance="${commentInstance}" />
        </g:each>
    </ul>
    <g:if test="${!comments }">
        Sorry, no comments to display.
    </g:if>

    <g:render template="/blog/templates/commentOverlay" plugin="comment" />
</body>
</html>