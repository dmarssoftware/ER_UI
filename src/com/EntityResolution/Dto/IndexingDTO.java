package com.EntityResolution.Dto;

import java.util.ArrayList;
import java.util.List;

public class IndexingDTO {
	
	private String inputPath ;
	private String outputPath ;
	private String tempPath ;
	private String noOfPass ;
	private String noOfReducer ;
	private String delimiter;
	private List<IndexingPanel>  indexingPanel = new ArrayList<IndexingPanel>();
	
	
	public String getInputPath() {
		return inputPath;
	}
	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}
	public String getOutputPath() {
		return outputPath;
	}
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	public String getTempPath() {
		return tempPath;
	}
	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}
	public String getNoOfPass() {
		return noOfPass;
	}
	public void setNoOfPass(String noOfPass) {
		this.noOfPass = noOfPass;
	}
	public String getNoOfReducer() {
		return noOfReducer;
	}
	public void setNoOfReducer(String noOfReducer) {
		this.noOfReducer = noOfReducer;
	}
	
	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	public List<IndexingPanel> getIndexingPanel() {
		return indexingPanel;
	}
	public void setIndexingPanel(List<IndexingPanel> indexingPanel) {
		this.indexingPanel = indexingPanel;
	}

}
