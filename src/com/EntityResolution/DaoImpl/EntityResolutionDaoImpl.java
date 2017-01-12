package com.EntityResolution.DaoImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.EntityResolution.Dao.EntityResolutionDao;
import com.EntityResolution.Entity.Cluster;
import com.EntityResolution.Entity.LoginDetails;
import com.EntityResolution.Entity.Slave;
import com.EntityResolution.Entity.UserDetails;

@Repository
public class EntityResolutionDaoImpl implements EntityResolutionDao{

	@Autowired
	SessionFactory sessionfactory;

	@SuppressWarnings({ "unchecked", "unused" })
	public boolean validateUsername(String username) {
		boolean userPresent = false;
		Transaction tx = null;
		Session session = null;
		List<String> userNameLst = new ArrayList<String>();
		try {
			session = this.sessionfactory.openSession();
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(UserDetails.class);
			cr.add(Restrictions.eq("username", username));
			userNameLst = cr.list();
			if(null != userNameLst && userNameLst.size()>0){
				userPresent = true;
			}
			return userPresent;
		} catch (Exception e) {
			userPresent = false;
			e.printStackTrace();
		} finally {
			session.close();
		}
		return userPresent;
	}

	public boolean saveUserRegistration(UserDetails userDetails) throws Exception{
		boolean isSuccess = false;
		Transaction tx = null;
		Session session = null;
		try {
			session = this.sessionfactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(userDetails);
			tx.commit();
			isSuccess = true;
		} catch (Exception e) {
			isSuccess = false;
			e.printStackTrace();
		} finally {
			session.close();
		}
		return isSuccess;
	}

	@SuppressWarnings("unchecked")
	public boolean checkLoginUser(LoginDetails loginDetails) throws Exception{
		boolean isValidLogin = false;
		Session session = null;
		List<UserDetails> userDetailsList = new ArrayList<UserDetails>();
		try {
			session = this.sessionfactory.openSession();
			Criteria cr = session.createCriteria(UserDetails.class);
			cr.add(Restrictions.eq("username",loginDetails.getUsername()));
			cr.add(Restrictions.eq("password",loginDetails.getPassword()));
			userDetailsList = cr.list();
			if(userDetailsList.size() > 0){
				isValidLogin = true;
			}else{
				isValidLogin =false;
			}
		} catch (Exception e) {
			isValidLogin = false;
			e.printStackTrace();
		} finally {
			session.close();
		}
		return isValidLogin;
	}



	public Cluster getClusterById(Long id) throws Exception{
		Session session = null;
		Cluster cluster = null;
		try {
			session = this.sessionfactory.openSession();
			Criteria cr = session.createCriteria(Cluster.class);
			cr.add(Restrictions.eq("id", id));
			cluster = (Cluster) cr.list().get(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return cluster;
	}

	public Long SaveClusterName(Cluster cluster) throws Exception{
		Transaction tx = null;
		Session session = null;
		Long id = null;
		try {
			session = this.sessionfactory.openSession();
			tx = session.beginTransaction();
			id = (Long) session.save(cluster);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return id;
	}

	public void saveSlave(Slave slave) throws Exception{
		Transaction tx = null;
		Session session = null;
		try {
			session = this.sessionfactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(slave);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Cluster> getCluster() throws Exception{
		Session session = null;
		List<Cluster> list = null;
		try {
			session = this.sessionfactory.openSession();
			Criteria cr = session.createCriteria(Cluster.class);
			cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			list = cr.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Slave> getSlaveByClusterId(Long id) throws Exception{
		Session session = null;
		List<Slave> slave = null;
		try {
			session = this.sessionfactory.openSession();
			Criteria cr = session.createCriteria(Slave.class);
			cr.add(Restrictions.eq("cluster", this.getClusterById(id)));
			slave = cr.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return slave;
	}

	@SuppressWarnings("unchecked")
	public List<String> fetchClusterNames() throws Exception{
		Session session = null;
		List<String> clusterNames = null;
		try {
			session = this.sessionfactory.openSession();
			Criteria cr = session.createCriteria(Cluster.class);
			ProjectionList proList = Projections.projectionList();
			proList.add(Projections.property("clusterName"));
			cr.setProjection(proList);
			clusterNames = cr.list();
			return clusterNames;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return clusterNames;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> fetchMasterDetails(String clusterName) throws Exception{
		Session session = null;
		List<Object[]> masterConfigDetails = null;
		try {
			session = this.sessionfactory.openSession();
			Criteria cr = session.createCriteria(Cluster.class);
			cr.add(Restrictions.eq("clusterName", clusterName));
			ProjectionList proList = Projections.projectionList();
			proList.add(Projections.property("masterIp"));
			proList.add(Projections.property("masterPort"));
			cr.setProjection(proList);
			masterConfigDetails = cr.list();
			return masterConfigDetails;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return masterConfigDetails;
	}

	/*@SuppressWarnings("unchecked")
		public List<HadoopMatching> fetchMatchingQuery() throws Exception {	
			Session session = null;
			List<HadoopMatching> matchingList = null;
			try {
				session = this.sessionfactory.openSession();
				Criteria cr = session.createCriteria(HadoopMatching.class);
				matchingList = cr.list();
				return matchingList;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				session.close();
			}
			return matchingList;
		}
	 */


}
