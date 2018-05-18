

angular.module('forgetest').controller('EditActionController', function($scope, $routeParams, $location, flash, ActionResource ) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.action = new ActionResource(self.original);
        };
        var errorCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The action could not be found.'});
            $location.path("/Actions");
        };
        ActionResource.get({ActionId:$routeParams.ActionId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.action);
    };

    $scope.save = function() {
        var successCallback = function(){
            flash.setMessage({'type':'success','text':'The action was updated successfully.'}, true);
            $scope.get();
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        $scope.action.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Actions");
    };

    $scope.remove = function() {
        var successCallback = function() {
            flash.setMessage({'type': 'error', 'text': 'The action was deleted.'});
            $location.path("/Actions");
        };
        var errorCallback = function(response) {
            if(response && response.data && response.data.message) {
                flash.setMessage({'type': 'error', 'text': response.data.message}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        }; 
        $scope.action.$remove(successCallback, errorCallback);
    };
    
    
    $scope.get();
});