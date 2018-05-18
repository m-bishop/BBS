angular.module('forgetest').factory('ItemResource', function($resource){
    var resource = $resource('rest/items/:ItemId',{ItemId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});