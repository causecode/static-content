$(document).ready(function() {
    $("#menuItemList").sortable({
        revert:true,
        update: function( event, ui ) {
            var $sortedMenuItem = $(ui.item[0])
            console.log($sortedMenuItem)
            if($sortedMenuItem.hasClass('temporaryMenuItem')) {
            	console.log("inside if")
            	$('#alertMessageLink').text("Save newly created menu item.");
                $('#alertMessageLink').show();
            } else {
            	console.log("inside else")
	            var $sortedParentMenuItem = $sortedMenuItem.parent()
	            console.log($sortedParentMenuItem)
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
   var role = $('select#roleId').val();
   var url = $('input#urlId').val();
   var showOnlyWhenLoggedIn = $('input#showOnlyWhenLoggedInId').val();
   $('ul.menuItem').prepend("<li class=\"thumbnail temporaryMenuItem\" data-title=\""+title+ "\""+
                                     " data-role=\"" + role + "\"" +
   									 " data-url=\""+ url + "\"" +
   									 " data-show-only-when-logged-in=\"" + showOnlyWhenLoggedIn + "\">" +
		                        "<p><strong>" + title + "</strong>" + 
		                        "<a id=\"save-button\" role=\"button\" href=\"#\" class=\"btn\">Save</a></p></li>");
    console.log(title)
});

$(document).on("click", "a#save-button", function(){ 
	$('#alertMessageLink').text('MenuItem Created Successfully!');
	$('#alertMessageLink').show();
	$('#save-button').remove();
	
	var $menuItem = $('li.temporaryMenuItem');
	var title = $menuItem.data('title');
	var role = $menuItem.data('role');
	var url = $menuItem.data('url');
	var showOnlyWhenLoggedIn = $menuItem.data('show-only-when-logged-in');
	
	var $parentMenuItem = $menuItem.parent()
	parentId = $parentMenuItem.data('parent-id');
    menuId = $parentMenuItem.data('menu-id')
    var index = $menuItem.index();
	
    console.log($menuItem)
	console.log("title" + title,"role"+role,url,showOnlyWhenLoggedIn,
			"parentId"+parentId,"menuId"+menuId,"index"+index)

	$('li.temporaryMenuItem').removeClass('temporaryMenuItem');
	$.ajax({
        type: 'POST',
        url: '/menuItem/saveMenu',
        data: {'title':title,'role':role,'url':url,'showOnlyWhenLoggedIn':showOnlyWhenLoggedIn,
        	   'parentId':parentId,'menuId':menuId,'index':index},
        success: function(result) {
            
        }
    });
});
