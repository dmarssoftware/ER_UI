package com.EntityResolution.Dto;

import java.util.ArrayList;
import java.util.List;

public class ClusterDTO {

	private Long id;
	private String clusterName;
	private String masterIp;
	private String masterPort;
	private List<SlaveDTO> slaveDTO = new ArrayList<SlaveDTO>();

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getClusterName() {
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public String getMasterIp() {
		return masterIp;
	}
	public void setMasterIp(String masterIp) {
		this.masterIp = masterIp;
	}
	public String getMasterPort() {
		return masterPort;
	}
	public void setMasterPort(String masterPort) {
		this.masterPort = masterPort;
	}
	public List<SlaveDTO> getSlaveDTO() {
		return slaveDTO;
	}
	public void setSlaveDTO(List<SlaveDTO> slaveDTO) {
		this.slaveDTO = slaveDTO;
	}
}
