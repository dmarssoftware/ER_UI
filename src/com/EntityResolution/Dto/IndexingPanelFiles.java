package com.EntityResolution.Dto;

import java.util.ArrayList;
import java.util.List;

public class IndexingPanelFiles {
	
	private int fileID ;
	private String indexingFileName;
	private List<IndexingPanelFilesCols> panelFilesCols = new ArrayList<IndexingPanelFilesCols>();
	
	
	public int getFileID() {
		return fileID;
	}
	public void setFileID(int fileID) {
		this.fileID = fileID;
	}
	public String getIndexingFileName() {
		return indexingFileName;
	}
	public void setIndexingFileName(String indexingFileName) {
		this.indexingFileName = indexingFileName;
	}
	public List<IndexingPanelFilesCols> getPanelFilesCols() {
		return panelFilesCols;
	}
	public void setPanelFilesCols(List<IndexingPanelFilesCols> panelFilesCols) {
		this.panelFilesCols = panelFilesCols;
	}

	
	
}
