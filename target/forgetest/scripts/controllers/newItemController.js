
angular.module('forgetest').controller('NewItemController', function ($scope, $location, locationParser, flash, ItemResource , ActionResource, ItemResource) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.item = $scope.item || {};
    
    $scope.actionsList = ActionResource.queryAll(function(items){
        $scope.actionsSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.id
            });
        });
    });
    $scope.$watch("actionsSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.item.actions = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.item.actions.push(collectionItem);
            });
        }
    });

    $scope.itemsList = ItemResource.queryAll(function(items){
        $scope.itemsSelectionList = $.map(items, function(item) {
            return ( {
                value : item.id,
                text : item.id
            });
        });
    });
    $scope.$watch("itemsSelection", function(selection) {
        if (typeof selection != 'undefined') {
            $scope.item.items = [];
            $.each(selection, function(idx,selectedItem) {
                var collectionItem = {};
                collectionItem.id = selectedItem.value;
                $scope.item.items.push(collectionItem);
            });
        }
    });


    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'The item was created successfully.'});
            $location.path('/Items');
        };
        var errorCallback = function(response) {
            if(response && response.data) {
                flash.setMessage({'type': 'error', 'text': response.data.message || response.data}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        ItemResource.save($scope.item, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Items");
    };
});