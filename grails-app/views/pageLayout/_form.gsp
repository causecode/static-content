<!--  Copyright (c) 2011, CauseCode Technologies Pvt Ltd, India.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are not permitted. -->

<%@ page import="com.cc.content.PageLayout" %>

<div class="fieldcontain ${hasErrors(bean: pageLayoutInstance, field: 'layoutName', 'error')} ">
	<label for="layoutName">
		<g:message code="pageLayout.layoutName.label" default="Layout Name" />
		
	</label>
	<g:textField name="layoutName" value="${pageLayoutInstance?.layoutName}"/>
</div>

