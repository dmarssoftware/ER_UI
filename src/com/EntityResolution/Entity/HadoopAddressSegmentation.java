package com.EntityResolution.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="hadoop_address_segmentation")
public class HadoopAddressSegmentation {

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private long id ;

	@Column(name = "inputfilepath")
	private String inputfilepath ;

	@Column(name = "filename")
	private String fileName;

	@Column(name = "addresscolindex")
	private String addressColIndex;

	@Column(name = "outputfilepath")
	private String outputfilepath ;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getInputfilepath() {
		return inputfilepath;
	}

	public void setInputfilepath(String inputfilepath) {
		this.inputfilepath = inputfilepath;
	}

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

	public String getOutputfilepath() {
		return outputfilepath;
	}

	public void setOutputfilepath(String outputfilepath) {
		this.outputfilepath = outputfilepath;
	}




}
