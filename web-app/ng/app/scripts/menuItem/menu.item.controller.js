/* global controllers*/

'use strict';

controllers.controller('MenuItemController', ['$scope', '$state', 'MenuItemModel', 'appService', '$modal', 'MenuModel',
                                              function($scope, $state, MenuItemModel, appService, $modal, MenuModel) {

	if ($scope.actionName === 'create') {
		$scope.menuItemInstance = new MenuItemModel();

	} else if (['edit', 'show'].indexOf($scope.actionName) > -1) {
		MenuModel.get({id: $scope.id}, function(data) {
			$scope.menuInstance = data.menuInstance;
//			$scope.menuItemInstanceList = data.menuItemInstanceList;
			$scope.createMenuItemInstanceList(data);
			$scope.roleList = data.roleList;
			console.info('>>>>>>>>>and list is', data.menuItemInstanceList);
		});
	}
	
	$scope.createMenuItemInstanceList = function(data) {
		angular.forEach(data.menuItemInstanceList, function(menuItemInstance) {
			$scope.menuItemInstanceList.push(new MenuItemModel(menuItemInstance));
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
		console.info('in createMenuItem',$scope.menuItemInstanceList);
		$scope.menuItemInstance.childItems = [];
		$scope.menuItemInstance.tempId = ($scope.tempId + 1);
		$scope.menuItemInstanceList.unshift($scope.menuItemInstance);
		$scope.tempId = $scope.tempId + 1;
		$scope.modalInstance.close();
		$scope.buttonDisabled = true;
	}
	
	$scope.editMenuItem = function(menuItemInstance) {
		$scope.menuItemInstance = menuItemInstance;
		$scope.menuItemInstance.role = $scope.menuItemInstance.roles; 
		$scope.modalInstance = $modal.open({
			templateUrl: 'views/menuItem/editMenuItemOverlay.html',
			scope: $scope
		});
	}
	
	$scope.removeMenuItem = function(menuItemInstance, parentMenuItem) {
		console.info('in remove');
		// delete from database
		if (menuItemInstance.id != null) {
			menuItemInstance.$delete(null, function() {
				
			});
		}
		if (!parentMenuItem) {
			var index = $scope.getIndexOfMenuItem(menuItemInstance.tempId);
			console.info('index is',index);
			$scope.menuItemInstanceList.slice(index, 1);
		}
		else {
			var parentIndex = $scope.getIndexOfMenuItem(parentMenuItem.tempId);
			var childIndex = $scope.getIndexOfchildItem($scope.menuItemInstanceList[parentIndex], menuItemInstance.tempId);
			$scope.menuItemInstanceList[parentIndex].childItems.slice(childIndex, 1)
		}
	}
	
	$scope.getIndexOfchildItem = function (menuItemInstance, childTempId) {
		for (var i=0; i < menuItemInstance.childItems.length; i ++) {
			if (menuItemInstance.childItems[i].tempId == childTempId) {
				return i;	
			}
		}
	}
	
	$scope.getIndexOfMenuItem = function(tempId) {
		for(var i=0; i < $scope.menuItemInstanceList.length; i ++) {
			if($scope.menuItemInstanceList[i].tempId == tempId) {
				return i;	
			}
		}
	}
	
	$scope.saveMenuItem = function(menuItemInstance,parent) {
		if (parent) {
			menuItemInstance.parentId = parent.id;
		} else {
			menuItemInstance.index = $scope.getIndexOfMenuItem(menuItemInstance.tempId);
		}
		menuItemInstance.menuId = $scope.menuInstance.id;
		menuItemInstance.$save(null, function(data) {
			console.info('save successfull');
			menuItemInstance.id = data.id;
			$scope.buttonDisabled = false;
			appService.showAlertMessage('MenuItem Created.', 'info'); 
		}, function(resp) {
				appService.showAlertMessage(resp.data.message, 'danger');
		}); 
	}
	
	$scope.updateMenuItem = function() {
		console.info('in update ', $scope.menuItemInstance);
		$scope.menuItemInstance.$update (null, function() {
			appService.showAlertMessage('MenuItem Updated.', 'info'); 
		}, function(resp) {
				appService.showAlertMessage(resp.data.message, 'danger');
		});	
		$scope.modalInstance.close();
	}
	
	$scope.reorder = function(menuItemInstance) {
		console.info('In updat on order change.');
		MenuItemModel.reorder(null, menuItemInstance, function() {
		}, function(resp) {
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

	$scope.sortableOptions = {
			//containment: '#sortable-container',
			//restrict move across columns. move only within column.
			accept: function (sourceItemHandleScope, destSortableScope) {
				return true
			},
			itemMoved: function (event) {
				console.info('item moved', event);
//				var index = event.dest.index;
//				event.dest.sortableScope.modelValue[index].index = index;
				$scope.onMenuItemMoved(event);
			},
			orderChanged: function (event) {
				console.info('order changed', event);
				$scope.onMenuItemOrderChanged(event);
			}
	};
	
}]);