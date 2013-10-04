<g:hasErrors bean="${menuItemInstance}">
    <ul class="text-danger field-error icons-ul">
        <g:eachError bean="${menuItemInstance}" var="error">
            <li>
                <i class="icon-li icon-exclamation-sign"></i><g:message error="${error}"/>
            </li>
        </g:eachError>
    </ul>
</g:hasErrors>

<div class="form-group ${hasErrors(bean: menuItemInstance, field: 'title', 'error')}">
    <label class="control-label col-sm-4" for="title"> <g:message code="menuItem.title.label" default="Title" />
    </label>
    <div class="col-sm-6">
        <g:textField name="title" value="${menuItemInstance?.title}" class="form-control" autofocus="autofocus" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: menuItemInstance, field: 'url', 'error')}">
    <label class="control-label col-sm-4" for="url"> <g:message code="menuItem.url.label" default="Url" />
    </label>
    <div class="col-sm-6">
        <g:textField name="url" value="${menuItemInstance?.url}" class="form-control" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: menuItemInstance, field: 'roles', 'error')}">
    <label class="control-label col-sm-4" for="roles"> <g:message code="menuItem.roles.label" default="Role" />
    </label>
    <div class="col-sm-6">
        <g:select name="roles" from="${grailsApplication.config.cc.plugins.content.rolesForMenuMenuItem}"
            value="${menuItemInstance?.roles}" multiple="true" class="form-control" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: menuItemInstance, field: 'showOnlyWhenLoggedIn', 'error')}">
    <label class="control-label col-sm-4" for="showOnlyWhenLoggedIn"> <g:message
            code='menuItem.showOnlyWhenLoggedIn.label' default="Show Only When Logged In" />
    </label>
    <div class="col-sm-6">
        <g:checkBox name="showOnlyWhenLoggedIn" optionKey="id" value="${menuItemInstance?.showOnlyWhenLoggedIn}"/>
    </div>
</div>