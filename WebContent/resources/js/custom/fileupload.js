myApp.controller('fileuploadController', function($scope, $http, jsonData){
	
	$scope.fileUploadDetails = [{id:0, status:""}];
	$scope.checkedItemsArray1 = [];
	$scope.count = 1;
	$scope.chkCount = 0;
	$scope.singleFilePath = "" ;
	$scope.clusterDropdown = [];
	
	var request = $http({
		method: 'GET',
		url: restContextpath+"/Entity/fetchClusterNames",
	});
	request.success(function(data){
		$scope.clusterDropdown = [];
		data.forEach(function(obj){
			if(obj!=null){
				$scope.clusterDropdown.push(obj);
			}
		});
	});
	request.error(function(data){
		//alert(data);
	});		
	
	$scope.selectCluster = function(){
		var request = $http({
			method: 'POST',
			data:$scope.selectedCluster ,
			url: restContextpath+"/Entity/fetchClusterIpPort",
		});
		request.success(function(data){
			console.log(data);
		});
		request.error(function(data){
			//alert(data);
		});
	}
	
	
$scope.addNew1 = function(fileDetail){
	$scope.count = $scope.count + 1;
	var rowCount1 = $scope.fileUploadDetails[$scope.fileUploadDetails.length-1].id+1;
	$scope.fileUploadDetails.push({id:rowCount1, status:""});
	//console.log($scope.fileUploadDetails);
};

$scope.checkedItem1 = function(id, $event){
	var elem = angular.element($event.target);
	if($(elem).is(':checked')){
		$scope.chkCount = $scope.chkCount + 1;
		$scope.checkedItemsArray1.push(id);

	}
	else{
		$scope.chkCount = $scope.chkCount - 1;
		var newItems = [];
		angular.forEach($scope.checkedItemsArray1, function(sid){
			if(sid!=id){
				newItems.push(sid);
			}
		});
		$scope.checkedItemsArray1 = newItems;
	}
	console.log($scope.checkedItemsArray1);
}
$scope.remove1 = function(){
			var newItems = [];
			console.log($scope.checkedItemsArray1);
			angular.forEach($scope.fileUploadDetails, function(singleRow){
				if($scope.checkedItemsArray1.indexOf(singleRow.id)==-1){
					newItems.push(singleRow);
				}
			});
			if(newItems.length>0){
				$scope.checkedItemsArray1 = [];
				$scope.chkCount = 0;
				$scope.fileUploadDetails = newItems;
			}
};

$scope.viewHdfsFileContent = function(){
	if($scope.selectedCluster == "" || $scope.selectedCluster == null ){
		alert("Please select the Cluster!");
	}else{
		
		var request = $http({
			method: 'POST',
			url: restContextpath+"/Entity/viewHDFSFileContent",
			data: $scope.hfdsPath,
			headers: { 
				'Accept': 'application/text',
				'Content-Type': 'application/text' 
			},
			ContentType: 'application/text'
		});
		request.success(function(data){
			$scope.hfdsFileContent = data ;
		});
		request.error(function(data){
			alert("Something wrong!!");
		});
	}		
}


$scope.uploadFile = function(el, event){
	var selectcluster = $("#selectcluster").val();
	if(selectcluster===null || selectcluster==="" ){
		alert("Please select a cluster to upload files");
	}else{
		$.ajax({
			url: restContextpath+"/Entity/copySingleFileToHDFS",
			type: "POST",
			data: new FormData(document.getElementById("uploadform")),
			enctype: 'multipart/form-data',
			processData: false,
			contentType: false
		}).done(function(data){
			console.log(data);
			$scope.$apply(function() {
				$scope.fileUploadDetails = data;
				$scope.checkedItemsArray1 = [];
				$scope.chkCount = 0;
			});
		}).fail(function(jqXHR, textStatus) {
			alert('File upload failed ...');
		});
	}
}

});