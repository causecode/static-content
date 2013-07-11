<!-- /*
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
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="page-header">
            <h1>
                <g:message code="default.create.label" args="[entityName]" />
            </h1>
            <g:form action="save" class="form-horizontal">
                <fieldset>
                    <g:render template="form"/>
                    <div class="form-actions">
                       <div class="fieldcontain ${hasErrors(bean: menuInstance, field: 'menuItem', 'error')}" id="toggleDiv">
                           <label for="menuItem">
                               <g:message code="menu.menuItem.label" default="Menu Items" />
                           </label>
                            <g:select name="menuItem" from="${topLevelMenuItems}" multiple="multiple"  
                                optionValue="title" optionKey="id" size="5" value="${menuInstance?.menuItems*.id}" 
                                class="many-to-many"/>
                       </div>
                       <g:submitButton name="addChildren" class="btn btn-primary" 
                            value="${message(code: 'default.button.addChildren.label', default: 'Add Children')}" />
                       <g:submitButton name="create" class="btn btn-primary" 
                            value="${message(code: 'default.button.create.label', default: 'Create')}" />
                    </div>
                </fieldset>
            </g:form>
        </div>
        <script type="text/javascript">
            $("#addChildren").click ( function () {
                $("#toggleDiv").show();
                $("#addChildren").hide();
            });
        </script>
        <script>
            jQuery(document).ready(function() { 
                $("#toggleDiv").hide();
            });
        </script>
    </body>
</html>
