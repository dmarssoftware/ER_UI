package com.EntityResolution.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.EntityResolution.Dto.IndexingDTO;
import com.EntityResolution.Entity.HadoopIndexing;
import com.EntityResolution.Service.IndexingService;

@Controller
@RequestMapping("/Entity")
public class IndexingController {
	
	@Autowired IndexingService indexingService;
	
	List<HadoopIndexing> hadoopIndexingList = new ArrayList<HadoopIndexing>();
	
	@RequestMapping(value = "/saveIndexingData", method = RequestMethod.POST)
	public @ResponseBody List<HadoopIndexing> saveandShowIndexingData(@RequestBody IndexingDTO indexingDTO) throws Exception {	
		hadoopIndexingList = indexingService.saveAndShowIndexingData(indexingDTO);
		return hadoopIndexingList;
	}
	@RequestMapping(value = "/executeIndexingjar", method = RequestMethod.POST)
	public @ResponseBody boolean executeIndexingjar() throws Exception {		
		indexingService.deleteHadoopDirectoryForIndexing(hadoopIndexingList);	
		return indexingService.executeSparkCommandForIndexing(hadoopIndexingList);
	}
	
	@RequestMapping(value = "/fetchAddrsSegmentationOutputPath", method = RequestMethod.GET)
	public @ResponseBody List<String> fetchAddrsSegmentationOutputPath() throws Exception {	
		return indexingService.fetchAddrsSegmentationOutputPath();
	}
	

}
