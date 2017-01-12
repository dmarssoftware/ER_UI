package com.EntityResolution.Service;

import java.util.List;
import com.EntityResolution.Dto.AlgorithmDTO;
import com.EntityResolution.Dto.MatchingDTO;
import com.EntityResolution.Entity.HadoopMatching;

public interface MatchingService {
	
	public List<AlgorithmDTO> fetchAlgorithmName() throws Exception;
	public List<MatchingDTO> showFileDeleteList() throws Exception;
	public List<HadoopMatching> saveAndShowMatchingData(MatchingDTO matchingDTO) throws Exception;
	public List<MatchingDTO> deleteMatchingData(String filename) throws Exception;
	public void deleteHadoopDirectoryForMatching(List<HadoopMatching> matchingList);
	public boolean executeSparkCommandForMatching(List<HadoopMatching> matchingList);
	public boolean validateMatchingFile(String filename) throws Exception;
	public List<String> fetchIndexingOutputPath() throws Exception;

}
