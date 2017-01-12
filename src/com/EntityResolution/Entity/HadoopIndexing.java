package com.EntityResolution.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="hadoop_indexing")
public class HadoopIndexing {
	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "inputPath")
	private String inputPath ;
	
	@Column(name = "outputPath")
	private String outputPath ;
	
	@Column(name = "tempPath")
	private String tempPath ;
	
	@Column(name = "indexingString")
	private String indexingString ;
	
	@Column(name = "windowSize")
	private String windowSize ;
	
	@Column(name = "noOfReducer")
	private String noOfReducer ;
	
	@Column(name = "delimiter")
	private String delimiter ;
		
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
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
	public String getIndexingString() {
		return indexingString;
	}
	public void setIndexingString(String indexingString) {
		this.indexingString = indexingString;
	}
	public String getWindowSize() {
		return windowSize;
	}
	public void setWindowSize(String windowSize) {
		this.windowSize = windowSize;
	}
	public String getNoOfReducer() {
		return noOfReducer;
	}
	public void setNoOfReducer(String noOfReducer) {
		this.noOfReducer = noOfReducer;
	}
	public String getdelimiter() {
		return delimiter;
	}
	public void setdelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	
	
	
}
