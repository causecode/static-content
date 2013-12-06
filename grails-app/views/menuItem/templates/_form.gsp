<div class="form-group">
    <label class="control-label col-sm-4" for="title"><g:message code="menuItem.title.label" default="Title" /></label>
    <div class="col-sm-6">
        <g:textField name="title" class="form-control" required="" autofocus="autofocus" />
    </div>
</div>

<div class="form-group">
    <label class="control-label col-sm-4" for="url"><g:message code="menuItem.url.label" default="Url" /></label>
    <div class="col-sm-6">
        <g:textField name="url" class="form-control" required="" />
    </div>
</div>

<div class="form-group">
    <label class="control-label col-sm-4" for="roles"><g:message code="menuItem.roles.label" default="Role" /></label>
    <div class="col-sm-6">
        <g:select name="roles" from="${Role.findAll()}" optionKey="authority" optionValue="authority"
            multiple="true" class="form-control" noSelection="['':'Select Roles']" />
    </div>
</div>

<div class="form-group">
    <label class="control-label col-sm-4" for="showOnlyWhenLoggedIn">
        <g:message code='menuItem.showOnlyWhenLoggedIn.label' default="Show Only When Logged In" />
    </label>
    <div class="col-sm-6">
        <g:checkBox name="showOnlyWhenLoggedIn" />
    </div>
</div>
<g:hiddenField name="id" />