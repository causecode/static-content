<r:require module="validation" />

<g:hasErrors bean="${menuInstance}">
    <ul class="text-danger field-error icons-ul">
        <g:eachError bean="${menuInstance}" var="error">
            <li>
                <i class="icon-li icon-exclamation-sign"></i><g:message error="${error}" />
            </li>
        </g:eachError>
    </ul>
</g:hasErrors>

<div class="form-group ${hasErrors(bean: menuInstance, field: 'name', 'has-error')}">
    <label class="control-label col-sm-3" for="name"> <g:message code="menu.name.label" default="Name" />
    </label>
    <div class="col-sm-5">
        <g:textField name="name" required="" value="${menuInstance?.name}" class="form-control" autofocus="" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: menuInstance, field: 'roles', 'has-error')}">
    <label class="control-label col-sm-3" for="roles"><g:message code="menu.roles.label" default="Role" /></label>
    <div class="col-sm-5">
        <g:select name="roles" from="${Role.findAll()}" optionKey="authority" optionValue="authority"
            value="${menuInstance?.roles}" multiple="true" class="form-control" noSelection="['':'Select Roles']" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: menuInstance, field: 'showOnlyWhenLoggedIn', 'has-error')}">
    <label class="control-label col-sm-3" for="showOnlyWhenLoggedIn"> <g:message
            code='menu.showOnlyWhenLoggedIn.label' default="Show Only When Logged In" />
    </label>
    <div class="col-sm-5">
        <div class="checkbox">
            <label>
                <g:checkBox name="showOnlyWhenLoggedIn" optionKey="id" value="${menuInstance?.showOnlyWhenLoggedIn}" />
            </label>
        </div>
    </div>
</div>