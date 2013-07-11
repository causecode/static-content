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
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="page-header">
            <h1>
                <g:message code="default.edit.label" args="[entityName]" />
            </h1>
        </div>
        <g:form class="form-horizontal" method="post" >
            <g:hiddenField name="id" value="${menuInstance?.id}" />
            <g:hiddenField name="version" value="${menuInstance?.version}" />
            <fieldset class="form">
                <g:render template="form" />
                <div class="controls">
                    <strong>Menu Items</strong><br/>
                    <g:if test="${menuInstance?.menuItems}">
                        <g:each in="${menuInstance.menuItems}">
                            <g:link controller="menuItem" action="show" id="${it.id}">${it.title}</g:link>
                            <g:link action="deleteChild" id="${it.id}">X</g:link><br>
                        </g:each>
                    </g:if>
                </div>
                <div class="form-actions">
                    <g:actionSubmit class="btn btn-primary" action="update"
                        value="${message(code: 'default.button.update.label')}" />
                    <g:actionSubmit class="btn btn-danger" action="delete"
                        value="${message(code: 'default.button.delete.label')}" formnovalidate=""
                        onclick="return confirm('${message(code: 'default.button.delete.confirm.message',default: 'Are you sure?')}');" />
                </div>
            </fieldset>
         </g:form>
    </body>
</html>
