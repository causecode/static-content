/*
 * JS for calling function which contain Ajax call for sorting Menu Items.
 */
$(document).ready(function() {
    $("#menuItemList").sortable({
        revert:true,
        update: function( event, ui ) {
            var $sortedMenuItem = $(ui.item[0])
            if(!$sortedMenuItem.hasClass('temporaryMenuItem')) {
                var $sortedParentMenuItem = $sortedMenuItem.parent()
                var parentId = $sortedParentMenuItem.data('parent-id');
                var menuItemId = $sortedMenuItem.data('menu-item-id');
                var menuId = $sortedParentMenuItem.data('menu-id')
                var index = $sortedMenuItem.index();
                getMenuItemIndex(menuId,menuItemId,parentId,index)
                console.log(menuId,menuItemId,parentId,index)
            }
        }
    });
    $( "ul, li" ).disableSelection();
});
/*
 * Function used to make Ajax call which sorts Menu Items. 
 */
function getMenuItemIndex(menuId,menuItemId,parentId,index) {
    $.ajax({
        type: 'POST',
        url: '/menu/show',
        data: {'menuId':menuId,'menuItemId':menuItemId,'parentId':parentId,'index':index},
        success: function(result) {
        }
    });
}
/*
 * JS for Creating Menu Item.
 */
$('a#create-menu-item').click(function() {
    $('#createMenuItemModal').modal('show');
});

$('a#create').click(function(){
    var title = $('input#title').val(); 
    var roles = $('select#roles').val();
    var url = $('input#url').val();
    var showOnlyWhenLoggedIn = $('input#showOnlyWhenLoggedIn').val();
    $('ul.menuItem').prepend("<li id=\"\"class=\"thumbnail temporaryMenuItem\" data-title=\""+title+ "\""+
                             " data-roles=\"" + roles + "\"" +
                             " data-url=\""+ url + "\"" +
                             " data-show-only-when-logged-in=\"" + showOnlyWhenLoggedIn + "\">" +
                             "<p><strong>" + title + "</strong>" + 
                             "<a id=\"save-button\" role=\"button\" href=\"#\" class=\"btn\">Save</a></p></li>");
});

$(document).on("click", "a#save-button", function(){ 
    $('#alertMessageLink').text('MenuItem Created Successfully!');
    $('#alertMessageLink').show();

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
    console.log('title'+title,'roles'+roles,'url'+url,'showOnlyWhenLoggedIn'+showOnlyWhenLoggedIn,
               'parentId'+parentId,'menuId'+menuId,'index'+index);
    $.ajax({
        type: 'POST',
        url: '/menuItem/saveMenuItem',
        data: {'title':title,'roles':roles,'url':url,'showOnlyWhenLoggedIn':showOnlyWhenLoggedIn,
               'parentId':parentId,'menuId':menuId,'index':index},
        success: function(response) {
                if(response) {
                    var menuItemId = response ;
                    $menuItem.attr('id',menuItemId);
                    $menuItem.data('menu-item-id',menuItemId);
                    $menuItem.removeAttr('data-title');
                    $menuItem.removeAttr('data-url');
                    $menuItem.removeAttr('data-roles');
                    $menuItem.removeAttr('data-show-only-when-logged-in');
                }
        }
    });
});
/*
 * JS for editing Menu Item.
 */
$('a#editMenuItem').click(function(){
    var $menuItem = $(this).parents('li');
    menuItemId = $menuItem.data('menu-item-id')
    $.ajax({
        type: 'POST',
        url: '/menuItem/editMenuItem',
        data: {'menuItemId':menuItemId},
        success: function(response) {
                if(response) {
                    var itemInstance = response
                    console.log($menuItem)
                    $menuItem.data('item-instance',itemInstance)
                    console.log($menuItem.data('item-instance'))
                    $('#editMenuItemModal #title').val(itemInstance.title)
                    $('#editMenuItemModal #url').val(itemInstance.url)
                    $('#editMenuItemModal #roles').val(itemInstance.roles)
                    $('#editMenuItemModal #showOnlyWhenLoggedIn').val(itemInstance.showOnlyWhenLoggedIn)
                    $('#editMenuItemModal').modal('show');
                    $('#updateMenuItem').data('menu-item-id',menuItemId);
                }
        }
    });
    
});
/*
 * JS for updating Menu Item.
 */
$('a#updateMenuItem').click(function(){
    menuItemId = $('#updateMenuItem').data('menu-item-id');
    var $menuItem = $('li#'+menuItemId);
    var itemInstance = $menuItem.data('item-instance');
    if(itemInstance.title != $('#editMenuItemModal #title').val()){
        var $item = $menuItem.find('strong');
        $item.text($('#editMenuItemModal #title').val());
    }
    var title = $('#editMenuItemModal #title').val();
    var rolesArray = $('#editMenuItemModal #roles').val();
    var url = $('#editMenuItemModal #url').val();
    var showOnlyWhenLoggedIn = $('#editMenuItemModal #showOnlyWhenLoggedIn').val();
    var roles = rolesArray + "";
    $.ajax({
        type: 'POST',
        url: '/menuItem/updateMenuItem',
        data: {'title':title,'roles':roles,'url':url,'showOnlyWhenLoggedIn':showOnlyWhenLoggedIn,'menuItemId':menuItemId},
        success: function(response) {
        }
    });
});

/*
 * JS for delete Menu Item.
 */
$('a#deleteMenuItem').click(function(){
    menuItemId = $('#updateMenuItem').data('menu-item-id');
    console.log(menuItemId)
    $.ajax({
        type: 'POST',
        url: '/menuItem/delete',
        data: {'menuItemId':menuItemId},
        success: function(response) {
            $('li#'+menuItemId).remove()
        }
    });
});