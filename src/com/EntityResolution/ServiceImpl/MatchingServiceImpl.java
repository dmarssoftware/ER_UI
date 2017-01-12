package com.EntityResolution.ServiceImpl;

import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.EntityResolution.Dao.MatchingDao;
import com.EntityResolution.Dto.AlgorithmDTO;
import com.EntityResolution.Dto.MatchingDTO;
import com.EntityResolution.Entity.HadoopAlgorithm;
import com.EntityResolution.Entity.HadoopMatching;
import com.EntityResolution.Service.MatchingService;
import com.EntityResolution.Util.ExecuteJarCommand;

@Service
public class MatchingServiceImpl implements MatchingService{

	@Autowired 
	MatchingDao matchingDao ;

	private String matchingoutputpath="";

	@Override
	public List<AlgorithmDTO> fetchAlgorithmName() throws Exception {
		List<AlgorithmDTO> algorithmDTOlist = new ArrayList<AlgorithmDTO>();
		List<HadoopAlgorithm> algorithmEntity =  matchingDao.fetchAlgorithmName();
		for (HadoopAlgorithm algorithm : algorithmEntity){
			AlgorithmDTO algorithmDTO = new AlgorithmDTO();
			algorithmDTO.setId(algorithm.getId());
			algorithmDTO.setAlgorithmName(algorithm.getAlgorithmName());
			algorithmDTOlist.add(algorithmDTO);
		}
		return algorithmDTOlist;
	}


	@Override
	public List<MatchingDTO> showFileDeleteList() throws Exception {
		List<MatchingDTO> matchingDTOlist = new ArrayList<MatchingDTO>();
		List<HadoopMatching> matchingEntity =  matchingDao.showFileDeleteList();
		for (HadoopMatching matching : matchingEntity){
			MatchingDTO matchingDTO = new MatchingDTO();
			matchingDTO.setFileName(matching.getFileName());
			matchingDTOlist.add(matchingDTO);
		}
		return matchingDTOlist;
	}



	@Override
	public List<HadoopMatching> saveAndShowMatchingData(MatchingDTO matchingDTO) throws Exception {
		String columnAlgoName = "";
		String fileName = matchingDTO.getFileName();
		String delimiter = matchingDTO.getDelimiter();
		String matchingInputPath = matchingDTO.getMatchingInputPath();
		String matchingOutputPath= matchingDTO.getMatchingOutputPath();
		for(int i=0;i<(matchingDTO.getMatchingDetailsDTO().size());i++){
			columnAlgoName = columnAlgoName +(matchingDTO.getMatchingDetailsDTO().get(i).getColumnNumber())+":"+(matchingDTO.getMatchingDetailsDTO().get(i).getAlgorithmName());
			if(i!=(matchingDTO.getMatchingDetailsDTO().size()-1)){
				columnAlgoName = columnAlgoName + ";" ;
			}	 
		}
		System.out.println("columnAlgo======="+columnAlgoName);
		HadoopMatching hadoopMatching = new HadoopMatching();
		hadoopMatching.setFileName(fileName);
		hadoopMatching.setDelimiter(delimiter);
		hadoopMatching.setColumnAlgoName(columnAlgoName);
		hadoopMatching.setMatchingInputPath(matchingInputPath);
		hadoopMatching.setMatchingOutputPath(matchingOutputPath);

		return  matchingDao.saveAndShowMatchingData(hadoopMatching);
	}

	@Override
	public List<MatchingDTO> deleteMatchingData(String filename) throws Exception {
		matchingDao.deleteMatchingData(filename);
		List<MatchingDTO> matchingDTOlist =  this.showFileDeleteList();
		return matchingDTOlist;	
	}

	@Override
	public void deleteHadoopDirectoryForMatching(List<HadoopMatching> hadoopMatchingList){
		System.setProperty("hadoop.home.dir", "D:\\HADOOP\\winutils\\");
		for(HadoopMatching hadoopMatching : hadoopMatchingList){
			matchingoutputpath = hadoopMatching.getMatchingOutputPath();
		}
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser("hduser");
			ugi.doAs(new PrivilegedExceptionAction<Void>() {
				public Void run() throws Exception {
					Configuration conf = new Configuration();
					conf.set("fs.defaultFS", "hdfs://172.18.100.103:54310/");
					conf.set("hadoop.job.ugi", "hduser");
					FileSystem fs = FileSystem.get(conf);
					Path dest = new Path(matchingoutputpath);
					fs.delete(dest,true);
					System.out.println("Hadoop Directory Deleted Successfully For Matching");
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean executeSparkCommandForMatching(List<HadoopMatching> matchingList){
		String filename="",columnAlgoName="",delimiter="",matchinputpath="",matchoutputpath="";
		for(HadoopMatching hadoopMatching : matchingList){
			filename=hadoopMatching.getFileName();
			columnAlgoName=hadoopMatching.getColumnAlgoName();
			delimiter = hadoopMatching.getDelimiter();
			matchinputpath = hadoopMatching.getMatchingInputPath();
			matchoutputpath = hadoopMatching.getMatchingOutputPath();
		}
		String hadoopMaster = "hdfs://hadoop-master:54310";
		matchinputpath = hadoopMaster+matchinputpath;
		matchoutputpath = hadoopMaster+matchoutputpath;
		String command = "spark-submit --master spark://hadoop-master:7077 ";
		command += "--class com.rs.Matching.MainObject EntityResolution_JARS/Entity_Resolution.jar"+" "+matchinputpath+"/part*"+" "+matchoutputpath+ " ";
		command += "\""+filename+"|"+columnAlgoName+"\"" +" "+delimiter;
		System.out.println(command);
		boolean status = ExecuteJarCommand.executeSparkCommand(command);
		System.out.println(status);
		return status;

	}

	@Override
	public boolean validateMatchingFile(String filename) throws Exception{
		return matchingDao.validateMatchingFile(filename);
	}

	@Override
	public List<String> fetchIndexingOutputPath() throws Exception{
		return  matchingDao.fetchIndexingOutputPath();
	}



}
