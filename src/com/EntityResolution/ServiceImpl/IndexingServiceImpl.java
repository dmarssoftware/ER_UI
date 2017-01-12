package com.EntityResolution.ServiceImpl;

import java.security.PrivilegedExceptionAction;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.EntityResolution.Dao.IndexingDao;
import com.EntityResolution.Dto.IndexingDTO;
import com.EntityResolution.Dto.IndexingPanelFiles;
import com.EntityResolution.Entity.HadoopIndexing;
import com.EntityResolution.Service.IndexingService;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Service
public class IndexingServiceImpl implements IndexingService{

	@Autowired
	IndexingDao indexingDao;

	private String tempath="",indxingoutputpath="";


	private String indexingOutputPath;
	public String getIndexingOutputPath() {
		return indexingOutputPath;
	}
	public void setIndexingOutputPath(String indexingOutputPath) {
		this.indexingOutputPath = indexingOutputPath;
	}

	@Override
	public List<HadoopIndexing> saveAndShowIndexingData(IndexingDTO indexingDTO) throws Exception {
		String inputPath = indexingDTO.getInputPath();
		String outputPath = indexingDTO.getOutputPath();
		String tempPath = indexingDTO.getTempPath();
		String noOfReducer = indexingDTO.getNoOfReducer();
		String delimiter = indexingDTO.getDelimiter();
		String windowSize = "";
		String indexingContent = "" ;
		int passlength = indexingDTO.getIndexingPanel().size();
		for(int i=0;i<passlength;i++){ 
			windowSize += indexingDTO.getIndexingPanel().get(i).getWindowSize();
			if(i!= passlength-1){
				windowSize += "/";
			}
			List<IndexingPanelFiles> listIndexingpanelFiles = indexingDTO.getIndexingPanel().get(i).getPanelFiles();
			for(int j= 0;j<listIndexingpanelFiles.size();j++){
				if(j!=0){
					indexingContent += "#"+listIndexingpanelFiles.get(j).getIndexingFileName()+"|";
				}
				else{
					indexingContent +=listIndexingpanelFiles.get(j).getIndexingFileName()+"|";
				}
				for(int k=0;k<listIndexingpanelFiles.get(j).getPanelFilesCols().size();k++){
					indexingContent +=listIndexingpanelFiles.get(j).getPanelFilesCols().get(k).getIndexingCol()+","+listIndexingpanelFiles.get(j).getPanelFilesCols().get(k).getIndexingSubstr();
					if(k!=listIndexingpanelFiles.get(j).getPanelFilesCols().size()-1){
						indexingContent +=":";
					}
				}
			}
			if(i!= passlength-1){
				indexingContent += "/";
			}
		}
		System.out.println("indexingContent----"+indexingContent);
		HadoopIndexing hadoopIndexing = new HadoopIndexing();
		hadoopIndexing.setInputPath(inputPath);
		hadoopIndexing.setOutputPath(outputPath);
		//Seeting Indexing Output Path
		this.setIndexingOutputPath(outputPath);
		hadoopIndexing.setTempPath(tempPath);
		hadoopIndexing.setNoOfReducer(noOfReducer);
		hadoopIndexing.setdelimiter(delimiter);
		hadoopIndexing.setIndexingString(indexingContent);
		hadoopIndexing.setWindowSize(windowSize);

		return  indexingDao.saveandShowIndexingData(hadoopIndexing);
	}

	@Override
	public void deleteHadoopDirectoryForIndexing(List<HadoopIndexing> hadoopIndexingList){
		System.setProperty("hadoop.home.dir", "D:\\HADOOP\\winutils\\");
		for(HadoopIndexing hadoopIndexing : hadoopIndexingList ){
			tempath = hadoopIndexing.getTempPath();
			indxingoutputpath = hadoopIndexing.getOutputPath();
		}
		try {
			UserGroupInformation ugi = UserGroupInformation.createRemoteUser("hduser");
			ugi.doAs(new PrivilegedExceptionAction<Void>() {
				public Void run() throws Exception {
					Configuration conf = new Configuration();
					conf.set("fs.defaultFS", "hdfs://172.18.100.103:54310/");
					conf.set("hadoop.job.ugi", "hduser");
					FileSystem fs = FileSystem.get(conf);
					Path dest1 = new Path(tempath);
					Path dest2 = new Path(indxingoutputpath);
					fs.delete(dest1,true);
					fs.delete(dest2,true);
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean executeSparkCommandForIndexing(List<HadoopIndexing> hadoopIndexingList){
		boolean isSuccess = false;
		try{
			String inputpath="", outputpath="", temppath="",indexingString="",noOfReducer="",delimiter="",windowsize="" ;
			for(HadoopIndexing hadoopIndexing:hadoopIndexingList){
				inputpath = hadoopIndexing.getInputPath();
				outputpath = hadoopIndexing.getOutputPath();
				temppath = hadoopIndexing.getTempPath();
				indexingString= hadoopIndexing.getIndexingString();
				noOfReducer=hadoopIndexing.getNoOfReducer();
				windowsize = hadoopIndexing.getWindowSize();
				delimiter=hadoopIndexing.getdelimiter();
			}
			String user = "hduser";
			String password = "Admin123";
			String host = "172.18.100.103";
			String hadoopmaster = "hdfs://hadoop-master:54310" ;
			inputpath = hadoopmaster+inputpath;
			outputpath = hadoopmaster+outputpath;
			temppath = hadoopmaster+temppath;
			String command = "spark-submit --master spark://hadoop-master:7077 ";
			//	command += "--class com.rs.Indexing.Main EntityResolution_JARS/Entity_Resolution.jar hdfs://hadoop-master:54310/EntityResolution/input_files/ hdfs://hadoop-master:54310/EntityResolution/index_output/ hdfs://hadoop-master:54310/EntityResolution/index_temp/";
			command += "--class com.rs.Indexing.Main EntityResolution_JARS/Entity_Resolution.jar "+inputpath+ " " +outputpath+ " "+temppath+" " ;
			command += "\""+indexingString+"\""+" ";
			command += "\""+windowsize+"\""+" ";
			//command +=  "3 , ";
			command += noOfReducer + " " +delimiter+ " " ;
			command += hadoopmaster;
			System.out.println(command);
			int port=22;
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			Channel channel=session.openChannel("exec"); 
			((ChannelExec)channel).setCommand(command);
			channel.connect();
			channel.run();
			isSuccess = true;
			channel.disconnect();
			session.disconnect();
		}
		catch(Exception ex){
			ex.printStackTrace();
			isSuccess = false;
		}
		return isSuccess;
	}
	/*public boolean executeSparkCommandForIndexing(List<HadoopIndexing> hadoopIndexingList){


		String inputpath="", outputpath="", temppath="",indexingString="",noOfReducer="",delimiter="",windowsize="" ;
		for(HadoopIndexing hadoopIndexing:hadoopIndexingList){
			inputpath = hadoopIndexing.getInputPath();
			outputpath = hadoopIndexing.getOutputPath();
			temppath = hadoopIndexing.getTempPath();
			indexingString= hadoopIndexing.getIndexingString();
			noOfReducer=hadoopIndexing.getNoOfReducer();
			windowsize = hadoopIndexing.getWindowSize();
			delimiter=hadoopIndexing.getdelimiter();
		}
		String hadoopmaster = "hdfs://hadoop-master:54310" ;
		inputpath = hadoopmaster+inputpath;
		outputpath = hadoopmaster+outputpath;
		temppath = hadoopmaster+temppath;
		String command = "spark-submit --master spark://hadoop-master:7077 ";
		command += "--class com.rs.Indexing.Main EntityResolution_JARS/Entity_Resolution.jar "+inputpath+ " " +outputpath+ " "+temppath+" " ;
		command += "\""+indexingString+"\""+" ";
		command += "\""+windowsize+"\""+" ";
		command += noOfReducer + " " +delimiter+ " " ;
		command += hadoopmaster;
		System.out.println(command);
		boolean status = ExecuteJarCommand.executeSparkCommand(command);

		System.out.println(status);

		return status;

	}*/



	@Override
	public List<String> fetchAddrsSegmentationOutputPath() throws Exception{
		return  indexingDao.fetchAddrsSegmentationOutputPath();
	}


}
