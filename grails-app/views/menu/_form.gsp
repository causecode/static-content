<!--  Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are not permitted. -->

<%@ page import="com.cc.navigation.Menu" %>

<g:hasErrors bean="${menuInstance}">
    <ul class="text-error">
        <g:eachError bean="${menuInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                <g:message error="${error}" /></li>
        </g:eachError>
    </ul>
</g:hasErrors>

<div class="control-group ${hasErrors(bean: menuInstance, field: 'name', 'error')}">
    <label class="control-label" for="name">
        <g:message code="menu.name.label" default="Name" />
    </label>
    <div class="controls">
        <g:textField name="name" required="" value="${menuInstance?.name}"/>
    </div>
</div>

<div id="clildMenuItemDiv">
</div>





