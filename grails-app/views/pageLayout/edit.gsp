<!--  /*
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
<title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
    <content tag="breadcrumb">
            <content:breadcrumb map="['/pageLayout/list': 'PageLayout List', 'active':'Edit PageLayout']"/>
    </content>
    <div id="edit-pageLayout" class="content page-header">
        <h1>
            <g:message code="default.edit.label" args="[entityName]" />
        </h1>
    </div>
    <g:form class="form-horizontal">
        <g:hiddenField name="id" value="${pageLayoutInstance?.id}" />
        <g:hiddenField name="version" value="${pageLayoutInstance?.version}" />
        <fieldset>
            <g:render template="form" />
            <div class="form-actions">
                <g:actionSubmit action="update" class="btn btn-primary"
                    value="${message(code: 'default.button.update.label', default: 'Update')}" />
                <g:actionSubmit action="delete" class="btn btn-danger"
                    value="${message(code: 'default.button.delete.label', default: 'Delete')}" formnovalidate=""
                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
            </div>
        </fieldset>
    </g:form>
</body>
</html>
