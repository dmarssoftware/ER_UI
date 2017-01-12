package com.EntityResolution.Dto;

public class AddressSegmentationDTO {
	
	private String inputfilepath ;
	private String fileName;
	private String addressColIndex;
	private String outputfilepath ;
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getAddressColIndex() {
		return addressColIndex;
	}
	public void setAddressColIndex(String addressColIndex) {
		this.addressColIndex = addressColIndex;
	}
	public String getInputfilepath() {
		return inputfilepath;
	}
	public void setInputfilepath(String inputfilepath) {
		this.inputfilepath = inputfilepath;
	}
	public String getOutputfilepath() {
		return outputfilepath;
	}
	public void setOutputfilepath(String outputfilepath) {
		this.outputfilepath = outputfilepath;
	}
	
	
	

}
