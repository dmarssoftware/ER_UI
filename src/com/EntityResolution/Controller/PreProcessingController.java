package com.EntityResolution.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.EntityResolution.Dto.AddressSegmentationDTO;
import com.EntityResolution.Service.PreProcessingService;

@Controller
@RequestMapping("/Entity")
public class PreProcessingController {

	@Autowired 
	PreProcessingService preProcessingService;

	@RequestMapping(value = "/executeAddressSegmentation", method = RequestMethod.POST)
	public @ResponseBody boolean executeAddressSegmentation(@RequestBody AddressSegmentationDTO addressSegmentationDTO) throws Exception {		
		boolean isSuccess = preProcessingService.saveAddressSegmentation(addressSegmentationDTO);
		if(isSuccess == true){
			boolean msg = preProcessingService.executeAddressSegmentation(addressSegmentationDTO);
			return msg;
		}
		else{
			return false;
		}
	}
	
	@RequestMapping(value = "/fetchClusterIpPortAddrs" ,method = RequestMethod.POST)
	public @ResponseBody List<Object[]> fetchClusterIpPortAddrs(@RequestBody String cluster) throws Exception{
		return preProcessingService.fetchMasterDetailsAddrs(cluster);
	}
	
}

