<!--  /*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */ -->

<%@ page import="com.cc.content.blog.Blog"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<ckeditor:resources />
<g:set var="entityName" value="${message(code: 'blog.label')}" />
<title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
    <content tag="breadcrumb">
            <content:breadcrumb map="['/blog/list': 'Blogs', 'active':'Edit Blog']"/>
    </content>
    <div id="edit-blog" class="content page-header" role="main">
        <h1>
            <g:message code="default.edit.label" args="[entityName]" />
        </h1>
    </div>
    <g:form class="form-horizontal" enctype="multipart/form-data">
        <g:hiddenField name="id" value="${blogInstance?.id}" />
        <g:hiddenField name="version" value="${blogInstance?.version}" />
        <fieldset>
            <g:render template="/content/form" model="[contentInstance: blogInstance]" plugin="content" />
            <div class="form-actions">
                <g:actionSubmit class="save btn btn-primary" action="update"
                    value="${message(code: 'default.button.update.label', default: 'Update')}" />
                <g:actionSubmit class="delete btn btn-danger" action="delete"
                    value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate=""
                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
            </div>
        </fieldset>
    </g:form>
</body>
</html>
