'use strict';

/**
 * @ngdoc controller
 * @name MenuItemController
 * @requires $scope
 * @requires MenuItemModel
 * @requires appService
 * @requires $modal
 * @requires MenuModal
 */
controllers.controller('MenuItemController', ['$scope', 'MenuItemModel', 'appService', '$modal', 'MenuModel',
      function($scope, MenuItemModel, appService, $modal, MenuModel) {

    if ($scope.actionName === 'create') {
        $scope.menuItemInstance = new MenuItemModel();
    } else if (['edit', 'show'].indexOf($scope.actionName) > -1) {
        MenuModel.get({id: $scope.id}, function(data) {
            $scope.menuInstance = data.menuInstance;
            $scope.createMenuItemInstanceList(data);
            $scope.roleList = data.roleList;
        });
    }

    /**
     * @ngdoc method
     * @methodOf MenuItemController
     * @name createMenuItemInstanceList
     * 
     * @param {Object} data Object containing menuItemInstanceList - List of menu item instances
     * 
     * @description 
     * This method creates a MenuItemModel instance for each object in the menuItemInstanceList. 
     * If any menuItem contains child menuItem object then that child menu item 
     * will be converted into MenuItemModel instance as well. 
     */
    $scope.createMenuItemInstanceList = function(data) {
        angular.forEach(data.menuItemInstanceList, function(menuItemInstance) {
            var menuItemModelInsance = new MenuItemModel(menuItemInstance);
            if (menuItemModelInsance.childItems.length > 0) {
                $scope.createMenuItemModelInstance(menuItemModelInsance);
            }
            $scope.menuItemInstanceList.push(menuItemModelInsance);
        });
    };

    /**
     * @ngdoc method 
     * @methodOf MenuItemController
     * @name createMenuItemModelInstance
     * 
     * @param {Object} menuItemModelInsance Instance of MenuItemModel
     * 
     * @description 
     * Creates an instance of MenuItemModel for each child menu item of the menuItemModelInstance.
     * Applicable for two levels only. Use recursion if its to be implemented for
     * multiple (i.e. more than two) layers of Menu Item.
     */
    $scope.createMenuItemModelInstance = function(menuItemModelInsance) {
        angular.forEach(menuItemModelInsance.childItems, function(childMenuItem, index) {
            menuItemModelInsance.childItems[index] = new MenuItemModel(childMenuItem);  
        });
    };

    /**
     * @ngdoc method
     * @methodOf MenuItemController
     * @name open
     * 
     * @description
     * Opens a pop window, that allows user to create new MenuItem instance.
     */
    $scope.open = function () {
        $scope.menuItemInstance = new MenuItemModel();
        $scope.modalInstance = $modal.open({
            templateUrl: 'views/menuItem/create.html',
            scope: $scope
        });
    };

    $scope.buttonDisabled = false;
    $scope.menuItemInstanceList = [];
    $scope.tempId = 0;
    
    /**
     * @ngdoc method
     * @methodOf MenuItemController
     * @name createMenuItem
     * 
     * @description
     * Adds an unique temporary id to newly created to menuItemInstance 
     * and push newly created menuItemInstacnce into menuItemInstanceList
     */
    $scope.createMenuItem = function() {
        $scope.menuItemInstance.childItems = [];
        $scope.menuItemInstance.tempId = ($scope.tempId + 1);
        $scope.menuItemInstanceList.unshift($scope.menuItemInstance);
        $scope.tempId = $scope.tempId + 1;
        $scope.modalInstance.close();
        $scope.disableMenuItemButton();
    };

    /**
     * @ngdoc method 
     * @methodOf MenuItemController
     * @name edit
     * 
     * @param {Object} menuItemInstance Instance of MenuItemModel
     * 
     * @description
     * Opens a pop window, that allows user to update menu item.
     */
    $scope.edit = function(menuItemInstance) {
        $scope.menuItemInstance = menuItemInstance;
        $scope.modalInstance = $modal.open({
            templateUrl: 'views/menuItem/edit.html',
            scope: $scope
        });
    };

    /**
     * @ngdoc method 
     * @methodOf MenuItemController
     * @name remove
     * 
     * @param {Object} menuItemInstance Menu item that is to be removed.
     * @param {Object} parentMenuItem Parent menu item of the menu item.
     * Parent menu item is optional parameter as root menu items (i.e. menu items
     * in first layer of hierarchy) has no parent menu item.
     * 
     *  @description
     *  Deletes the given menu item instance. If the menu item is already been
     *  saved in database, then it is deleted from database as well as DOM.
     *  However if its not, then it is removed from DOM only (i.e. no request
     *  to server will be sent to delete).
     */
    $scope.remove = function(menuItemInstance, parentMenuItem) {
        if (menuItemInstance.id) {
            menuItemInstance.$delete(null, function() {
                removeFromDOM(menuItemInstance.id, parentMenuItem, 'id');
            });
        } else {
            removeFromDOM(menuItemInstance.tempId, parentMenuItem, 'tempId');
            $scope.enableMenuItemButton();
        }
    };

    /**
     * @ngdoc method
     * @methodOf MenuItemController
     * @name removeFromDOM
     * 
     * @param {Object} value value that is to be removed. 
     * @param {Object} [parentMenuItem] parent menu item of menu item. 
     * @param {Object} property name of the property on which menu item 
     * should be deleted (i.e. 'tempId' for unsaved menu item and 'id' for menu
     * item that is already been saved in database).
     * 
     * @description
     * Deletes the given menu item from DOM.
     */
    function removeFromDOM(value, parentMenuItem, property) {
        if (parentMenuItem) {
            parentMenuItem.childItems.remove(property, value);
        } else {
            $scope.menuItemInstanceList.remove(property, value);
        }
    }

    /**
     * @ngdoc method
     * @methodOf MenuItemController
     * @name getIndexOfMenuItem
     * 
     * @param {Object} tempId tempId of menu item
     * 
     * @description
     * Returns index of the menu item in menuItemInstanceList with 
     * given tempId
     */
    $scope.getIndexOfMenuItem = function(tempId) {
        for (var i=0; i < $scope.menuItemInstanceList.length; i ++) {
            if ($scope.menuItemInstanceList[i].tempId === tempId) {
                return i;
            }
        }
    };

    /**
     * @ngdoc method
     * @methodOf MenuItemController
     * @name save
     * 
     * @param {Object} menuItemInstance menu item that is to be saved.
     * @param {Object} [parent] parent menu item.
     * 
     * @description
     * Saves a menu item in database. If a parent menu item is provided
     * then it is linked to menu item as parent of that menu item.
     */
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
    };

    /**
     * @ngdoc method
     * @methodOf MenuItemController
     * @name update
     * 
     * @description
     * Updates a menu item instance.
     */
    $scope.update = function() {
        $scope.menuItemInstance.$update();
        $scope.modalInstance.close();
    };

    /**
     * @ngdoc method
     * @methodOf MenuItemController
     * @name reorder
     * 
     * @param {Object} menuItemInstance Instance of menu item. 
     * @description
     * Reorders a menu item according to its changed position. 
     */
    $scope.reorder = function(menuItemInstance) {
        MenuItemModel.reorder(menuItemInstance, function() {}, function() {});
    };

    /**
     * @ngdoc method
     * @methodOf MenuItemController
     * @name onMenuItemOrderChanged
     * 
     * @description
     * Changes index of a menu item according to changed position when 
     * order of menu item is changed.
     */
    $scope.onMenuItemOrderChanged = function(event) {
        var index = event.dest.index;
        var menuItemInstance = event.dest.sortableScope.modelValue[index];
        menuItemInstance.index = index; 
        if (menuItemInstance.id !== null) {
            $scope.reorder(menuItemInstance);
        }
    };

    /**
     * @ngdoc method
     * @methodOf MenuItemController
     * @name onMenuItemMoved
     * 
     * @description
     * Changes index of the menu item according to changed position when 
     * parent of menu item is changed.
     */
    $scope.onMenuItemMoved = function(event) {
        var index = event.dest.index;
        var menuItemInstance = event.dest.sortableScope.modelValue[index];
        menuItemInstance.index = index; 
        var parentMenuItem = event.dest.sortableScope.$parent.menuItemInstance;
        if (parentMenuItem !== null) {
            menuItemInstance.parentId = parentMenuItem.id;
        }
        if (menuItemInstance.id !== null) {
            $scope.reorder(menuItemInstance);
        }
    };

    /**
     * @ngdoc method
     * @methodOf MenuItemController
     * @name enableMenuItemButton
     * 
     * @description
     * Used to Enable and Disable button in intermediate state i.e. After create and before save operation.
     */
    $scope.enableMenuItemButton = function() {
        $scope.buttonDisabled = false;
    };

    /**
     * @ngdoc method
     * @methodOf MenuItemController
     * @name disableMenuItemButton
     * 
     * @description
     * Disables new MenuItem button, that allows user to create 
     * new menu item.
     * Whenever new menu item is created, creation of another Menu Item
     * is discouraged unless already created menu item is saved.
     */
    $scope.disableMenuItemButton = function() {
        $scope.buttonDisabled = true;
    };

    // Specifying events for sortable.
    $scope.sortableOptions = {
        accept: function (sourceItemHandleScope, destSortableScope) { //jshint ignore:line
            return true;
        },
        itemMoved: function (event) {
            $scope.onMenuItemMoved(event);
        },
        orderChanged: function (event) {
            $scope.onMenuItemOrderChanged(event);
        }
    };

    $scope.buttonDisabled = false;
    $scope.menuItemInstanceList = [];
    $scope.tempId = 0;
}]);
