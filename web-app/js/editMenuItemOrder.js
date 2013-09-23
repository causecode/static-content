/**
 * JS for calling function which contain Ajax call for sorting Menu Items.
 */

var $createMenuItemOverlay = $("#create-menu-item-overlay");
var $editMenuItemOverlay;

$("ul#menuItemList").sortable({
    revert:true,
    update: function( event, ui ) {
        var $sortedMenuItem = $(ui.item[0]);
        if($sortedMenuItem.hasClass('temporaryMenuItem')) {
            return;
        }
        var $sortedParentMenuItem = $sortedMenuItem.parent()
        var parentId = $sortedParentMenuItem.data('parent-id');
        var menuItemId = $sortedMenuItem.data('menu-item-id');
        var menuId = $sortedParentMenuItem.data('menu-id')
        var index = $sortedMenuItem.index();
        $.ajax({
            type: 'POST',
            url: '/menuItem/reorder',
            data: {menuId: menuId, menuItemId: menuItemId, parentId: parentId, index: index},
            success: function(result) {
            }
        });
    }
});

$( "ul, li" ).disableSelection();

$("a#create", $createMenuItemOverlay).click(function(){
    var title = $('input#title').val(); 
    var roles = $('select#roles').val();
    var url = $('input#url').val();
    var showOnlyWhenLoggedIn = $('input#showOnlyWhenLoggedIn').val();
    $('ul.menuItem').prepend("<li id=\"\"class=\"thumbnail clearfix temporaryMenuItem\" data-title=\""+title+ "\""+
                             " data-roles=\"" + roles + "\"" +
                             " data-url=\""+ url + "\"" +
                             " data-show-only-when-logged-in=\"" + showOnlyWhenLoggedIn + "\">" +
                             "<i class=\"icon-move\"></i>"+
                             "<strong>" + title + "</strong>" + 
                             "<a id=\"save-button\" role=\"button\" href=\"#\" class=\"btn btn-default btn-xs\">Save</a></li>");
});

$(document).on("click", "a#save-button", function(){ 
    $('#alertMessageLink').text('MenuItem Created Successfully!').show();

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
        url: '/menuItem/save',
        data: {'title':title,'roles':roles,'url':url,'showOnlyWhenLoggedIn':showOnlyWhenLoggedIn,
               'parentId':parentId,'menuId':menuId,'index':index},
        success: function(response) {
            var menuItemId = response ;
            $menuItem.attr('id',menuItemId);
            $menuItem.data('menu-item-id',menuItemId);
            $menuItem.removeAttr('data-title');
            $menuItem.removeAttr('data-url');
            $menuItem.removeAttr('data-roles');
            $menuItem.removeAttr('data-show-only-when-logged-in');
            $menuItem.append('<a  id="editMenuItem" href="#" class="pull-right"><i class="icon-pencil"></i></a>');
        }
    });
});

/**
 * JS for editing Menu Item.
 */
$(document).on("click", "a#editMenuItem", function(){
    var $menuItem = $(this).parents('li');
    menuItemId = $menuItem.data('menu-item-id')
    $.ajax({
        type: 'POST',
        url: '/menuItem/edit',
        data: {id: menuItemId},
        success: function(response) {
            var itemInstance = response
            $menuItem.data('item-instance',itemInstance)
            $('#editMenuItemModal #title').val(itemInstance.title)
            $('#editMenuItemModal #url').val(itemInstance.url)
            $('#editMenuItemModal #roles').val(itemInstance.roles)
            $('#editMenuItemModal #showOnlyWhenLoggedIn').val(itemInstance.showOnlyWhenLoggedIn)
            $('#editMenuItemModal').modal('show');
            $('#updateMenuItem').data('menu-item-id',menuItemId);
        }
    });
    
});

/**
 * JS for updating Menu Item.
 */
$(document).on("click", "a#updateMenuItem", function(){
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
        url: '/menuItem/update',
        data: {'title':title,'roles':roles,'url':url,'showOnlyWhenLoggedIn':showOnlyWhenLoggedIn, id: menuItemId},
        success: function(response) {
        }
    });
});

/**
 * JS for delete Menu Item.
 */
$('a#deleteMenuItem').click(function(){
    menuItemId = $('#updateMenuItem').data('menu-item-id');
    $.ajax({
        type: 'POST',
        url: '/menuItem/delete',
        data: {'menuItemId':menuItemId},
        success: function(response) {
            $('li#'+menuItemId).remove();
        }
    });
});