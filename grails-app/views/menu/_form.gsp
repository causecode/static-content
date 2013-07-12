<g:hasErrors bean="${menuInstance}">
    <ul class="text-error">
        <g:eachError bean="${menuInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                <g:message error="${error}" />
            </li>
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

<div class="control-group ${hasErrors(bean: menuInstance, field: 'menuItems', 'error')}">
    <label class="control-label" for="menuItems">
        <g:message code="menu.menuItems.label" default="Menu Items" />
    </label>
    <div class="controls">
        <ul class="one-to-many">
            <g:each in="${menuInstance?.menuItems?}" var="m">
                <li><g:link controller="menuItem" action="show" id="${m.id}">${m?.title.encodeAsHTML()}</g:link></li>
            </g:each>
            </br>
            <g:link controller="menuItem" action="create" params="['menu.id': menuInstance?.id]">
                ${message(code: 'default.add.label', args: [message(code: 'menuItem.label', default: 'MenuItem')])}
            </g:link>
        </ul>
    </div>
</div>

