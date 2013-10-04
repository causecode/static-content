<!-- /*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */  -->

<%@ page import="com.cc.content.PageLayout"%>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'pageLayout.label', default: 'PageLayout')}" />
<title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
    <content tag="breadcrumb">
            <content:breadcrumb map="['/pageLayout/list': 'PageLayout List', 'active':'Create PageLayout']"/>
    </content>
    <div id="create-pageLayout" class=" page-header content " role="main">
        <h1>
            <g:message code="default.create.label" args="[entityName]" />
        </h1>
    </div>

    <g:form action="save" class="form-horizontal">
        <fieldset>
            <g:render template="form" />
            <div class="form-actions">
                <g:submitButton name="create" class="save" class="btn  btn-primary"
                    value="${message(code: 'default.button.create.label', default: 'Create')}" />
            </div>
        </fieldset>
    </g:form>
</body>
</html>
