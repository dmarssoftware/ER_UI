package com.EntityResolution.Entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="slave")
public class Slave {
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "slaveip")
	private String slaveIp;
	
	@Column(name = "slaveport")
	private String slavePort;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="clusterid")
	private Cluster cluster;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSlaveIp() {
		return slaveIp;
	}

	public void setSlaveIp(String slaveIp) {
		this.slaveIp = slaveIp;
	}

	public String getSlavePort() {
		return slavePort;
	}

	public void setSlavePort(String slavePort) {
		this.slavePort = slavePort;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	
	
	
	

}
