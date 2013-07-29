$(document).ready(function() {
    $("#menuItemList").sortable({
        revert:true,
        update: function( event, ui ) {
            var $sortedMenuItem = $(ui.item[0])
            if($sortedMenuItem.hasClass('temporaryMenuItem')) {
            	$('#alertMessageLink').text("Save newly created menu item.");
                $('#alertMessageLink').show();
            } else {
                var $sortedParentMenuItem = $sortedMenuItem.parent()
                parentId = $sortedParentMenuItem.data('parent-id');
                menuItemId = $sortedMenuItem.data('menu-item-id');
                menuId = $sortedParentMenuItem.data('menu-id')
                var index = $sortedMenuItem.index();
                getMenuItemIndex(menuId,menuItemId,parentId,index)
                console.log(menuItemId,index,parentId, menuId)
            }
        }
    });
    $( "ul, li" ).disableSelection();
});

function getMenuItemIndex(menuId,menuItemId,parentId,index) {
    $.ajax({
        type: 'POST',
        url: '/menuItem/editOrder',
        data: {'menuId':menuId,'menuItemId':menuItemId,'parentId':parentId,'index':index},
        success: function(result) {
        }
    });
}

$('a#create-menu-item').click(function() {
    $('#createMenuItemModal').modal('show');
});

$('a#create').click(function(){
    var title = $('input#titleId').val(); 
    var roles = $('select#roleId').val();
    var url = $('input#urlId').val();
    var showOnlyWhenLoggedIn = $('input#showOnlyWhenLoggedInId').val();
    $('ul.menuItem').prepend("<li class=\"thumbnail temporaryMenuItem\" data-title=\""+title+ "\""+
                             " data-roles=\"" + roles + "\"" +
                             " data-url=\""+ url + "\"" +
                             " data-show-only-when-logged-in=\"" + showOnlyWhenLoggedIn + "\">" +
                             "<p><strong>" + title + "</strong>" + 
                             "<a id=\"save-button\" role=\"button\" href=\"#\" class=\"btn\">Save</a></p></li>");
});

$(document).on("click", "a#save-button", function(){ 
    $('#alertMessageLink').text('MenuItem Created Successfully!');
    $('#alertMessageLink').show();

    console.log($(this))
    var $menuItem = $(this).parents('li.temporaryMenuItem');
    var title = $menuItem.data('title');
    var roles = $menuItem.data('roles');
    var url = $menuItem.data('url');
    var showOnlyWhenLoggedIn = $menuItem.data('show-only-when-logged-in');

    var $parentMenuItem = $menuItem.parent()
    var parentId = $parentMenuItem.data('parent-id');
    var menuId = $parentMenuItem.data('menu-id')
    var index = $menuItem.index();
    $('#save-button').remove();
    $('li.temporaryMenuItem').removeClass('temporaryMenuItem');
    $.ajax({
        type: 'POST',
        url: '/menuItem/saveMenu',
        data: {'title':title,'roles':roles,'url':url,'showOnlyWhenLoggedIn':showOnlyWhenLoggedIn,
               'parentId':parentId,'menuId':menuId,'index':index},
        success: function(result) {

        }
    });
});
