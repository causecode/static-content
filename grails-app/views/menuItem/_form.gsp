<!--  Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are not permitted. -->

<%@ page import="com.cc.navigation.*" %>

<div class="fieldcontain ${hasErrors(bean: menuItemInstance, field: 'title', 'error')} ">
	<label for="title">
		<g:message code="menuItem.title.label" default="Title" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="title" class="required" value="${menuItemInstance?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: menuItemInstance, field: 'url', 'error')} ">
	<label for="url">
		<g:message code="menuItem.url.label" default="Url" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="text" class="required url" name="url" value="${menuItemInstance?.url}"/>
</div>

<g:select name="selectedMenu"
          from="${Menu.list()}" noSelection="['':'-Choose Menu-']"
          value="${menuItemInstance?.menu?.id}"
          optionKey="id" optionValue="name"/>

<fieldset class="addChildren">
		<button type="button" id="addChildren">Add Children</button>
</fieldset>

<div id="clildMainDiv">
</div>



