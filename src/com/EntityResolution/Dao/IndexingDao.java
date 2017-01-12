package com.EntityResolution.Dao;

import java.util.List;

import com.EntityResolution.Entity.HadoopIndexing;

public interface IndexingDao {

	public List<HadoopIndexing> saveandShowIndexingData(HadoopIndexing hadoopIndexing) throws Exception;
	public List<HadoopIndexing> fetchIndexingQuery() throws Exception;
	public List<String> fetchAddrsSegmentationOutputPath() throws Exception ;
}
