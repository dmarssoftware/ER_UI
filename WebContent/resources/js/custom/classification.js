myApp.controller('classificationcontroller', function($scope,$http,$window){
	
	 $http.get(restContextpath+"/Entity/fetchClusterNames").then(function(response) {
		 $scope.clusterDropdown = [];
	    	if(response.status == 200){
	    		$scope.clusterDropdown = response.data;
	    	}else{
	    		alert("Something wrong from server.");
	    	}
	    });
	 
	 $scope.selectCluster = function(){
			var request = $http({
				method: 'POST',
				data:$scope.selectedCluster ,
				url: restContextpath+"/Entity/fetchClusterIpPort",
			});
			request.success(function(data){
				//console.log(data);
			});
			request.error(function(data){
				//alert(data);
			});
		}
	
	 $scope.viewHdfsFileList = function(){
			if($scope.selectedCluster == "" || $scope.selectedCluster == null){
				alert("Please select the Cluster!");
			}else{
			var request = $http({
				method: 'POST',
				url: restContextpath+"/Entity/viewHDFSFileList",
				data: $scope.hdfspath ,
				contentType: "application/json",
				headers: { 
					'Accept': 'application/json',
					'Content-Type': 'application/json' 
				},
			});
			request.success(function(data){
				$scope.matchingfileDropdown=[];
				data.forEach(function(obj){
					if(obj.name!=null){
						$scope.matchingfileDropdown.push(obj.name);
					}
				});
			});
			request.error(function(data){
				alert("Something wrong!!");
			});
		}
		}
	 
	 
});