angular.module('forgetest').factory('ActionResource', function($resource){
    var resource = $resource('rest/actions/:ActionId',{ActionId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});