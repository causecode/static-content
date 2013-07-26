$(document).ready(function() {
    $(".sortable").sortable({
        revert:true,
        update: function( event, ui ) {
            var $sortedMenuItem = $(ui.item[0])
            
            var $sortedParentMenuItem = $sortedMenuItem.parent()
            console.log($sortedParentMenuItem)
            parentMenuItemId = $sortedParentMenuItem.data('parent-id');
            menuItemId = $sortedMenuItem.data('menu-item-id');
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

$('a#create-menu-item').click(function() {
    jQuery('#myModal').modal('show');
})

