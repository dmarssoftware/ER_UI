package com.EntityResolution.Dto;

import java.util.ArrayList;
import java.util.List;

public class MatchingDTO {
	
	private Long id;
	private String fileName ;
	private String delimiter;
	private String matchingInputPath;
	private String matchingOutputPath;
	private List<MatchingDetailsDTO> matchingDetailsDTO = new ArrayList<MatchingDetailsDTO>();

	public String getDelimiter() {
		return delimiter;
	}
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
	public List<MatchingDetailsDTO> getMatchingDetailsDTO() {
		return matchingDetailsDTO;
	}
	public void setMatchingDetailsDTO(List<MatchingDetailsDTO> matchingDetailsDTO) {
		this.matchingDetailsDTO = matchingDetailsDTO;
	}
	
	
}
