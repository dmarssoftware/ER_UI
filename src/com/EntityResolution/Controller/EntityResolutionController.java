package com.EntityResolution.Controller;

import java.util.List;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.EntityResolution.Dto.ClusterDTO;
import com.EntityResolution.Dto.FileUpload;
import com.EntityResolution.Entity.LoginDetails;
import com.EntityResolution.Entity.UserDetails;
import com.EntityResolution.Service.EntityResolutionService;


@Controller
@RequestMapping("/Entity")
public class EntityResolutionController {
	
	@Autowired 
	EntityResolutionService entityResolutionService;
	
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public @ResponseBody boolean registerUser(@RequestBody UserDetails userDetails) throws Exception {
		boolean validateUsername = entityResolutionService.validateUsername(userDetails.getUsername());
			//entityResolutionService.createRemoteHadoopDirectory();
			//entityResolutionService.createRemoteClusterDirectory(userDetails.getUsername());
		if(validateUsername == false){
			return entityResolutionService.saveUserRegistration(userDetails);
		}
		else{
			return false;
		}
    }
	
	@RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
    public @ResponseBody boolean checkLogin(@RequestBody LoginDetails loginDetails) {
		try{
		return entityResolutionService.checkLoginUser(loginDetails);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return true;
    }
	
	@RequestMapping(value = "/showClusterDetails ", method = RequestMethod.GET)
    public @ResponseBody List<ClusterDTO> showClusterDetails(String clusterName) throws Exception{
		List<ClusterDTO> listClusterDTO = entityResolutionService.fetchCluster();
		return listClusterDTO;
	}
	
	@RequestMapping(value = "/submitClusterRegistration ", method = RequestMethod.POST)
    public @ResponseBody boolean submitClusterRegistration(@RequestBody ClusterDTO clusterDTO) throws Exception {
			return entityResolutionService.submitClusterRegistration(clusterDTO);
    }
	
	@RequestMapping(value = "/fetchClusterNames" ,method = RequestMethod.GET)
	public @ResponseBody List<String> fetchClusterNames() throws Exception {
			return entityResolutionService.fetchClusterNames();
	}
	
	@RequestMapping(value = "/fetchMasterDetails" ,method = RequestMethod.POST)
	public void fetchMasterDetails(@RequestBody ClusterDTO clusterDTO) throws Exception{
		 entityResolutionService.fetchMasterDetails(clusterDTO.getClusterName());
	}
	//file upload
	@RequestMapping(value = "/copySingleFileToHDFS" ,method = RequestMethod.POST,headers=("content-type=multipart/*"))
	public @ResponseBody  List<FileUpload> singleFileUploadToHDFS(@RequestParam("destPath") String[] destPath,@RequestParam("file") MultipartFile[] file) throws Exception {
		return entityResolutionService.singleFileUploadToHDFS(destPath,file);
	}

	@RequestMapping(value = "/copyMultipleFileToHDFS" ,method = RequestMethod.POST)
	public @ResponseBody boolean multiFileUploadToHDFS(@RequestBody String folder) throws Exception {
		return entityResolutionService.multiFileUploadToHDFS(folder);
	}
	@RequestMapping(value = "/viewHDFSFileContent" ,method = RequestMethod.POST)
	public @ResponseBody String viewHDFSFileContent(@RequestBody String hdfsPath) throws Exception{
		Path path = new Path(hdfsPath);
		return entityResolutionService.viewHDFSFileContent(path);
	}	
	@RequestMapping(value = "/fetchClusterIpPort" ,method = RequestMethod.POST)
	public @ResponseBody List<Object[]> fetchClusterIpPort(@RequestBody String cluster) throws Exception{
		return entityResolutionService.fetchMasterDetails(cluster);
	}
	@RequestMapping(value = "/viewHDFSFileList" ,method = RequestMethod.POST)
	public @ResponseBody List<Path> viewHdfsFilelist(@RequestBody String hdfsPath) throws Exception {
		Path path = new Path(hdfsPath);
		return entityResolutionService.viewHdfsFilelist(path);
	}
	

	
	
	
}
