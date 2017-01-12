package com.EntityResolution.ServiceImpl;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.EntityResolution.Dao.EntityResolutionDao;
import com.EntityResolution.Dto.ClusterDTO;
import com.EntityResolution.Dto.FileUpload;
import com.EntityResolution.Dto.SlaveDTO;
import com.EntityResolution.Entity.Cluster;
import com.EntityResolution.Entity.LoginDetails;
import com.EntityResolution.Entity.Slave;
import com.EntityResolution.Entity.UserDetails;
import com.EntityResolution.Service.EntityResolutionService;
import com.EntityResolution.Util.RowNumGenerator;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Service
public class EntityResolutionServiceImpl implements EntityResolutionService{

	private String line;
	private String content;
	private int count;
	ClusterDTO clusterDTO = new ClusterDTO();
	private static final String HADOOPUSER = "hduser";
	private boolean fileUploadSuccess ;
	private String cluster ;

	@Autowired
	EntityResolutionDao entityResolutionDao;

	public boolean validateUsername(String username){
		return entityResolutionDao.validateUsername(username);
	}

	public boolean saveUserRegistration(UserDetails userDetails) throws Exception{
		return entityResolutionDao.saveUserRegistration(userDetails);
	}


	public boolean checkLoginUser(LoginDetails loginDetails) throws Exception{
		return entityResolutionDao.checkLoginUser(loginDetails);
	}

	public void createRemoteHadoopDirectory(){
		System.setProperty("hadoop.home.dir", "D:\\HADOOP\\winutils\\");
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser(HADOOPUSER);
			ugi.doAs(new PrivilegedExceptionAction<Void>() {
				public Void run() throws Exception {
					Configuration conf = new Configuration();
					conf.set("fs.defaultFS", "hdfs://172.25.3.7:54310/");
					conf.set("hadoop.job.ugi", "hduser");
					FileSystem fs = FileSystem.get(conf);
					//copy local file to hdfs
					Path local = new Path("D:\\EntityResolution\\Directory\\hdfsTestFile.txt");
					Path dest = new Path("/soumyojit");
					fs.copyFromLocalFile(local,dest);
					//create a new file 
					//fs.createNewFile(new Path("/soumyojit/nmtest"));
					//list all file in a directory
					FileStatus[] status = fs.listStatus(new Path("/soumyojit"));
					for(int i=0;i<status.length;i++){
						System.out.println(status[i].getPath());
					}
					System.out.println("Hadoop Directory Created Successfully ");
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createRemoteClusterDirectory(String userName) throws Exception{
		try{
			String user = HADOOPUSER;
			String password = "Admin123";
			String host = "172.25.3.67";
			int port=22;
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
			sftpChannel.connect();
			sftpChannel.cd("soumyojit");
			sftpChannel.mkdir(userName);
			System.out.println("Directory Created Successfully ");
		}
		catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Error In Directory Creation");
		}
	}



	public boolean submitClusterRegistration(ClusterDTO clusterDTO) throws Exception{
		boolean isSuccess = false;
		Cluster cluster = new Cluster();
		cluster.setClusterName(clusterDTO.getClusterName());
		cluster.setMasterIp(clusterDTO.getMasterIp());
		cluster.setMasterPort(clusterDTO.getMasterPort());
		Long id = entityResolutionDao.SaveClusterName(cluster);
		List<SlaveDTO> slaveDTO = clusterDTO.getSlaveDTO();
		try{
			for (SlaveDTO dto : slaveDTO){
				Slave slave = new Slave();
				slave.setSlaveIp(dto.getSlaveIp());
				slave.setSlavePort(dto.getSlavePort());
				slave.setCluster(entityResolutionDao.getClusterById(id));
				entityResolutionDao.saveSlave(slave);
				isSuccess = true;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			isSuccess = false;
		}
		return isSuccess;
	}

	public List<ClusterDTO> fetchCluster() throws Exception{
		List<ClusterDTO> dtoClusterListReturn = new ArrayList<ClusterDTO>();

		List<Cluster> clusterEntity =  entityResolutionDao.getCluster();
		for (Cluster cluster : clusterEntity){
			ClusterDTO dtoReturn = new ClusterDTO();
			dtoReturn.setId(cluster.getId());
			dtoReturn.setClusterName(cluster.getClusterName());
			dtoReturn.setMasterIp(cluster.getMasterIp());
			dtoReturn.setMasterPort(cluster.getMasterPort());
			List<Slave> slaveSet = entityResolutionDao.getSlaveByClusterId(cluster.getId());
			List<SlaveDTO> dtoSlaveListReturn = new ArrayList<SlaveDTO>();
			for (Slave slave : slaveSet){
				SlaveDTO sdto = new SlaveDTO();
				sdto.setId(slave.getId());
				sdto.setSlaveIp(slave.getSlaveIp());
				sdto.setSlavePort(slave.getSlavePort());
				dtoSlaveListReturn.add(sdto);
			}
			dtoReturn.setSlaveDTO(dtoSlaveListReturn);
			dtoClusterListReturn.add(dtoReturn);
		}
		return dtoClusterListReturn;
	}

	public List<String> fetchClusterNames() throws Exception{
		return entityResolutionDao.fetchClusterNames();
	}

	public List<Object[]> fetchMasterDetails(String clusterName) throws Exception{
		List<Object[]> masterConfigDeatilsList = entityResolutionDao.fetchMasterDetails(clusterName);
		if(null !=masterConfigDeatilsList && masterConfigDeatilsList.size() >0 ){
			if(null != masterConfigDeatilsList.get(0)){
				Object[] obj = masterConfigDeatilsList.get(0);
				clusterDTO.setMasterIp((String)obj[0]);
				clusterDTO.setMasterPort((String)obj[1]);
			}
		}
		return masterConfigDeatilsList;
	}

	public List<FileUpload> singleFileUploadToHDFS(String[] destPath,MultipartFile[] files) throws Exception{
		boolean fileUploadtoServer = false;
		List<FileUpload> uploadList = new ArrayList<FileUpload>();
		//System.setProperty("hadoop.home.dir", "D:\\HADOOP\\winutils\\");
		for (int i = 0; i < files.length; i++) {
			FileUpload fileUpload = new FileUpload();
			MultipartFile file = files[i];
			String destpath = destPath[i] ;
			Path dest = new Path(destpath);
			String name = files[i].getOriginalFilename();
			String inputpath = "/home/hadoop/Entity_Resolution/ER_inputFiles/" + name ;
			String outputpath = "/home/hadoop/Entity_Resolution/ER_outputFiles/" + name ;
			try {
				byte[] bytes = file.getBytes();
				File dir1 = new File("/home/hadoop/Entity_Resolution/ER_inputFiles");  
				if (!dir1.exists()){
					dir1.mkdirs();}
				File serverFile = new File(dir1.getAbsolutePath()+ File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close(); 
				fileUploadtoServer = RowNumGenerator.generateRowNum(inputpath, outputpath);
				if(fileUploadtoServer==true){
					Path src = new Path(outputpath);
					boolean fileuploadtohdfs = uploadToHDfS(src,dest);
					if(fileuploadtohdfs==true){
						fileUploadSuccess = true;
						fileUpload.setId(i);
						fileUpload.setStatus(name +" uploaded to HDFS");
						fileUpload.setPath(destpath);
						uploadList.add(fileUpload);
					}
					else{
						fileUploadSuccess = false;
						fileUpload.setId(i);
						fileUpload.setStatus(name +" not uploaded to HDFS");
						fileUpload.setPath(destpath);
						uploadList.add(fileUpload);
					}
				}
				else{
					fileUpload.setId(i);
					fileUpload.setStatus(name +" row number generation error!");
					fileUpload.setPath(destpath);
					uploadList.add(fileUpload);
				}
			} catch (Exception e) {
				e.printStackTrace();
				fileUploadSuccess = false;
				fileUpload.setId(i);
				fileUpload.setStatus(name +" not uploaded to HDFS");
				fileUpload.setPath(destpath);
				uploadList.add(fileUpload);
			}
		}
		return uploadList;
	}
	private boolean uploadToHDfS(Path src,Path dest){
		fileUploadSuccess = false;
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser(HADOOPUSER);
			ugi.doAs(new PrivilegedExceptionAction<Boolean>() {
				public Boolean run() throws Exception {
					Configuration conf = new Configuration();
					cluster = "hdfs://" + clusterDTO.getMasterIp() + ":" + clusterDTO.getMasterPort() + "/" ;
					conf.set("fs.defaultFS", cluster);
					conf.set("hadoop.job.ugi", "hduser");
					FileSystem fs = FileSystem.get(conf);
					if(fs.exists(dest)){
						fs.copyFromLocalFile(src,dest); 
						fileUploadSuccess = true;
					}else{
						fileUploadSuccess = false;
					}
					return fileUploadSuccess;
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			fileUploadSuccess = false;
		}
		return fileUploadSuccess;
	}

	public boolean multiFileUploadToHDFS(String folder) throws Exception{
		boolean fileUploadSuccess = true;
		System.setProperty("hadoop.home.dir", "D:\\HADOOP\\winutils\\");
		Path src = new Path(folder);
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser(HADOOPUSER);
			ugi.doAs(new PrivilegedExceptionAction<Void>() {
				public Void run() throws Exception {
					Configuration conf = new Configuration();
					cluster = "hdfs://" + clusterDTO.getMasterIp() + ":" + clusterDTO.getMasterPort() + "/" ;
					conf.set("fs.defaultFS", cluster);
					conf.set("hadoop.job.ugi", "hduser");
					FileSystem fs = FileSystem.get(conf);
					Path dest = new Path("/soumyojit");
					if(fs.exists(dest) ){
						fs.copyFromLocalFile(src,dest);
					}
					return null;
				}
			});
		}
		catch (Exception e) {
			e.printStackTrace();
			fileUploadSuccess = false;
		}
		return fileUploadSuccess ;
	}


	///changed in viewHDFSFileContent code********
	@Override
	public String viewHDFSFileContent(Path hdfspath) throws Exception{
		//System.setProperty("hadoop.home.dir", "D:\\HADOOP\\winutils\\");
		content = "";
		line = "";
		count = 1;
		UserGroupInformation ugi = UserGroupInformation.createRemoteUser(HADOOPUSER);
		ugi.doAs(new PrivilegedExceptionAction<String>() {	
			public String run() throws Exception {
				Configuration conf = new Configuration();
				cluster = "hdfs://" + clusterDTO.getMasterIp() + ":" + clusterDTO.getMasterPort() + "/" ;
				conf.set("fs.defaultFS", cluster);
				conf.set("hadoop.job.ugi", "hduser");
				FileSystem fs = FileSystem.get(conf);
				if(fs.exists(hdfspath) ){
					BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(hdfspath)));
					//line=br.readLine();
					while (line != null ){
						if( count <= 10){
							line=br.readLine();
							content = content+ "\n" +line;	
						}
						else{
							break ;
						}
						count = count + 1;
					}
				}
				return content;
			}
		});
		return content;
	}

	public List<Path> viewHdfsFilelist(Path hdfspath) {
		List<Path> fileList = new ArrayList<>(); 
		//System.setProperty("hadoop.home.dir", "D:\\HADOOP\\winutils\\");
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser(HADOOPUSER);
			ugi.doAs(new PrivilegedExceptionAction<List<Path>>() {	
				public List<Path> run() throws Exception {
					Configuration conf = new Configuration();
					cluster = "hdfs://" + clusterDTO.getMasterIp() + ":" + clusterDTO.getMasterPort() + "/" ;
					conf.set("fs.defaultFS", cluster);
					conf.set("hadoop.job.ugi", "hduser");
					FileSystem fs = FileSystem.get(conf);
					if(fs.exists(hdfspath) ){
						FileStatus[] status = fs.listStatus(hdfspath);
						for(int i=0;i<status.length;i++){
							if(status[i].isFile()){
								fileList.add(status[i].getPath());
							}
						}
					}
					return fileList;
				}
			});
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return fileList;
	}

}
