<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'menuItem.label', default: 'EditOrder')}" />
    <title><g:message code="default.editOrder.label" args="[entityName]" /></title>
    <r:require modules="draggableAndSortable"/>
    <style>
        p.ex1 {margin-left:40px;}
    </style>
</head>
<body>
    <script>
        $(function() {
            $("#sortable").sortable({
                revert:true,
                update: function( event, ui ) {
                    var $sortedMenuItem = $(ui.item[0])
                    var $sortedParentMenuItem = $sortedMenuItem.parent()
                    parentMenuItemId = $sortedParentMenuItem.data('parent-menu-item-id');
                    menuItemId = $sortedMenuItem.data('item-id');
                    menuId = $sortedParentMenuItem.data('menu-id')
                    var index = $sortedMenuItem.index();
                    getMenuItemIndex(menuItemId, index, parentMenuItemId,menuId )
                    console.log(menuItemId,index,parentMenuItemId, menuId)
                }
            });
            $( "ul, li" ).disableSelection();
        });
        
        function getMenuItemIndex(menuItemId, index, parentMenuItemId, menuId) {
            $.ajax({
                type: 'POST',
                url: '/menuItem/editOrder',
                data: {'menuItemId':menuItemId,'index':index,'parentMenuItemId':parentMenuItemId,'menuId':menuId},
                success: function(result) {
                    
                }
            });
        }
    </script>
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
</body>
</html>