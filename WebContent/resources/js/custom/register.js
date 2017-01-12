myApp.controller('registrationController', function($scope,$http,$window){
	$scope.msg="";
	$scope.doRegisterUser = function(){
		console.log($scope.userDetails);
		if($scope.userDetails){

			if(!$scope.userDetails.name){
				$scope.msg="Please provide your Name ";
			}else if(!$scope.userDetails.email){
				$scope.msg="Please provide Email ";
			}else if(!$scope.userDetails.email.match("@")){
				$scope.msg="Please provide valid email id ";
			}else if(!$scope.userDetails.username){
				$scope.msg="Please provide username ";
			}else if(!$scope.userDetails.password){
				$scope.msg="Please provide password ";
			}else if(!$scope.userDetails.confirmPassword){
				$scope.msg="Please provide confirm password ";
			}else if($scope.userDetails.password.length <6 || $scope.userDetails.confirmPassword.length < 6){
				$scope.msg="Password should be atleast 6 character long";
			}else if ($scope.userDetails.password != $scope.userDetails.confirmPassword){
				$scope.msg="Password don't match";
			}else{
				var request = $http({
					method: 'POST',
					url: restContextpath+"/Entity/registerUser",
					data: JSON.stringify($scope.userDetails),
					headers: { 
						'Accept': 'application/json',
						'Content-Type': 'application/json' 
					},
					ContentType: 'application/json'
				});

				request.success(function(data){
					if(data == true){
						$scope.msg="You have successfully registered to ER!";
						$window.location.href = restContextpath+"/login.html";
					}
					else{
						$scope.msg="Username is already exists! Please try again with another!";
					}
				});

				request.error(function(data){
					//alert(data);
				});
			}
		}
		else{
			$scope.msg="Please provide the credentials to Register";
		}

	}

});