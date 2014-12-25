/* global controllers*/

'use strict';

controllers.controller('MenuItemController', ['$scope', '$state', 'MenuItemModel', 'appService', '$modal', 'MenuModel',
                                              function($scope, $state, MenuItemModel, appService, $modal, MenuModel) {

	if ($scope.actionName === 'create') {
		$scope.menuItemInstance = new MenuItemModel();

	} else if (['edit', 'show'].indexOf($scope.actionName) > -1) {
		MenuModel.get({id: $scope.id}, function(data) {
			$scope.menuInstance = data.menuInstance;
			$scope.createMenuItemInstanceList(data);
			$scope.roleList = data.roleList;
		});
	}
	
	$scope.createMenuItemInstanceList = function(data) {
		angular.forEach(data.menuItemInstanceList, function(menuItemInstance) {
			var menuItemModelInsance = new MenuItemModel(menuItemInstance);
			if (menuItemModelInsance.childItems.length > 0) {
				$scope.createMenuItemModelInstance(menuItemModelInsance);
			}
			$scope.menuItemInstanceList.push(menuItemModelInsance);
		});
	}
	
//	Applicable for two levels only. Use recursion if its to be implemented for
//	multiple (i.e. more than two layers) layers of Menu Item.
	$scope.createMenuItemModelInstance = function(menuItemModelInsance) {
		angular.forEach(menuItemModelInsance.childItems, function(childMenuItem, index) {
			menuItemModelInsance.childItems[index] = new MenuItemModel(childMenuItem);	
		});
	}

	$scope.open = function () {
		$scope.menuItemInstance = new MenuItemModel();
		$scope.modalInstance = $modal.open({
			templateUrl: 'views/menuItem/createMenuItemOverlay.html',
			scope: $scope
		});
	};
	
	$scope.buttonDisabled = false;
	$scope.menuItemInstanceList = [];
	$scope.tempId = 0;
	$scope.createMenuItem = function() {
		$scope.menuItemInstance.childItems = [];
		$scope.menuItemInstance.tempId = ($scope.tempId + 1);
		$scope.menuItemInstanceList.unshift($scope.menuItemInstance);
		$scope.tempId = $scope.tempId + 1;
		$scope.modalInstance.close();
		$scope.disableMenuItemButton();
	}
	
	$scope.edit = function(menuItemInstance) {
		$scope.menuItemInstance = menuItemInstance;
		$scope.modalInstance = $modal.open({
			templateUrl: 'views/menuItem/editMenuItemOverlay.html',
			scope: $scope
		});
	}
	
	
	$scope.remove = function(menuItemInstance, parentMenuItem) {
		if (menuItemInstance.id) {
			menuItemInstance.$delete(null, function() {
			removeFromDOM(menuItemInstance.id, parentMenuItem, 'id');
			});
		} else {
			removeFromDOM(menuItemInstance.tempId, parentMenuItem, 'tempId');
			$scope.enableMenuItemButton();
		}
	}

	function removeFromDOM(value, parentMenuItem, property) {
		if (parentMenuItem) {
			parentMenuItem.childItems.remove(property, value);
		} else {
			$scope.menuItemInstanceList.remove(property, value);
		}
	}
	
	$scope.getIndexOfMenuItem = function(tempId) {
		for(var i=0; i < $scope.menuItemInstanceList.length; i ++) {
			if($scope.menuItemInstanceList[i].tempId == tempId) {
				return i;	
			}
		}
	}
	
	$scope.save = function(menuItemInstance,parent) {
		if (parent) {
			menuItemInstance.parentId = parent.id;
		} else {
			menuItemInstance.index = $scope.getIndexOfMenuItem(menuItemInstance.tempId);
		}
		menuItemInstance.menuId = $scope.menuInstance.id;
		menuItemInstance.$save(null, function(data) {
			menuItemInstance.id = data.id;
			menuItemInstance.tempId = null;
			$scope.enableMenuItemButton();
			appService.showAlertMessage('MenuItem Created.', 'info'); 
		}, function(resp) {
				appService.showAlertMessage(resp.data.message, 'danger');
		}); 
	}
	
	$scope.update = function() {
		$scope.menuItemInstance.$update (null, function() {
			appService.showAlertMessage('MenuItem Updated.', 'info'); 
		}, function(resp) {
				appService.showAlertMessage(resp.data.message, 'danger');
		});	
		$scope.modalInstance.close();
	}
	
	$scope.reorder = function(menuItemInstance) {
		MenuItemModel.reorder(null, menuItemInstance, function() {
		});
	}

	
	$scope.onMenuItemOrderChanged = function(event) {
		var index = event.dest.index;
		var menuItemInstance = event.dest.sortableScope.modelValue[index];
		menuItemInstance.index = index; 
		if (menuItemInstance.id != null) {
			$scope.reorder(menuItemInstance);	
		}
	}
	
	$scope.onMenuItemMoved = function(event) {
		var index = event.dest.index;
		var menuItemInstance = event.dest.sortableScope.modelValue[index];
		menuItemInstance.index = index; 
		var parentMenuItem = event.dest.sortableScope.$parent.menuItemInstance;
		if (parentMenuItem != null) {
			menuItemInstance.parentId = parentMenuItem.id;
		}
		if (menuItemInstance.id != null) {
			$scope.reorder(menuItemInstance);	
		}
	}
	
	$scope.enableMenuItemButton = function() {
		$scope.buttonDisabled = false;
	}

	$scope.disableMenuItemButton = function() {
		$scope.buttonDisabled = true;
	}

	$scope.sortableOptions = {
			//containment: '#sortable-container',
			//restrict move across columns. move only within column.
			accept: function (sourceItemHandleScope, destSortableScope) {
				return true
			},
			itemMoved: function (event) {
				$scope.onMenuItemMoved(event);
			},
			orderChanged: function (event) {
				$scope.onMenuItemOrderChanged(event);
			}
	};
	
}]);