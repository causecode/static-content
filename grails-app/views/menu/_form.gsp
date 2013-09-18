<g:hasErrors bean="${menuInstance}">
    <ul class="text-error">
        <g:eachError bean="${menuInstance}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                <g:message error="${error}" />
            </li>
        </g:eachError>
    </ul>
</g:hasErrors>

<div class="form-group ${hasErrors(bean: menuInstance, field: 'name', 'error')}">
    <label class="control-label col-sm-2" for="name"> <g:message code="menu.name.label" default="Name" />
    </label>
    <div class="col-sm-5">
        <g:textField name="name" required="" value="${menuInstance?.name}" class="form-control" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: menuInstance, field: 'roles', 'error')}">
    <label class="control-label col-sm-2" for="roles"> <g:message code="menu.roles.label" default="Role" />
    </label>
    <div class="col-sm-5">
        <g:select name="roles" from="${['ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN','ROLE_JOB_BOARD_MANAGER']}"
            value="${menuInstance?.roles}" multiple="true" noSelection="['':'']" class="form-control" />
    </div>
</div>

<div class="form-group ${hasErrors(bean: menuInstance, field: 'showOnlyWhenLoggedIn', 'error')}">
    <label class="control-label col-sm-2" for="showOnlyWhenLoggedIn"> <g:message
            code='menu.showOnlyWhenLoggedIn.label' default="Show Only When Logged In" />
    </label>
    <div class="col-sm-5">
        <g:checkBox name="showOnlyWhenLoggedIn" optionKey="id" value="${menuInstance?.showOnlyWhenLoggedIn}" />
    </div>
</div>


<div class="form-group ${hasErrors(bean: menuInstance, field: 'menuItems', 'error')}">
    <label class="control-label col-sm-2" for="menuItems"> <g:message code="menu.menuItems.label"
            default="Menu Items" />
    </label>
    <div class="col-sm-5">
        <ul class="one-to-many">
            <g:each in="${menuInstance?.menuItems?}" var="m">
                <li><g:link controller="menuIstem" action="show" id="${m?.id}">
                        ${m?.title.encodeAsHTML()}
                    </g:link></li>
            </g:each>
            </br>
            <g:link controller="menuItem" action="create" params="['menu.id': menuInstance?.id]">
                ${message(code: 'default.add.label', args: [message(code: 'menuItem.label', default: 'MenuItem')])}
            </g:link>
        </ul>
    </div>
</div>

