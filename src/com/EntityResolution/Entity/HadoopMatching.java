package com.EntityResolution.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="hadoop_matching")
public class HadoopMatching {
	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private long id ;
	
	@Column(name = "FileName")
	private String fileName ;
	
	@Column(name = "Column_AlgoName")
	private String columnAlgoName ;
	
	@Column(name = "Delimiter")
	private String delimiter ;
	
	@Column(name = "MatchingInputPath")
	private String matchingInputPath;
	
	@Column(name = "MatchingOutputPath")
	private String matchingOutputPath;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getColumnAlgoName() {
		return columnAlgoName;
	}

	public void setColumnAlgoName(String columnAlgoName) {
		this.columnAlgoName = columnAlgoName;
	}

	public String getMatchingInputPath() {
		return matchingInputPath;
	}

	public void setMatchingInputPath(String matchingInputPath) {
		this.matchingInputPath = matchingInputPath;
	}

	public String getMatchingOutputPath() {
		return matchingOutputPath;
	}

	public void setMatchingOutputPath(String matchingOutputPath) {
		this.matchingOutputPath = matchingOutputPath;
	}
	
	
}
