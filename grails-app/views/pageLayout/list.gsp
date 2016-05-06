<!--  /*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */  -->

<%@ page import="com.causecode.content.PageLayout"%>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'pageLayout.label', default: 'PageLayout')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
    <content tag="breadcrumb"> <content:breadcrumb map="['active': 'PageLayout List']" /> </content>


    <div class="page-header">
        <h1>
            <g:message code="default.list.label" args="[entityName]" />
        </h1>
    </div>
    <table class="table table-bordered table-hover table-striped">
        <thead>
            <tr>
                <g:sortableColumn property="layoutName"
                    title="${message(code: 'pageLayout.layoutName.label', default: 'Layout Name')}" />
            </tr>
        </thead>
        <tbody>
            <g:each in="${pageLayoutInstanceList}" var="pageLayoutInstance">
                <tr>
                    <td><g:link action="show" id="${pageLayoutInstance.id}">
                            ${fieldValue(bean: pageLayoutInstance, field: "layoutName")}
                        </g:link></td>
                </tr>
            </g:each>
            <g:if test="${!pageLayoutInstanceList }">
                <tr>
                    <td colspan="5">No record found. <g:link action="create">Create new</g:link>.
                    </td>
                </tr>
            </g:if>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${pageLayoutInstanceTotal}" />
    </div>
</body>
</html>
