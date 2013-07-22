<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'menuItem.label', default: 'EditOrder')}" />
    <title><g:message code="default.editOrder.label" args="[entityName]" /></title>
    <r:require modules="draggableAndSortable"/>
    <style>
        p.ex1 {margin-left:40px;}
          #containment-wrapper { width: 95%; height:450px; border:2px solid #ccc; padding: 10px; }
    </style>
</head>
<body>
    <script>
        $(function() {
            $("#sortableChild,#sortableSubChild").sortable({
                revert:true,
                update: function( event, ui ) {
                    var $sortedMenuItem = $(ui.item[0])
                    elementId = $sortedMenuItem.data('item-id');
                    var index = $sortedMenuItem.index();
                    getMenuItemIndex(elementId , index )
                }
            });
            $( "ul, li" ).disableSelection();
        });
        
        function getMenuItemIndex(elementId,index) {
            var menuItemInstance = MenuItem().get(elementId)
            console.log(menuItemInstance)
            $.ajax({
                type: 'POST',
                url: '/menuItem/editOrder',
                data: JSON.stringify({ index: index }),
                success: function(result) {
                    //
                }
            });
        }
    </script>
        <ul id="sortableChild" class="ui-sortable media-list" >
            <g:each in="${menuItemInstanceList}" var="menuItemInstance">
                <g:if test="${!menuItemInstance?.parent }">
                    <li class="ui-state-default media " data-item-id="${menuItemInstance?.id }">
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