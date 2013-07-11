<!--  /*
 * Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are not permitted.
 */  -->

<%@ page import="com.cc.navigation.Menu" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'menu.label', default: 'Menu')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="page-header" id="list-menu">
            <h1>
                <g:message code="default.list.label" args="[entityName]" />
            </h1>
        </div>
        <table class="table table-bordered table-hover table-striped">
            <thead>
                <tr>
                    <g:sortableColumn property="Name" title="${message(code: 'FAQ.name.label', default: 'Name')}" />
                </tr>
            </thead>
            <tbody>
                <g:each in="${menuInstanceList}" var="menuInstance">
                    <tr>
                        <td><g:link action="show" id="${menuInstance.id}">${fieldValue(bean: menuInstance, field: "name")}</g:link></td>
                    </tr>
                </g:each>
                <g:if test="${!menuInstanceList }">
                    <tr>
                        <td>
                            No record found. <g:link action="create">Create new</g:link>.
                        </td>
                    </tr>
                </g:if>
            </tbody>
        </table>
        <div class="pager" >
            <g:paginate total="${menuInstanceTotal}" next="Forward" prev="Back" />
        </div>
    </body>
</html>
