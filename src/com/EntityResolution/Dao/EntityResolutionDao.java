package com.EntityResolution.Dao;

import java.util.List;

import com.EntityResolution.Entity.Cluster;
import com.EntityResolution.Entity.LoginDetails;
import com.EntityResolution.Entity.Slave;
import com.EntityResolution.Entity.UserDetails;

public interface EntityResolutionDao {

	public boolean validateUsername(String username);
	public boolean saveUserRegistration(UserDetails userDetails) throws Exception;
	public boolean checkLoginUser(LoginDetails userDetails) throws Exception;
	public Cluster getClusterById(Long id) throws Exception;
	public Long SaveClusterName(Cluster cluster) throws Exception;
	public void saveSlave(Slave slave) throws Exception;
	public List<Cluster> getCluster() throws Exception;
	public List<Slave> getSlaveByClusterId(Long id) throws Exception;
	public List<String> fetchClusterNames() throws Exception;
	public List<Object[]> fetchMasterDetails(String clusterName) throws Exception;

}
