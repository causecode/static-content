<g:hasErrors bean="${menuInstance}">
    <ul class="text-danger field-error icons-ul">
        <g:eachError bean="${menuInstance}" var="error">
            <li>
                <i class="icon-li icon-exclamation-sign"></i><g:message error="${error}" />
            </li>
        </g:eachError>
    </ul>
</g:hasErrors>

<div class="form-group ${hasErrors(bean: menuInstance, field: 'name', 'error')}">
    <label class="control-label col-sm-3" for="name"> <g:message code="menu.name.label" default="Name" />
    </label>
    <div class="col-sm-5">
        <g:textField name="name" required="" value="${menuInstance?.name}" class="form-control" autofocus="" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: menuInstance, field: 'roles', 'error')}">
    <label class="control-label col-sm-3" for="roles"> <g:message code="menu.roles.label" default="Role" />
    </label>
    <div class="col-sm-5">
        <g:select name="roles" from="${['ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN','ROLE_JOB_BOARD_MANAGER']}"
            value="${menuInstance?.roles}" multiple="true" class="form-control" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: menuInstance, field: 'showOnlyWhenLoggedIn', 'error')}">
    <label class="control-label col-sm-3" for="showOnlyWhenLoggedIn"> <g:message
            code='menu.showOnlyWhenLoggedIn.label' default="Show Only When Logged In" />
    </label>
    <div class="col-sm-5">
        <g:checkBox name="showOnlyWhenLoggedIn" optionKey="id" value="${menuInstance?.showOnlyWhenLoggedIn}" />
    </div>
</div>


<div class="form-group ${hasErrors(bean: menuInstance, field: 'menuItems', 'error')}">
    <label class="control-label col-sm-3" for="menuItems"><g:message code="menu.menuItems.label"
            default="Menu Items" />
    </label>
    <div class="col-sm-5">
        <ul class="one-to-many">
            <g:each in="${menuInstance?.menuItems?}" var="m">
                <li>
                    <g:link controller="menuIstem" action="show" id="${m?.id}">
                        ${m?.title.encodeAsHTML()}
                    </g:link></li>
            </g:each>
            <g:link controller="menuItem" action="create" params="['menu.id': menuInstance?.id]">
                ${message(code: 'default.add.label', args: [message(code: 'menuItem.label')])}
            </g:link>
        </ul>
    </div>
</div>

