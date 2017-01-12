/*$(document).ready(function(){
	
});*/

function simpleJson(jsonArr){
		var arr = [];
		jsonArr.forEach(function(json){
			if(json.children){
				arr.push(json);
				json.children.forEach(function(childJson){
					arr.push(childJson);
				});
			}else{
				arr.push(json);
			}
		});
		return arr;
	}


/**********************************************************************/
var myApp = angular.module('myApp', ["ngRoute"]);
var restContextpath="http://172.18.100.102:8080/EntityResolution";
//var restContextpath="http://dma.rssoftware.co.in/EntityResolution";

myApp.controller('leftNavigation', function($scope){
	$scope.leftNav = APP_DATA.navData;
});

myApp.service("jsonData", function(){
	this.simplify = function(jsonArr){
		var arr = [];
		jsonArr.forEach(function(json){
			if(json.children){
				json.children.forEach(function(childJson){
					arr.push(childJson);
				});
			}else{
				arr.push(json);
			}
		});
		return arr;
	}
	this.getJsonByKeyAndValueFromJsonArray = function(arrJson, jkey, value){
		var R = false;
		arrJson.forEach(function(json){
			for(key in json){
				if(key==jkey && json[key]==value){
					R = json;
				}
			}
		});
		return R;
	}
	
	this.resetIdForJsonArray = function(arr){
		var R = [];
		var count = 0;
		arr.forEach(function(json){
			json.id = count;
			R.push(json);
			count++;
		});
		return R;
	}
	
});

