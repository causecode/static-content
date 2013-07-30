<g:hasErrors bean="${menuItemInstance}">
    <ul class="text-error">
        <g:eachError bean="${menuItemInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                <g:message error="${error}" /></li>
        </g:eachError>
    </ul>
</g:hasErrors>

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

<div class="control-group ${hasErrors(bean: menuItemInstance, field: 'roles', 'error')}">
    <label class="control-label" for="roles">
        <g:message code="menuItem.roles.label" default="Role" />
    </label>
    <div class="controls">
        <g:select name="roles" from="${['ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN','ROLE_JOB_BOARD_MANAGER']}" 
            value="${menuItemInstance?.roles}" multiple="true" />
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