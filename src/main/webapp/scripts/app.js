'use strict';

angular.module('forgetest',['ngRoute','ngResource'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/',{templateUrl:'views/landing.html',controller:'LandingPageController'})
      .when('/Actions',{templateUrl:'views/Action/search.html',controller:'SearchActionController'})
      .when('/Actions/new',{templateUrl:'views/Action/detail.html',controller:'NewActionController'})
      .when('/Actions/edit/:ActionId',{templateUrl:'views/Action/detail.html',controller:'EditActionController'})
      .when('/Items',{templateUrl:'views/Item/search.html',controller:'SearchItemController'})
      .when('/Items/new',{templateUrl:'views/Item/detail.html',controller:'NewItemController'})
      .when('/Items/edit/:ItemId',{templateUrl:'views/Item/detail.html',controller:'EditItemController'})
      .when('/Users',{templateUrl:'views/User/search.html',controller:'SearchUserController'})
      .when('/Users/new',{templateUrl:'views/User/detail.html',controller:'NewUserController'})
      .when('/Users/edit/:UserId',{templateUrl:'views/User/detail.html',controller:'EditUserController'})
      .otherwise({
        redirectTo: '/'
      });
  }])
  .controller('LandingPageController', function LandingPageController() {
  })
  .controller('NavController', function NavController($scope, $location) {
    $scope.matchesRoute = function(route) {
        var path = $location.path();
        return (path === ("/" + route) || path.indexOf("/" + route + "/") == 0);
    };
  });
