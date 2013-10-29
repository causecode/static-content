/**
 * JS for calling function which contain Ajax call for sorting Menu Items.
 */

var $createMenuItemOverlay = $("div#create-menu-item-overlay");
var $editMenuItemOverlay = $("div#edit-menu-item-overlay");
var $alert = $("div#menu-item-alert");

$("ul.menu-item-container").sortable({
    revert: true,
    opacity: 0.8,
    tolerance: "pointer",
    connectWith: "ul.menu-item-container",
    cancel: ".edit-menu-item, .delete-menu-item, .save-menu-item",
    activate: function(en, ui) {
        $(this).animate({'min-height': '13px'});
    },
    deactivate: function(en, ui) {
        $(this).animate({'min-height': '0px'});
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

        $sortedMenuItem.toggleClass("thumbnail", (parentId == undefined || parentId == ""));
        if($sortedMenuItem.hasClass('un-saved')) {
            return;
        }

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
                "<i class=\"icon-move\"></i> " +
                "<strong>" + title + "</strong>" +
                "<span class=\"pull-right\">" +
                    "<a href=\"#\" class=\"save-menu-item\"><i class=\"icon-ok\"></i></a> " +
                    "<a href=\"#\" class=\"edit-menu-item hid\"><i class=\"icon-pencil\"></i></a> " +
                    "<a href=\"#\" class=\"delete-menu-item\"><i class=\"icon-remove\"></i></a>" +
                "</span>" +
                "<ul class=\"menu-item-container\" data-parent-id=\"\">" +
             "</li>").data("formData", data);
    
    $('ul.menu-item-container.top-level-container').prepend($newMenuItem);
});

$(document).on("click", "li.menu-item.un-saved a.save-menu-item", function() { 
    var $this = $(this);
    var $menuItem = $this.parents('li.un-saved');
    var $parentMenuItem = $menuItem.parent();
    var parentId = $parentMenuItem.data('parent-id');
    var index = $menuItem.index();

    $.ajax({
        type: 'POST',
        url: '/menuItem/save',
        data: $menuItem.data('formData') + "&index=" + index + "&menuId=" + $("input#menuId").val() + "&parentId=" + parentId,
        success: function(response) {
            showAlertMessage("MenuItem created successfully.", "info", {element: $alert});
            $menuItem.find("i.icon-remove").addClass("icon-trash").remove("icon-remove");
            $menuItem.data('menu-item-id', response).removeClass('un-saved');
            $menuItem.find(".edit-menu-item").show();
            $this.remove();
        },
        error: function() {
            showAlertMessage("Something went wrong while saving menu item. Please try refreshing the page.", "danger", {element: $alert});
        }
    });
    return false;
});

/**
 * JS for editing Menu Item.
 */
$(document).on("click", "a.edit-menu-item", function() {
    var $menuItem = $(this).parents('li.menu-item');
    var menuItemId = $menuItem.data('menu-item-id');
    $.ajax({
        url: '/menuItem/edit',
        data: {id: menuItemId},
        success: function(response) {
            var itemInstance = response
            $menuItem.data('item-instance',itemInstance);
            $('#title', $editMenuItemOverlay).val(itemInstance.title);
            $('#url', $editMenuItemOverlay).val(itemInstance.url);
            if(itemInstance.roles) {
                $('#roles', $editMenuItemOverlay).val(itemInstance.roles.split(','));
            }
            $('#id', $editMenuItemOverlay).val(menuItemId);
            var checked = itemInstance.showOnlyWhenLoggedIn;

            $('#showOnlyWhenLoggedIn', $editMenuItemOverlay).prop("checked", checked).attr("checked", checked);
            $editMenuItemOverlay.modal('show');
        },
        error: function() {
            showAlertMessage("Something went wrong while reading content from server.", "danger", {element: $alert});
        }
    });
    return false;
});

/**
 * JS for updating Menu Item.
 */
$("a#update", $editMenuItemOverlay).click(function() {
    var $this = $(this);
    var menuItemId = $('#id', $editMenuItemOverlay).val();
    var $menuItem = $('li#menu-item-' + menuItemId);
    var newTitle = $('#title', $editMenuItemOverlay).val();

    $menuItem.find(".title").text(newTitle);

    $.ajax({
        type: 'POST',
        url: '/menuItem/update',
        data: $this.parents("form").serialize(),
        success: function() {
            showAlertMessage("MenuItem updated successfully.", "info", {element: $alert});
        },
        error: function() {
            showAlertMessage("Sorry something went wrong, Please try refreshing the page.", "danger", {element: $alert});
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

    if($menuItem.hasClass('un-saved')) {
        $menuItem.fadeOut().remove();
        return false;
    }

    $.ajax({
        type: 'POST',
        url: '/menuItem/delete',
        data: {
            id: $menuItem.data("menu-item-id")
        },
        success: function(response) {
            $menuItem.fadeOut().remove();
            showAlertMessage("MenuItem deleted successfully.", "info", {element: $alert});
        },
        error: function() {
            showAlertMessage("Sorry something went wrong, Please try refreshing the page.", "danger", {element: $alert});
        }
    });
    return false;
});