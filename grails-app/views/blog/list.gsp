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
    <r:require modules="tagcloud"/>
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

    <div class="row">
        <div class="col-sm-9">
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
                    <g:paginate action="list" total="${total}" params="[monthFilter: month, tag: params.tag]" />
                </ul>
            </div>
        </div>
        <div class="col-sm-3" style="border-left: 1px solid #eeeeee">
            <g:if test="${monthFilterList}">
                <h4>Blogs Posted in</h4>
                <hr>
                <div>
                    <g:link action="list" style="display: inherit;margin-bottom:5px;"
                        class=" month-filter-item ${params.monthFilter ?'':'selected' }">
                        ALL
                    </g:link>
                    <g:each in="${monthFilterList}" var="month">
                        <g:link action="list" params="[monthFilter: month, tag: params.tag]" style="display: inherit;margin-bottom:5px;"
                            class="bt month-filter-item ${params.monthFilter == month ?'selected':'' }">
                            ${month}
                        </g:link>
                    </g:each>
                </div><br>
            </g:if>
            <g:if test="${tagNameList}">
                <h4>
                    <i class="icon-tags"></i>
                    Tags
                </h4>
                <hr>
                <div class="blog-tags">
                    <g:each in="${tagNameList}" var="tag" status="index">
                        <g:link action="list" params="[tag: tag, monthFilter: params.monthFilter]" rel="${tagFrequesncyList[index]}" 
                            style="line-height: normal;">
                            ${tag}
                            <span>&nbsp;</span>
                        </g:link>${index < tagNameList.size() - 1 ? ' ' : '' }
                    </g:each>
                </div>
            </g:if>
        </div>
    </div>
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
</html>