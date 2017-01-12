package com.EntityResolution.Dao;

import com.EntityResolution.Entity.HadoopAddressSegmentation;

public interface PreProcessingDao {
	
	public boolean saveAddressSegmentation(HadoopAddressSegmentation hadoopAddressSegmentation) throws Exception;

}
