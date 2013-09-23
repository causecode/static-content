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
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
    <content tag="breadcrumb">
            <content:breadcrumb map="['/pageLayout/list': 'PageLayout List', 'active':(pageLayoutInstance.layoutName)]"/>
    </content>
    <div id="show-pageLayout" class="content page-header">
        <h1>
            <g:message code="default.show.label" args="[entityName]" />
        </h1>
    </div>
    <g:form class="form-horizontal">
        <ol class="property-list pageLayout">

            <g:if test="${pageLayoutInstance?.layoutName}">
                <li class="fieldcontain"><span id="layoutName-label" class="property-label"><g:message
                            code="pageLayout.layoutName.label" default="Layout Name" /></span> <span class="property-value"
                    aria-labelledby="layoutName-label"><g:fieldValue bean="${pageLayoutInstance}"
                            field="layoutName" /></span></li>
            </g:if>

        </ol>
        <fieldset class="form-actions">
            <g:hiddenField name="id" value="${pageLayoutInstance?.id}" />
            <g:link action="edit" id="${pageLayoutInstance?.id}" class="btn btn-primary">
                <g:message code="default.button.edit.label" default="Edit" />
            </g:link>
            <g:actionSubmit action="delete" class="btn btn-danger"
                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
    </g:form>
</body>
</html>
