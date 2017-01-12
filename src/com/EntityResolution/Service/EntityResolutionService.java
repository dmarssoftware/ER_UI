package com.EntityResolution.Service;

import java.util.List;
import org.apache.hadoop.fs.Path;
import org.springframework.web.multipart.MultipartFile;
import com.EntityResolution.Dto.ClusterDTO;
import com.EntityResolution.Dto.FileUpload;
import com.EntityResolution.Entity.LoginDetails;
import com.EntityResolution.Entity.UserDetails;

public interface EntityResolutionService {
	
	//cluster registration
	public boolean validateUsername(String username);
	public boolean saveUserRegistration(UserDetails userDetails) throws Exception;
	public boolean checkLoginUser(LoginDetails userDetails) throws Exception;
	public void createRemoteHadoopDirectory() throws Exception;
	public void createRemoteClusterDirectory(String userName) throws Exception;
	public boolean submitClusterRegistration(ClusterDTO clusterDTO) throws Exception;
	public List<ClusterDTO> fetchCluster() throws Exception;
	public List<String> fetchClusterNames() throws Exception;
	//file upload
	public List<FileUpload> singleFileUploadToHDFS(String[] destPath,MultipartFile[] file) throws Exception;
	public boolean multiFileUploadToHDFS(String folder) throws Exception;
	public String viewHDFSFileContent(Path hdfspath) throws Exception;
	public List<Object[]> fetchMasterDetails(String clusterName) throws Exception;
	public List<Path> viewHdfsFilelist(Path hdfspath) throws Exception ;

	
	
	
	
	
}
