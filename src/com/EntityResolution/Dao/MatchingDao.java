package com.EntityResolution.Dao;

import java.util.List;

import com.EntityResolution.Entity.HadoopAlgorithm;
import com.EntityResolution.Entity.HadoopMatching;

public interface MatchingDao {
	
	public List<HadoopAlgorithm> fetchAlgorithmName() throws Exception;
	public List<HadoopMatching> showFileDeleteList() throws Exception;
	public List<HadoopMatching> saveAndShowMatchingData(HadoopMatching hadoopMatching) throws Exception;
	public boolean deleteMatchingData(String filename) throws Exception;
	public boolean validateMatchingFile(String filename) throws Exception ;
	public List<String> fetchIndexingOutputPath() throws Exception;

}
