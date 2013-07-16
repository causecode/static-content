<g:hasErrors bean="${menuItemInstance}">
    <ul class="text-error">
        <g:eachError bean="${menuItemInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                <g:message error="${error}" /></li>
        </g:eachError>
    </ul>
</g:hasErrors>

<div class="control-group ${hasErrors(bean: menuItemInstance, field: 'parent', 'error')}">
    <label class="control-label" for="parent">
        <g:message code="menuItem.parent.label" default="Parent" />
    </label>
    <div class="controls">
        <g:select id="parent" name="parent.id" from="${com.cc.content.navigation.MenuItem.list()}" 
            optionKey="id" value="${menuItemInstance?.parent?.id}" class="many-to-one" 
            noSelection="['null': '']" optionValue="title"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: menuItemInstance, field: 'menu', 'error')}">
    <label class="control-label" for="menu">
        <g:message code="menuItem.menu.label" default="Menu" />
    </label>
    <div class="controls">
        <g:select id="menu" name="menu.id" from="${com.cc.content.navigation.Menu.list()}" optionValue="name"
            optionKey="id" value="${menuItemInstance?.menu?.id}" class="many-to-one" noSelection="['': '']"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: menuItemInstance, field: 'roles', 'error')}">
    <label class="control-label" for="roles">
        <g:message code="menuItem.roles.label" default="Role" />
    </label>
    <div class="controls">
        <g:select name="roles" from="${['ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN','ROLE_JOB_BOARD_MANAGER']}" 
            value="${menuItemInstance?.roles}" multiple="true" noSelection="['':'None']"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: menuItemInstance, field: 'title', 'error')}">
    <label class="control-label" for="title">
        <g:message code="menuItem.title.label" default="Title" />
    </label>
    <div class="controls">
        <g:textField name="title" value="${menuItemInstance?.title}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: menuItemInstance, field: 'url', 'error')}">
    <label class="control-label" for="url">
        <g:message code="menuItem.url.label" default="Url" />
    </label>
    <div class="controls">
        <g:textField name="url" value="${menuItemInstance?.url}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: menuItemInstance, field: 'showOnlyWhenLoggedIn', 'error')}">
    <label class="control-label" for="showOnlyWhenLoggedIn">
        <g:message code='menuItem.showOnlyWhenLoggedIn.label' default="Show Only When Logged In" />
    </label>
    <div class="controls">
        <g:checkBox name="showOnlyWhenLoggedIn" optionKey="id" 
            value="${menuItemInstance?.showOnlyWhenLoggedIn}"/>
    </div>
</div>

<div class="control-group ${hasErrors(bean: menuItemInstance, field: 'childItems', 'error')}">
    <label class="control-label" for="childItems">
        <g:message code="menuItem.childItems.label" default="Child Items" />
    </label>
    <div class="controls">
        <ul class="one-to-many">
            <g:each in="${menuItemInstance?.childItems?}" var="c">
                <li><g:link controller="menuItem" action="show" id="${c.id}">${c?.title.encodeAsHTML()}</g:link></li>
            </g:each>
            </br>
            <g:link controller="menuItem" action="create" params="['menuItem.id': menuItemInstance?.id]">
                ${message(code: 'default.add.label', args: [message(code: 'menuItem.label', default: 'MenuItem')])}
            </g:link>
        </ul>
    </div>
</div>