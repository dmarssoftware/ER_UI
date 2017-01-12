package com.EntityResolution.ServiceImpl;

import java.io.File;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EntityResolution.Dao.EntityResolutionDao;
import com.EntityResolution.Dao.PreProcessingDao;
import com.EntityResolution.Dto.AddressSegmentationDTO;
import com.EntityResolution.Dto.ClusterDTO;
import com.EntityResolution.Entity.HadoopAddressSegmentation;
import com.EntityResolution.Service.PreProcessingService;
import com.EntityResolution.Util.ExecuteJarCommand;


@Service
public class PreProcessingServiceImpl implements PreProcessingService{

	@Autowired 
	PreProcessingDao preProcessingDao;
	@Autowired
	EntityResolutionDao entityResolutionDao;
	
	ClusterDTO clusterDTO = new ClusterDTO();
	private static final String HADOOPUSER = "hduser";
	private String cluster ;
	private boolean ismergesuccess = false;
	
	@Override
	public List<Object[]> fetchMasterDetailsAddrs(String clusterName) throws Exception {
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

	@Override
	public boolean saveAddressSegmentation(AddressSegmentationDTO addressSegmentationDTO) throws Exception {
		HadoopAddressSegmentation hadoopAddressSegmentation = new HadoopAddressSegmentation();
		hadoopAddressSegmentation.setFileName(addressSegmentationDTO.getFileName());
		hadoopAddressSegmentation.setInputfilepath(addressSegmentationDTO.getInputfilepath());
		hadoopAddressSegmentation.setAddressColIndex(addressSegmentationDTO.getAddressColIndex());
		hadoopAddressSegmentation.setOutputfilepath(addressSegmentationDTO.getOutputfilepath());
	return	preProcessingDao.saveAddressSegmentation(hadoopAddressSegmentation);
	}

	@Override
	public boolean executeAddressSegmentation(AddressSegmentationDTO addressSegmentationDTO) throws Exception{
		String command = "";
		String hadoopMaster = "hdfs://hadoop-master:54310";
		String sparknode = "spark://hadoop-master:7077" ;
		String filename = addressSegmentationDTO.getFileName() ;
		String inputfilepath = addressSegmentationDTO.getInputfilepath();
		String addressColIndex = addressSegmentationDTO.getAddressColIndex();
		String outputpath = addressSegmentationDTO.getOutputfilepath();
		command = "spark-submit address_segmentation.py ";
		command +=  hadoopMaster ;
		command  += inputfilepath + "/" +filename+ " " ;
		command +=  hadoopMaster + outputpath +"/"+filename + "_dir ";
		command += sparknode + " " + addressColIndex;	
		System.out.println(command);
		ExecuteJarCommand.executeSparkCommand(command);
		TimeUnit.SECONDS.sleep(80);
		boolean mergedFile = mergePartFile(filename,outputpath);
		return mergedFile;
	}

	private boolean mergePartFile(String filename,String outputpath){
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser(HADOOPUSER);
			ugi.doAs(new PrivilegedExceptionAction<Boolean>() {
				public Boolean run() throws Exception {
					Configuration conf = new Configuration();
					cluster = "hdfs://" + clusterDTO.getMasterIp() + ":" + clusterDTO.getMasterPort() ;
					conf.set("fs.defaultFS", cluster);
					conf.set("hadoop.job.ugi", HADOOPUSER);
					FileSystem fs = FileSystem.get(conf);
					// put file to hdfs
						System.out.println("hdcluster==="+cluster);
						String inputPath =  "/home/hadoop/Entity_Resolution/ER_AdrsSeg/"+filename ;
						File blankfile = new File(inputPath);
						blankfile.createNewFile();
						Path pathinput = new Path(inputPath);
						Path pathOutput = new Path(outputpath);
						Path successPath = new Path(pathOutput+"/"+filename+"_dir/_SUCCESS");
						Path subDirpath = new Path(pathOutput+"/"+filename+"_dir");
						fs.copyFromLocalFile(pathinput,subDirpath); 
						fs.delete(successPath, true);
					// merge file	
						String trg = cluster + outputpath +"/" + filename + "_dir/"+ filename;
						System.out.println("trg   "+trg);
						Path target = new Path(trg);
						List<Path> fileList = new ArrayList<Path>(); 
						FileStatus[] fstatus1 = fs.listStatus(new Path(outputpath+"/"+filename+"_dir"));
						for(int i=0;i<fstatus1.length;i++){
							if(fstatus1[i].getPath().equals(target) || fstatus1[i].isDirectory()){
								System.out.println("break...");
							}else{
								fileList.add(fstatus1[i].getPath());
								System.out.println(fstatus1[i].getPath());
							}
						}
						Path [] mergeFile = fileList.toArray(new Path[fileList.size()]);
						fs.concat(target, mergeFile);
						System.out.println("merging done..");
						fs.rename(target, pathOutput);
						fs.delete(subDirpath, true);
					return ismergesuccess =true;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return ismergesuccess =false;
		}
		return ismergesuccess;
	}




}
