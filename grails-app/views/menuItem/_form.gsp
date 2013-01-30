<%@ page import="com.cc.navigation.MenuItem" %>

<div class="fieldcontain ${hasErrors(bean: menuItemInstance, field: 'title', 'error')} ">
	<label for="title">
		<g:message code="menuItem.title.label" default="Title" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="title" value="${menuItemInstance?.title}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: menuItemInstance, field: 'url', 'error')} ">
	<label for="url">
		<g:message code="menuItem.url.label" default="Url" />
		<span class="required-indicator">*</span>
	</label>
	<g:field type="url" name="url" value="${menuItemInstance?.url}"/>
</div>

<fieldset class="addChildren">
		<button type="button" id="addChildren">Add Children</button>
</fieldset>

<div id="clildMainDiv">
</div>



