package com.EntityResolution.Service;

import java.util.List;

import com.EntityResolution.Dto.AddressSegmentationDTO;

public interface PreProcessingService {
	
	public boolean executeAddressSegmentation(AddressSegmentationDTO addressSegmentationDTO) throws Exception;
	public boolean saveAddressSegmentation(AddressSegmentationDTO addressSegmentationDTO) throws Exception;
	public List<Object[]> fetchMasterDetailsAddrs(String clusterName) throws Exception ;
}
