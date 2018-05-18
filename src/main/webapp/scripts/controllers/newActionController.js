
angular.module('forgetest').controller('NewActionController', function ($scope, $location, locationParser, flash, ActionResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.action = $scope.action || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'The action was created successfully.'});
            $location.path('/Actions');
        };
        var errorCallback = function(response) {
            if(response && response.data) {
                flash.setMessage({'type': 'error', 'text': response.data.message || response.data}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        ActionResource.save($scope.action, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Actions");
    };
});