

angular.module('forgetest').controller('EditItemController', function($scope, $routeParams, $location, flash, ItemResource , ActionResource, ItemResource) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.item = new ItemResource(self.original);
            ActionResource.queryAll(function(items) {
                $scope.actionsSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.command
                    };
                    if($scope.item.actions){
                        $.each($scope.item.actions, function(idx, element) {
                            if(item.id == element.id) {
                                $scope.actionsSelection.push(labelObject);
                                $scope.item.actions.push(wrappedObject);
                            }
                        });
                        self.original.actions = $scope.item.actions;
                    }
                    return labelObject;
                });
            });
            ItemResource.queryAll(function(items) {
                $scope.itemsSelectionList = $.map(items, function(item) {
                    var wrappedObject = {
                        id : item.id
                    };
                    var labelObject = {
                        value : item.id,
                        text : item.name
                    };
                    if($scope.item.items){
                        $.each($scope.item.items, function(idx, element) {
                            if(item.id == element.id) {
                                $scope.itemsSelection.push(labelObject);
                                $scope.item.items.push(wrappedObject);
                            }
                        });
                        self.original.items = $scope.item.items;
                    }
                    return labelObject;
                });
            });
        };
        var errorCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The item could not be found.'});
            $location.path("/Items");
        };
        ItemResource.get({ItemId:$routeParams.ItemId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.item);
    };

    $scope.save = function() {
        var successCallback = function(){
            flash.setMessage({'type':'success','text':'The item was updated successfully.'}, true);
            $scope.get();
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        $scope.item.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Items");
    };

    $scope.remove = function() {
        var successCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The item was deleted.'});
            $location.path("/Items");
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        }; 
        $scope.item.$remove(successCallback, errorCallback);
    };
    
    $scope.actionsSelection = $scope.actionsSelection || [];
    $scope.$watch("actionsSelection", function(selection) {
        if (typeof selection != 'undefined' && $scope.item) {
            $scope.item.actions = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.item.actions.push(collectionItem);
            });
        }
    });
    $scope.itemsSelection = $scope.itemsSelection || [];
    $scope.$watch("itemsSelection", function(selection) {
        if (typeof selection != 'undefined' && $scope.item) {
            $scope.item.items = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.item.items.push(collectionItem);
            });
        }
    });
    
    $scope.get();
});