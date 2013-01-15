<%@ page import="com.cc.content.PageLayout" %>



<div class="fieldcontain ${hasErrors(bean: pageLayoutInstance, field: 'layoutFile', 'error')} ">
	<label for="layoutFile">
		<g:message code="pageLayout.layoutFile.label" default="Layout File" />
		
	</label>
	<g:textField name="layoutFile" value="${pageLayoutInstance?.layoutFile}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: pageLayoutInstance, field: 'layoutName', 'error')} ">
	<label for="layoutName">
		<g:message code="pageLayout.layoutName.label" default="Layout Name" />
		
	</label>
	<g:textField name="layoutName" value="${pageLayoutInstance?.layoutName}"/>
</div>

