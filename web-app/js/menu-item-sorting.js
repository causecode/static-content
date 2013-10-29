/**
 * JS for calling function which contain Ajax call for sorting Menu Items.
 */

var $createMenuItemOverlay = $("div#create-menu-item-overlay");
var $editMenuItemOverlay = $("div#edit-menu-item-overlay");
var $alert = $("div#menu-item-alert");

$("ul.menu-item-container").sortable({
    revert: true,
    connectWith: "ul.menu-item-container",
    activate: function(en, ui) {
        $(this).css('min-height', '13px');
    },
    deactivate: function(en, ui) {
        $(this).css('min-height', '0px');
    },
    update: function( event, ui ) {
        if(this != ui.item.parent()[0]) {
            //@see http://forum.jquery.com/topic/sortables-update-callback-and-connectwith
            return; // Needs to prevent duplicate call due to connectWith.
        }
        var $sortedMenuItem = $(ui.item[0]);
        var index = $sortedMenuItem.index();
        var $sortedParentMenuItem = $sortedMenuItem.parent();
        var menuItemId = $sortedMenuItem.data('menu-item-id');
        var parentId = $sortedParentMenuItem.data('parent-id');

        if($sortedMenuItem.hasClass('un-saved')) {
            return;
        }

        $sortedMenuItem.toggleClass("thumbnail", parentId == undefined);

        $.ajax({
            type: 'POST',
            url: '/menuItem/reorder',
            data: {id: menuItemId, parentId: parentId, index: index},
            success: function(result) {
            }
        });
    }
});

$( "ul, li" ).disableSelection();

$("a#create", $createMenuItemOverlay).click(function() {
    var $this = $(this);
    var $form = $createMenuItemOverlay.find("form");
    if(!$form.valid()) {
        return false;
    }
    var title = $('input#title', $createMenuItemOverlay).val();
    var data = $form.serialize();
    var $newMenuItem
        = $("<li class=\"thumbnail menu-item un-saved\">" +
                "<i class=\"icon-move\"></i>" +
                "<strong>" + title + "</strong>" +
                "<span class=\"pull-right\">" +
                    "<a href=\"#\" class=\"save-menu-item\"><i class=\"icon-ok\"></i></a>" +
                    "<a href=\"#\" class=\"edit-menu-item hid\"><i class=\"icon-pencil\"></i></a>" +
                    "<a href=\"#\" class=\"delete-menu-item\"><i class=\"icon-remove\"></i></a>" +
                "</span>" +
                "<ul class=\"menu-item-container\" data-parent-id=\"\">" +
             "</li>").data("formData", data);
    
    $('ul.menu-item-container.top-level-container').prepend($newMenuItem);
});

$(document).on("click", "li.menu-item.un-saved a.save-menu-item", function() { 
    $alert.text('MenuItem Created Successfully!').show();

    var $this = $(this);
    var $menuItem = $this.parents('li.un-saved');
    var $parentMenuItem = $menuItem.parent();
    var parentId = $parentMenuItem.data('parent-id');
    var index = $menuItem.index();

    $.ajax({
        type: 'POST',
        url: '/menuItem/save',
        data: $menuItem.data('formData') + "&index=" + index + "&menuId=" + $("input#id").val() + "&parentId=" + parentId,
        success: function(response) {
            $menuItem.data('menu-item-id', response).removeClass('un-saved');
            $this.remove();
        }
    });
});

/**
 * JS for editing Menu Item.
 */
$(document).on("click", "a.edit-menu-item", function() {
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
$("a#update", $editMenuItemOverlay).click(function() {
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
$(document).on("click", "a.delete-menu-item", function() {
    var menuItem = $(this).parents("li.menu-item").get(0);
    var $menuItem = $(menuItem);
    var confirmDelete = confirm("Are you sure want to delete MenuItem [" + $menuItem.find('> .title').text() + "]");
    if(!confirmDelete) return false;
    
    $.ajax({
        type: 'POST',
        url: '/menuItem/delete',
        data: {
            id: $menuItem.data("menu-item-id")
        },
        success: function(response) {
            $menuItem.fadeOut().remove();
        },
        error: function() {
            alert("Sorry something went wrong, Please try refreshing the page.")
        }
    });
});