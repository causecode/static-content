<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'menuItem.label', default: 'EditOrder')}" />
    <title><g:message code="default.editOrder.label" args="[entityName]" /></title>
    <r:require modules="draggableAndSortable"/>
    <r:require modules="menuItem"/>
</head>
<body>
    <div id="menu-item-sorting">
        <a href="#" role="button" class="" id="create-menu-item">Create Menu Item</a>
        <ul class="sortable thumbnails" data-parent-id="" data-menu-id="${menuInstance?.id }">
            <g:each in="${menuItemInstanceList}" var="menuItemInstance">
                <g:if test="${!menuItemInstance?.parent }">
                    <li id="${menuItemInstance?.id }" class="thumbnail" data-menu-item-id="${menuItemInstance?.id }">
                        <p>
                            <strong>${menuItemInstance?.title}</strong>
                        </p>
                        <com:bootstrapMediaMenu id="${menuItemInstance?.id}"></com:bootstrapMediaMenu>
                    </li>
                </g:if>
            </g:each>
        </ul>
        <div id="sa" style="visibility: hidden">
            <li id="new"><p><strong></strong></p></li>
        </div>
    </div>
    
    <div id="myModal" class="modal hide fade" data-captcha="true" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">Modal header</h3>
      </div>
      <div class="modal-body">
        <p>One fine body…</p>
      </div>
      <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
        <button class="btn btn-primary">Create</button>
      </div>
    </div><%--
    
    <div>
        <ul id="sortable" class="ui-sortable media-list" data-parent-menu-item-id="null" data-menu-id="${menuInstance?.id }">
            <g:each in="${menuItemInstanceList}" var="menuItemInstance">
                <g:if test="${!menuItemInstance?.parent }">
                    <li id="${menuItemInstance?.id }" class=" media " data-item-id="${menuItemInstance?.id }">
                        <div id="${menuItemInstance?.id }" class="draggable ui-widget-content media-body thumbnail">
                             <p class="ex1 "> ${menuItemInstance?.title}</p>
                             <com:bootstrapMediaMenu id="${menuItemInstance?.id}"></com:bootstrapMediaMenu>
                        </div>
                    </li>
                </g:if>
            </g:each>
        </ul>
    </div>
--%></body>
</html>