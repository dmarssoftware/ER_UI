myApp.config(function($routeProvider){
	var arr = simpleJson(APP_DATA.navData);
	arr.forEach(function(page){
		var control = page.controller?page.controller:"defaultController";
		var page_link = (page.template && page.template!="")?page.template:"not-found";
		$routeProvider.when(page.routeLink, {
			templateUrl:restContextpath+"/templates/"+page_link+".html", 
			controller:control
		});
	});
});

myApp.controller('defaultController', function($scope){
	console.log("default controller");
	
});

myApp.controller('homeController', function($scope,$http){
	
	$scope.logout = function(){
		alert("ok");
	}
	
	
	$scope.init = function(){

	};
});   

