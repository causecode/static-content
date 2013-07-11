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
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        ${menuInstance?.name}
        <com:bootstrapMenu id="${menuInstance?.id}"/> 
        <g:form>
            <fieldset class="form-actions">
                <g:hiddenField name="id" value="${menuInstance?.id}" />
                <g:link class="btn btn-primary" action="edit" id="${menuInstance?.id}">
                    <g:message code="default.button.edit.label" default="Edit" />
                </g:link>
                <g:actionSubmit class="btn btn-danger" action="delete" 
                    value="${message(code: 'default.button.delete.label', default: 'Delete')}" 
                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
            </fieldset>
        </g:form>
    </body>
</html>
