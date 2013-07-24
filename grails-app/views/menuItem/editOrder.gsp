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
                    var index = $sortedMenuItem.index();
                    getMenuItemIndex(menuItemId, index, parentMenuItemId )
                    console.log(menuItemId,index,parentMenuItemId)
                }
            });
            $( "ul, li" ).disableSelection();
        });
        
        function getMenuItemIndex(menuItemId, index, parentMenuItemId) {
            $.ajax({
                type: 'POST',
                url: '/menuItem/editOrder',
                data: {'menuItemId':menuItemId,'index':index,'parentMenuItemId':parentMenuItemId},
                success: function(result) {
                    
                }
            });
        }
    </script>
        <ul id="sortable" class="ui-sortable media-list" data-parent-menu-item-id="null">
            <g:each in="${menuItemInstanceList}" var="menuItemInstance">
                <g:if test="${!menuItemInstance?.parent }">
                    <li id="${menuItemInstance?.id }" class="ui-state-default media " data-item-id="${menuItemInstance?.id }">
                        <div id="${menuItemInstance?.id }" class="draggable ui-widget-content media-body thumbnail">
                             <p class="ex1 "> ${menuItemInstance?.title}</p>
                             <com:bootstrapMediaMenu id="${menuItemInstance?.id}"></com:bootstrapMediaMenu>
                        </div>
                    </li>
                </g:if>
            </g:each>
        </ul>
</body>
</html>