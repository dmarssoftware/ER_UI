myApp.controller('clusterManagement', function($scope, $http, jsonData){
	
	$scope.clusterTablelist = [];
	$scope.clusterDropdown=[];
	$scope.clusterListToShow = [];
	$scope.clusterDTO = {};
	$scope.clusterDTO.slaveDTO = [];
	$scope.clusterName="";
	
	$scope.addNewSlavesCluster = function(){
		$scope.clusterDTO.slaveDTO.push({id:"", slaveIp:"", slavePort:""});
		console.log($scope.clusterDTO);
		console.log($scope.clusterDTO.slaveDTO);
	}
	$scope.removeSlaveRow = function(i){
		var jsonDel =  [];
		var j = 0;
		$scope.clusterDTO.slaveDTO.forEach(function(obj){
			if(i!=j){
				jsonDel.push(obj);}
			j++;
		});
		$scope.clusterDTO.slaveDTO = jsonDel;
	}
	
	$scope.submitClusterRegistration = function(){
		var json = JSON.stringify( $scope.clusterDTO, function(key,value) {
			if( key === "$$hashKey" ) {
				return undefined;}
			return value;
		});
		var request = $http({
			method: 'POST',
			url: restContextpath+"/Entity/submitClusterRegistration",
			data: json,
			headers: { 
				'Accept': 'application/json',
				'Content-Type': 'application/json' },
			ContentType: 'application/json'
		});
		request.success(function(data){
			if(data == true){
				alert("Cluster is Registered!!");
				$scope.clusterDTO.clusterName = "";
				$scope.clusterDTO.masterIp = "";
				$scope.clusterDTO.masterPort = "";
				$scope.clusterDTO = "";
			}else{
				alert("Cluster not Registered!!");
			}
		});
		request.error(function(data){
			alert("Something wrong in Server!");
		});
	}
	
	//**********************************************************************************************************************//
    $scope.clusterTablelist = [];
    $http.get(restContextpath+"/Entity/showClusterDetails").then(function(response) {
    	console.log(response);
    	if(response.status == 200){
    		$scope.clusterTablelist = response.data;
    	}else{
    		alert("Something wrong from server.");
    	}
    	//$scope.clusterTablelist = response.data;
    	
    });

$scope.showClusterDetails = function(id){
	var json = jsonData.getJsonByKeyAndValueFromJsonArray($scope.clusterTablelist, "id", id);
	$scope.clusterListToShow = [];
	$scope.clusterListToShow[0] = json;
}

$scope.selectCluster = function(){
	//$scope.lblclusterName = $scope.selectedCluster;
	var request = $http({
		method: 'POST',
		data:$scope.selectedCluster ,
		url: restContextpath+"/Entity/fetchClusterIpPort",
	});
	request.success(function(data){
		//alert(data);
	});
	request.error(function(data){
		//alert(data);
	});
}
$scope.fetchMasterDetails = function(){
	//	alert($scope.clusterDTO);
	var request = $http({
		method: 'POST',
		data: JSON.stringify($scope.clusterDTO),
		url: restContextpath+"/Entity/fetchMasterDetails",
	});
	request.success(function(data){
		//alert(data);
	});
	request.error(function(data){
		//alert(data);
	});
}
});