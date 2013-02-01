<%@ page import="com.cc.navigation.Menu" %>

<div class="fieldcontain ${hasErrors(bean: menuInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="menu.name.label" default="Name" />
		
	</label>
	<g:textField name="name" value="${menuInstance?.name}"/>
</div>

<fieldset class="addChildren">
		<button type="button" id="addChildren">Add Children</button>
</fieldset>

<div class="fieldcontain ${hasErrors(bean: menuInstance, field: 'menuItem', 'error')}" id="toggleDiv">
	<label for="menuItem">
		<g:message code="menu.menuItem.label" default="Menu Item" />
		
	</label>
	<g:select name="menuItem" from="${topLevelMenuItems}" multiple="multiple"  optionValue="title" optionKey="id" size="5" value="${menuInstance?.menuItem*.id}" class="many-to-many"/>
</div>

<div id="clildMenuItemDiv">
</div>





