myApp.controller('preprocessingController', function($scope,$http,$window){
	
	$scope.isShowStatus = false;
	
	$scope.addressSegmentationDTO = {};
	
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
			});
			request.error(function(data){
			});
			
			var req = $http({
				method: 'POST',
				data:$scope.selectedCluster ,
				url: restContextpath+"/Entity/fetchClusterIpPortAddrs",
			});
			req.success(function(data){
			});
			req.error(function(data){
			});
			
		}
	
	 $scope.viewHdfsFileList = function(){
			if($scope.selectedCluster == "" || $scope.selectedCluster == null){
				alert("Please select the Cluster!");
			}else{
			var request = $http({
				method: 'POST',
				url: restContextpath+"/Entity/viewHDFSFileList",
				data: $scope.addressSegmentationDTO.inputfilepath ,
				contentType: "application/json",
				headers: { 
					'Accept': 'application/json',
					'Content-Type': 'application/json' 
				},
			});
			request.success(function(data){
				$scope.addressSegDropDown=[];
				data.forEach(function(obj){
					if(obj.name!=null){
						$scope.addressSegDropDown.push(obj.name);
					}
				});
			});
			request.error(function(data){
				alert("Something wrong!!");
			});
		}
		}
	 
	 $scope.viewFileContent = function(){
			if($scope.addressSegmentationDTO.inputfilepath == "/"){
				$scope.filepath = $scope.addressSegmentationDTO.inputfilepath + $scope.addressSegmentationDTO.fileName ;
			}
			else{
			    $scope.filepath = $scope.addressSegmentationDTO.inputfilepath + "/" + $scope.addressSegmentationDTO.fileName ;
			}
			//var fileName=$scope.addressSegmentationDTO.fileName;
			var request = $http({
				method: 'POST',
				url: restContextpath+"/Entity/viewHDFSFileContent",
				data: $scope.filepath,
				headers: { 
					'Accept': 'application/text',
					'Content-Type': 'application/text' 
				},
				ContentType: 'application/text'
			});
			request.success(function(data){
				$scope.txtFilecontent = data ;
			});
			request.error(function(data){
				alert("Something wrong!!");
			});
		}
	 
	$scope.submitAddressSegmentation = function(){
		
			var request = $http({
				method: 'POST',
				url: restContextpath+"/Entity/executeAddressSegmentation",
				data: $scope.addressSegmentationDTO ,
				headers: { 
					'Accept': 'application/json',
					'Content-Type': 'application/json' 
				},
				ContentType: 'application/text'
			});
			request.success(function(data){
				/*if(data === true){
					$scope.isShowStatus = true;
				}
				else{
					alert("Something went wrong!Please try again!");
					$scope.isShowStatus = true;
				}*/
				//alert("Please check in the unix console!");
				$scope.isShowStatus = true;
				
			});
			request.error(function(data){
				alert("Something wrong!!");
			});
	}
	
	 
	 
	 
});