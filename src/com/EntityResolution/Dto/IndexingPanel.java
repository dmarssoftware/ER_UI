package com.EntityResolution.Dto;

import java.util.ArrayList;
import java.util.List;

public class IndexingPanel {
	
	private String id ;
	private String windowSize ;
	private List<IndexingPanelFiles> panelFiles = new ArrayList<IndexingPanelFiles>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getWindowSize() {
		return windowSize;
	}
	public void setWindowSize(String windowSize) {
		this.windowSize = windowSize;
	}
	public List<IndexingPanelFiles> getPanelFiles() {
		return panelFiles;
	}
	public void setPanelFiles(List<IndexingPanelFiles> panelFiles) {
		this.panelFiles = panelFiles;
	}
	
	
	
	

}
