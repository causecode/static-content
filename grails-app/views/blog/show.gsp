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
    <g:set var="entityName" value="${blogInstance}" />
    <content:renderMetaTags contentInstance="${blogInstance }" />
    <title>${entityName }</title>
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
    <r:require modules="prettyprint, tagcloud"/>
</head>

<body>
    <content tag="breadcrumb">
        <content:breadcrumb map="['/blog/list': 'Blogs', 'active':(blogInstance.title)]"/>
    </content>
    <div class="page-header">
        <h1>
            ${blogInstance.title }
        </h1>
        <g:if test="${blogInstance.subTitle }">
            <h4 class="blog-subtitle">
                ${blogInstance.subTitle}
            </h4>
        </g:if>
        <div class="blog-info">
            <g:render template="/blog/templates/additionalInfo" model="[id: blogInstance.id,
                publishedDate: blogInstance.publishedDate]" />
            <sec:access url="/blog/create">
                &nbsp;<small>
                    <g:link action="edit" id="${blogInstance.id}" class="clear-hover"><i class="icon-edit"></i></g:link>
                    <g:link action="delete" id="${blogInstance.id }" class="clear-hover"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');"><i class="icon-trash"></i></g:link>
                </small>
            </sec:access>
        </div>
    </div>
    <div class="blog-body">
        <%= blogInstance.body %>
    </div>
    <g:if test="${tagNameList}">
        <div class="blog-tags" style="margin-top: 20px;">
            <i class="icon-tags"></i>
            <g:each in="${tagNameList}" var="tag" status="index">
                <g:link action="list" params="[tag: tag]" rel="${tagFrequesncyList[index]}" >
                    ${tag}
                </g:link>${index < blogInstance.tags.size() - 1 ? ',' : '' }
            </g:each>
        </div>
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

    <g:render template="/blog/templates/commentOverlay" />

    <r:script>
        var startSize = ${grailsApplication.config.cc.plugins.content.tags.startSize }
        var endSize = ${grailsApplication.config.cc.plugins.content.tags.endSize }
        var startColor = "${grailsApplication.config.cc.plugins.content.tags.startColor }"
        var endColor = "${grailsApplication.config.cc.plugins.content.tags.endColor }"
        $(function () {
          $('div.blog-tags a').tagcloud({
              size: {start: parseInt(startSize), end: parseInt(endSize), unit:'px'},
              color: {start: "#"+startColor, end: "#"+endColor}
          });
        });
    </r:script>

</body>