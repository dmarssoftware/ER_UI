package com.EntityResolution.Controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.EntityResolution.Dto.AlgorithmDTO;
import com.EntityResolution.Dto.MatchingDTO;
import com.EntityResolution.Entity.HadoopMatching;
import com.EntityResolution.Service.MatchingService;

@Controller
@RequestMapping("/Entity")
public class MatchingController {
	
	@Autowired
	MatchingService matchingService;
	
	List<HadoopMatching> hadoopMatchingList = new ArrayList<HadoopMatching>();
	
	@RequestMapping(value = "/showAlgorithmDetails", method = RequestMethod.GET)
	public @ResponseBody List<AlgorithmDTO> showAlgorithmDetails() throws Exception{
		List<AlgorithmDTO> listAlgorithmDTO = matchingService.fetchAlgorithmName();
		return listAlgorithmDTO;
	}
	
	@RequestMapping(value = "/saveMatchingData", method = RequestMethod.POST)
	public @ResponseBody List<HadoopMatching> saveAndShowMatchingData(@RequestBody MatchingDTO matchingDTO) throws Exception {
		boolean validateMatchingFile = matchingService.validateMatchingFile(matchingDTO.getFileName());
		if(validateMatchingFile==false){
			hadoopMatchingList = matchingService.saveAndShowMatchingData(matchingDTO);
			return hadoopMatchingList;
		}
		else{
			return null;
		}
	}
	
	@RequestMapping(value = "/showFileDeleteList", method = RequestMethod.GET)
	public @ResponseBody List<MatchingDTO> showFileDeletList() throws Exception {
		List<MatchingDTO> listfiledelete = matchingService.showFileDeleteList();
		return listfiledelete;
		
	}
	
	@RequestMapping(value = "/deleteMatchingData", method = RequestMethod.POST)
	public @ResponseBody List<MatchingDTO> deleteMatchingData(@RequestBody String filename) throws Exception {
		return matchingService.deleteMatchingData(filename);
	}
	
	@RequestMapping(value = "/executeMatchingjar", method = RequestMethod.POST)
	public @ResponseBody boolean executeMatchingjar() throws Exception {	
		matchingService.deleteHadoopDirectoryForMatching(hadoopMatchingList);
		return matchingService.executeSparkCommandForMatching(hadoopMatchingList);
	}
	
	@RequestMapping(value = "/fetchIndexingOutputPath", method = RequestMethod.GET)
	public @ResponseBody List<String> fetchIndexingOutputPath() throws Exception {	
		return matchingService.fetchIndexingOutputPath();
	}

}
