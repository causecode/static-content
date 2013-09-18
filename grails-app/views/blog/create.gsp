<!--  /*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */ -->

<%@ page import="com.cc.blog.Blog"%>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="main">
<ckeditor:resources />
<g:set var="entityName" value="${message(code: 'blog.label', default: 'Blog')}" />
<title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
    <div id="create-blog" class="page-header content" role="main">
        <h1>
            <g:message code="default.create.label" args="[entityName]" />
        </h1>
    </div>
    <g:form action="save" class="form-horizontal" enctype="multipart/form-data">
        <fieldset>
            <g:render template="form" />
            <div class="form-actions">
                    <g:submitButton name="create" class="save btn btn-default btn-primary"
                        value="${message(code: 'default.button.create.label', default: 'Create')}" />
            </div>
        </fieldset>
    </g:form>
</body>
</html>
