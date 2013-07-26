$(document).ready(function() {
    $("#menuItemList").sortable({
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
    $('#createMenuItemModal').modal('show');
});

$('a#create').click(function(){
    
	var title = $('input#titleId').val(); 
   var role = $('select#roleId').val();
   var url = $('input#urlId').val();
   var showOnlyWhenLoggedIn = $('input#showOnlyWhenLoggedInId').val();
   $('ul#menuItemList').prepend("<li class=\"thumbnail temporaryMenuItem\" data-title=\""+title+ "\""+
                                     " data-role=\"" + role + "\"" +
   									 " data-url=\""+ url + "\"" +
   									 " data-showOnlyWhenLoggedIn=\"" + showOnlyWhenLoggedIn + "\">" + 
		                        "<p><strong>" + title + "</strong>" + 
		                        "<a id=\"save-button\" role=\"button\" href=\"#\" class=\"btn\" onClick=\"this.style.visibility=\"hidden\"\">Save</a></p></li>");
    console.log(title)
    
});

$(document).delegate("a#save-button", "click", function(){ 
	$('#alertMessageLink').show();
	$('#save-button').hide();
	var $menuItem = $('li.temporaryMenuItem');
	var title = $menuItem.data('title');
	var role = $menuItem.data('role');
	var url = $menuItem.data('url');
	var showOnlyWhenLoggedIn = $menuItem.data('showOnlyWhenLoggedIn');
	console.log($menuItem)
	console.log(title)

	$.ajax({
        type: 'POST',
        url: '/menuItem/saveMenu',
        data: {'title':title,'role':role,'url':url,'showOnlyWhenLoggedIn':showOnlyWhenLoggedIn},
        success: function(result) {
            
        }
    });
});
