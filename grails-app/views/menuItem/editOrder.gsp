<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'menuItem.label', default: 'EditOrder')}" />
    <title><g:message code="default.editOrder.label" args="[entityName]" /></title>
    <r:require modules="draggableAndSortable"/>
    <r:require modules="menuItem"/>
</head>
<body>
    <a href="#" role="button" class="" id="create-menu-item" style="float:right">Create Menu Item</a>
    <br>
    <div id="menu-item-sorting">
        <div id="alertMessageLink" class="alert alert-success hide"></div>
        <ul id="menuItemList" class="sortable thumbnails menuItem" data-parent-id="" data-menu-id="${menuInstance?.id }">
            <g:each in="${menuItemInstanceList}" var="menuItemInstance">
                <g:if test="${menuItemInstance && !menuItemInstance?.parent }">
                    <li id="${menuItemInstance?.id }" class="thumbnail" data-menu-item-id="${menuItemInstance?.id }">
                        <p>
                            <strong>${menuItemInstance?.title}</strong>
                        </p>
                        <com:bootstrapMediaMenu id="${menuItemInstance?.id}"></com:bootstrapMediaMenu>
                    </li>
                </g:if>
            </g:each>
        </ul>
    </div>
    
    <div id="createMenuItemModal" class="modal hide fade" >
        <div class="modal-header">
            <a href="#" class="close" data-dismiss="modal">x</a>
            <h3>Create Menu Item</h3>
        </div>
        <g:form name="create-menu-item-form" class="form-horizontal block-error jquery-form">
            <fieldset form="create-menu-item-form">
                <div class="modal-body">
                    <div class="alert hide fade in" id="alert-lead">
                        <button type="button" class="close" onclick="$(this).parent().fadeOut()">&times;</button>
                        <p></p>
                    </div>
                    
                    <div class="control-group">
                        <label for="title" class="control-label">
                            <g:message code="person.label.Title" default="Title" />
                        </label>
                        <div class="controls">
                            <g:textField id="titleId" title="Enter menu item title." class="required" name="menuItemTitle"
                                placeholder="Home" autofocus="autofocus" />
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="roles">
                            <g:message code="menuItem.roles.label" default="Role" />
                        </label>
                        <div class="controls">
                            <g:select id="roleId" name="roles" from="${['ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN','ROLE_JOB_BOARD_MANAGER']}" 
                                multiple="true" noSelection="['':'']"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="url">
                            <g:message code="menuItem.url.label" default="Url" />
                        </label>
                        <div class="controls">
                            <g:textField id="urlId" name="url"/>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="showOnlyWhenLoggedIn">
                            <g:message code='menuItem.showOnlyWhenLoggedIn.label' default="Show Only When Logged In" />
                        </label>
                        <div class="controls">
                            <g:checkBox id="showOnlyWhenLoggedInId" name="showOnlyWhenLoggedIn" optionKey="id" />
                        </div>
                    </div>

                </div>
                <div class="modal-footer">
                    <g:submitButton id="createButton"name="create" class="btn btn-primary"
                        value="${message(code: 'default.button.create.label', default: 'Create')}" />
                    <a id="create" href="#" class="btn" data-dismiss="modal">create</a>
                    <a href="#" class="btn" data-dismiss="modal">Cancel</a>
               </div>
            </fieldset>
        </g:form>
    </div>
    
</body>
</html>