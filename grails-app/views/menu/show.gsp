<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'menu.label', default: 'Menu')}" />
    <title><g:message code="default.SortMenuItems.label" args="[entityName]" /></title>
    <r:require modules="draggableAndSortable"/>
    <r:require modules="menuItem"/>
</head>
<body>
    <div class="page-header">
        <h1>Sort Menu Items
            <span class="btn-group pull-right">
                <a href="#" class="btn btn-primary btn-small" id="create-menu-item">Create Menu Item</a>
            </span>
        </h1>
    </div>

    <div id="menu-item-sorting">
        <div id="alertMessageLink" class="alert alert-success hide"></div>
        <ul id="menuItemList" class="sortable thumbnails menuItem" data-parent-id="" data-menu-id="${menuInstance?.id }">
            <g:each in="${menuItemInstanceList}" var="menuItemInstance">
                <g:if test="${menuItemInstance && !menuItemInstance?.parent }">
                    <li id="${menuItemInstance?.id }" class="thumbnail" data-menu-item-id="${menuItemInstance?.id }">
                        <i class="icon-move"></i>
                        <strong>${menuItemInstance?.title}</strong>
                        <a  id="editMenuItem" href="#" class="pull-right"><i class="icon-pencil"></i></a>
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
                    <g:render template="basicForm"></g:render>
                </div>
                <div class="modal-footer">
                    <a id="create" href="#" class="btn btn-primary" data-dismiss="modal">create</a>
                    <a href="#" class="btn" data-dismiss="modal">Cancel</a>
               </div>
            </fieldset>
        </g:form>
    </div>

    <div id="editMenuItemModal" class="modal hide fade" >
        <div class="modal-header">
            <a href="#" class="close" data-dismiss="modal">x</a>
            <h3>Edit Menu Item</h3>
        </div>
        <g:form name="edit-menu-item-form" class="form-horizontal block-error jquery-form">
            <fieldset form="edit-menu-item-form">
                <div class="modal-body">
                    <div class="alert hide fade in" id="alert-lead">
                        <button type="button" class="close" onclick="$(this).parent().fadeOut()">&times;</button>
                        <p></p>
                    </div>
                    <g:render template="basicForm"></g:render>
                </div>
                <div class="modal-footer">
                        <a id="updateMenuItem" class="btn btn-primary" data-dismiss="modal" >Update</a>
                        <a id="deleteMenuItem" class="btn btn-danger" data-dismiss="modal"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" >Delete</a>
               </div>
            </fieldset>
        </g:form>
    </div>
</body>
</html>