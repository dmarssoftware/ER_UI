myApp.controller('indexingController', function($scope,$http){
	
	$scope.isShowStatus = false;
	
	//indexing
	$scope.IndexingDTO = {} ;
	$scope.IndexingDTO.indexingPanel = [];
	$scope.IndexingDTO.indexingPanel.panelFiles = [];
	$scope.indxfileDropdown=[];
	$scope.indxTablelist =[];
	$scope.isShowIndexingTable=false;
	$scope.isShowindxBtn = false;
	$scope.clusterDropdown = [];
	
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
			$scope.fileDropdown=[];
			data.forEach(function(obj){
				if(obj.name!=null){
					$scope.fileDropdown.push(obj.name);
				}
			});
		});
		request.error(function(data){
			alert("Something wrong!!");
		});
	}
	}
	$scope.viewFileContent = function(){
		if($scope.hdfspath == "/"){
			$scope.filepath = $scope.hdfspath + $scope.fileName ;
		}
		else{
		    $scope.filepath = $scope.hdfspath + "/" + $scope.fileName ;
		}
		// 	var fileName=$scope.fileName;
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
			$scope.txtmatchingFilecontent = data ;
		});
		request.error(function(data){
			alert("Something wrong!!");
		});
	}
	
	$http.get(restContextpath+"/Entity/fetchClusterNames").then(function(response) {
		 $scope.clusterDropdown = [];
	    	if(response.status == 200){
	    		$scope.clusterDropdown = response.data;
	    	}else{
	    		alert("Something wrong from server.");
	    	}
	});
	
	$http.get(restContextpath+"/Entity/fetchAddrsSegmentationOutputPath").then(function(response) {
		 $scope.indexinputDropdown = [];
	    	if(response.status == 200){
	    		$scope.indexinputDropdown = response.data;
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
			console.log(data);
		});
		request.error(function(data){
			//alert(data);
		});
	}
	
			$scope.addIndexingPanel = function(){
				$scope.IndexingDTO.indexingPanel = [];
				if($scope.selectedCluster == "" || $scope.selectedCluster == null){
					alert("Please select the Cluster!");
				}
				else if($scope.IndexingDTO.noOfPass == null || $scope.IndexingDTO.noOfPass == "" ){
					alert("Enter no of pass");
				}
				else if($scope.IndexingDTO.noOfPass){
					$scope.isShowindxBtn = true;
					for(var i = 1; i<=$scope.IndexingDTO.noOfPass; i++){
						$scope.IndexingDTO.indexingPanel.push({id:i,windowSize:""});
					}
				}
				else{
					
				}
			}

			$scope.addIndexingFile = function(panelID){
				var data = [];
				$scope.IndexingDTO.indexingPanel.forEach(function(panelJSON){
					if(panelJSON.id == panelID){
						if(panelJSON.panelFiles){
							var countFileId = panelJSON.panelFiles.length+1;
							panelJSON.panelFiles.push({fileID: countFileId});
						}
						else{
							panelJSON.panelFiles = [{fileID: 1}];
						}
					}
					data.push(panelJSON);
				});
				$scope.IndexingDTO.indexingPanel = data;
			}

			$scope.addColSubstr = function(panelID, fileId){
				var data = [];
				$scope.IndexingDTO.indexingPanel.forEach(function(panelJSON){
					if(panelJSON.id == panelID){
						var pjf = [];
						panelJSON.panelFiles.forEach(function(panelFilesJson){
							if(panelFilesJson.fileID == fileId){
								if(panelFilesJson.panelFilesCols){
									var countFileId = panelFilesJson.panelFilesCols.length+1;
									panelFilesJson.panelFilesCols.push({colID: countFileId});
								}
								else{
									panelFilesJson.panelFilesCols = [{colID: 1}];
								}
							}
							pjf.push(panelFilesJson);

						});
						panelJSON.panelFiles = pjf;
					}
					data.push(panelJSON);
				});
				$scope.IndexingDTO.indexingPanel = data;
				console.log($scope.IndexingDTO.indexingPanel);
				console.log($scope.IndexingDTO);
			}


			$scope.viewindxFiledropdown = function(){
				var request = $http({
					method: 'POST',
					url: restContextpath+"/Entity/viewHDFSFileList",
					data: $scope.IndexingDTO.inputPath ,
					contentType: "application/json",
					headers: { 
						'Accept': 'application/json',
						'Content-Type': 'application/json' 
					},
				});
				request.success(function(data){
					console.log(data);
					$scope.indxfileDropdown=[];
					data.forEach(function(obj){
						if(obj.name!=null){
							$scope.indxfileDropdown.push(obj.name);
						}
					});
				});
				request.error(function(data){
					//alert(data);
				});

			}

			$scope.saveIndexingingData = function(){
				var indexingjson = JSON.stringify( $scope.IndexingDTO, function(key,value) {
					if( key === "$$hashKey") {
						return undefined;
					}
					return value;
				});
				console.log(indexingjson);
				var request = $http({
					method: 'POST',
					url: restContextpath+"/Entity/saveIndexingData",
					data: indexingjson,
					headers: { 
						'Accept': 'application/json',
						'Content-Type': 'application/json' 
					},
					ContentType: 'application/json'
				});
				request.success(function(data){
					if(data){
					$scope.isShowIndexingTable=true;
					$scope.indxTablelist=[];
					data.forEach(function(obj){
						$scope.indxTablelist.push({
							id: obj.id,
							inputPath: obj.inputPath,
							tempPath : obj.tempPath,
							outputPath:obj.outputPath,
							windowSize:obj.windowSize,
							noOfReducer: obj.noOfReducer,
							indexingString : obj.indexingString,
							delimiter : obj.delimiter
						});
					});
					}else{
						
					}

				});
				request.error(function(data){
					//alert(data);
				});
			}

			$scope.resetIndexing = function(){
				$scope.indxfileDropdown.length=0;
				$scope.IndexingDTO.noOfPass = "" ;
				$scope.IndexingDTO.indexingPanel.length = 0;
				$scope.IndexingDTO.indexingPanel.panelFiles.length=0;
			}

			$scope.executeIndexingJar = function(){
				var request = $http({
					method: 'POST',
					url: restContextpath+"/Entity/executeIndexingjar",
					headers: { 
						'Accept': 'application/json',
						'Content-Type': 'application/json' 
					},
					ContentType: 'application/json'
				});
				request.success(function(data){
					/*if(data == true){
						$scope.isShowStatus = true;
						//alert("Jar execution started!!!");
					}else{
						alert("Jar Execution Error!!!");
						$scope.isShowStatus = true;
					}*/
					//alert("Please check in unix console!");
					$scope.isShowStatus = true;
				});
				request.error(function(data){
					alert(data);
				});
			}

});	