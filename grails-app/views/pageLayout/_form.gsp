<%@ page import="com.cc.content.PageLayout" %>

<div class="fieldcontain ${hasErrors(bean: pageLayoutInstance, field: 'layoutName', 'error')} ">
	<label for="layoutName">
		<g:message code="pageLayout.layoutName.label" default="Layout Name" />
		
	</label>
	<g:textField name="layoutName" value="${pageLayoutInstance?.layoutName}"/>
</div>

