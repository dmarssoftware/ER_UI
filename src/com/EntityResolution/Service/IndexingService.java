package com.EntityResolution.Service;

import java.util.List;

import com.EntityResolution.Dto.IndexingDTO;
import com.EntityResolution.Entity.HadoopIndexing;

public interface IndexingService {
	
	public List<HadoopIndexing> saveAndShowIndexingData(IndexingDTO indexingDTO) throws Exception;
	public void deleteHadoopDirectoryForIndexing(List<HadoopIndexing> hadoopIndexingList);
	public boolean executeSparkCommandForIndexing(List<HadoopIndexing> hadoopIndexingList);
	public List<String> fetchAddrsSegmentationOutputPath() throws Exception;

}
