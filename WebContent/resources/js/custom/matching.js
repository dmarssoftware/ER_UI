myApp.controller('matchingController', function($scope,$http,$window){
	
	$scope.isShowStatus = false;
	
	$scope.matchingDTO = {};
	$scope.matchingDTO.matchingDetailsDTO = [];
	$scope.isDisabled = false;
	$scope.matchingfileDropdown=[];
	$scope.filepath = {};
	$scope.matchTablelist=[];
	$scope.matchinginputfileDropdown=[];
	$scope.isShowMatchingTable = false;	
	$scope.isShowMatchingCol = false;
	
	 $http.get(restContextpath+"/Entity/fetchIndexingOutputPath").then(function(response) {
		 $scope.matchinginputfileDropdown = [];
	    	if(response.status == 200){
	    		$scope.matchinginputfileDropdown = response.data;
	    	}else{
	    		alert("Something wrong from server.");
	    	}
	 });
	 
	 $http.get(restContextpath+"/Entity/showAlgorithmDetails").then(function(response) {
		 $scope.algorithmDropdown = [];
		 response.data.forEach(function(obj){
				if(obj.algorithmName!=null){
					$scope.algorithmDropdown.push(obj.algorithmName);
				}
			});
	    });
	 
	 $http.get(restContextpath+"/Entity/showFileDeleteList").then(function(response) {
		 $scope.dltfileDropdown = [];
		 response.data.forEach(function(obj){
				if(obj.fileName!=null){
					$scope.dltfileDropdown.push(obj.fileName);
				}
			});
	    });
	 
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

		$scope.addColumnAlgorithm = function(){
			$scope.matchingDTO.matchingDetailsDTO.push({columnNumber:"", algorithmName:""});	
		}

		$scope.removeColumnAlgorithm = function(i){
			var jsonDel =  [];
			var j = 0;
			$scope.matchingDTO.matchingDetailsDTO.forEach(function(obj){
				if(i!=j){
					jsonDel.push(obj);
				}
				j++;
			});
			$scope.matchingDTO.matchingDetailsDTO = jsonDel;
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

		$scope.viewMatchingFileContent = function(){
			$scope.isDisabled = true;
			if($scope.hdfspath == "/"){
				$scope.filepath = $scope.hdfspath + $scope.matchingDTO.fileName ;
			}
			else{
			    $scope.filepath = $scope.hdfspath + "/" + $scope.matchingDTO.fileName ;
			}
			var fileName=$scope.matchingDTO.fileName;
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

			$scope.matchColvalidate = function(){
				var firstLine = $scope.txtmatchingFilecontent.split('\n')[1];
				var tblJson =[] , tblarr=[];var tblJsonArr;
				if($scope.matchingDTO.delimiter=="" || $scope.matchingDTO.delimiter==null){
					alert("Please provide the valid delimiter!");
				}else{
				var delim = $scope.matchingDTO.delimiter;
				tblJson = firstLine.split(delim);
				tblarr.push("");
				tblJsonArr = tblarr.concat(tblJson);
				var len = tblJsonArr.length ;
				for(j=0 ;j<$scope.matchingDTO.matchingDetailsDTO.length;j++){
					var col = $scope.matchingDTO.matchingDetailsDTO[j].columnNumber;
				}
				if(isNaN(col)){
					alert("Please provide numeric value!");
				}
				if(col > (len-1) || col===0){
					alert("Column No should not be 0 or greater than "+(len-1));
				}
				}
			}

		$scope.saveMatchingData = function(){
			$scope.showMathchingTable = function(){
				$scope.isShowMatchingCol = true;
				if($scope.matchingDTO.delimiter == "" || $scope.matchingDTO.delimiter == null){
					alert("Please provide the valid delimiter!");
					$scope.isShowMatchingTable = false;	
					$scope.isShowMatchingCol = false;
					}
				else{
				var colarr = [];
				var myEl = angular.element( document.querySelector( '#matchTable' ));
				var firstLine = $scope.txtmatchingFilecontent.split('\n')[1];
				var tblJson =[];
				var delim = $scope.matchingDTO.delimiter;
				tblJson = firstLine.split(delim);
				var tblarr=[];
				tblarr.push("");
				var tblJsonArr = tblarr.concat(tblJson);
				var fileName = $scope.matchingDTO.fileName;
				var crtTr;
				var colorcrtTr;
				for(j=0 ;j<$scope.matchingDTO.matchingDetailsDTO.length;j++){
					var col = $scope.matchingDTO.matchingDetailsDTO[j].columnNumber;
					colarr.push(col);
				}	
				myEl.append('<tr>');
				fileName ="<td style='font-weight:bold;background-color:BEFBE7'>"+fileName+"</td>";
				myEl.append(fileName);
				for(i=0;i<tblJsonArr.length;i++){
					crtTr ="<td >"+tblJsonArr[i]+"</td>";
					colorcrtTr ="<td style='background-color:FFFF99'>"+tblJsonArr[i]+"</td>";
					if(colarr.indexOf(i.toString())!==-1){
						myEl.append(colorcrtTr);
					}
					else{
						myEl.append(crtTr);
					}
				}	
				myEl.append('</tr>');
		    	}
			}
				$scope.matchingDTO.fileName = $scope.matchingDTO.fileName.split('/').pop().split('\\').pop();		
				var request = $http({
					method: 'POST',
					url: restContextpath+"/Entity/saveMatchingData",
					data: JSON.stringify($scope.matchingDTO),
					headers: { 
						'Accept': 'application/json',
						'Content-Type': 'application/json' 
					},
					ContentType: 'application/json'
				});
				request.success(function(data){
						$scope.isDisabled = false;
						$scope.isDisabled = false;
						if(data=="" || data==null){
							alert("File already exists in DB!");
						}else{
							$scope.showMathchingTable();
							$scope.isShowMatchingTable = true;	
							$scope.matchTablelist=[];
							data.forEach(function(obj){
								$scope.matchTablelist.push({
									id: obj.id,
									fileName : obj.fileName,
									columnAlgoName : obj.columnAlgoName,
									delimiter : obj.delimiter,
									indexingOutputPath : obj.indexingOutputPath,
									matchingInputPath : obj.matchingInputPath,
									matchingOutputPath : obj.matchingOutputPath
								});
							});
						}
				});
				request.error(function(data){
					//alert(data);
				});
			}

			$scope.deleteMatchingData = function(){
				if($scope.fileListDlt!=""){
					var request = $http({
						method: 'POST',
						url: restContextpath+"/Entity/deleteMatchingData",
						data: $scope.fileListDlt,
						headers: { 
							'Accept': 'application/json',
							'Content-Type': 'application/json' 
						},
						ContentType: 'application/json'
					});
					request.success(function(data){
						$scope.dltfileDropdown=[];
						data.forEach(function(obj){
							if(obj.fileName!=null){
								$scope.dltfileDropdown.push(obj.fileName);
							}
						});
					});
					request.error(function(data){
						//alert(data);
					});
				}
				else{
					alert("Select a file to delete!");
				}
			}

				$scope.resetMatching = function(){
					$scope.isDisabled = false;
					$scope.matchingfileDropdown.length = 0;
					$scope.matchingDTO.matchingDetailsDTO.length = 0;
					$scope.tblJsonArr.length = 0;
				}

				$scope.executeMatchingJar = function(){
					var request = $http({
						method: 'POST',
						url: restContextpath+"/Entity/executeMatchingjar",
						headers: { 
							'Accept': 'application/json',
							'Content-Type': 'application/json' 
						},
						ContentType: 'application/json'
					});
					request.success(function(data){
						/*if(data == true){
							$scope.isShowStatus = true;
						}else{
							alert("Jar Execution Error!!!");
							$scope.isShowStatus = true;
						}*/
						$scope.isShowStatus = true;
						//alert("Please check in unix console..");
					});
					request.error(function(data){
						alert("Something wrong!");
					});
				}	
});