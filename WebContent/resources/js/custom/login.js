myApp.controller('loginController', function($scope,$http,$window){
	$scope.msg="";
	$scope.doCheckLogin = function(){
		if($scope.loginDetails){
			if(!$scope.loginDetails.username){
				$scope.msg = "Please Provide UserName!";
			}else if(!$scope.loginDetails.password){
				$scope.msg = "Please Provide Password!";
			}else{
				var request = $http({
					method: 'POST',
					url: restContextpath+"/Entity/checkLogin",
					data: JSON.stringify($scope.loginDetails),
					headers: { 
						'Accept': 'application/json',
						'Content-Type': 'application/json' 
					},
					ContentType: 'application/json'
				});
				request.success(function(data){
					if(data == true){
						$window.location.href = restContextpath+"/home.html";
					}else{
						$scope.msg = "Please Provide Correct Username and Password!";
					}
				});
				request.error(function(data){
					//alert(data);
				});
			}
		}
		else{
			$scope.msg = "Please Provide UserName and Password!";
		}
	}
	
	
	
	
});

