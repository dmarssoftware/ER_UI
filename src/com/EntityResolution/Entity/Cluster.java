package com.EntityResolution.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cluster")
public class Cluster {

	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "clustername")
	private String clusterName;
	
	@Column(name = "masterip")
	private String masterIp;
	
	@Column(name = "masterport")
	private String masterPort;

	//@OneToMany(mappedBy="cluster")
	//private Set<Slave> slaves;
	
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

	/*public Set<Slave> getSlaves() {
		return slaves;
	}

	public void setSlaves(Set<Slave> slaves) {
		this.slaves = slaves;
	}*/

	
}
