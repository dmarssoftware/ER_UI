package com.EntityResolution.DaoImpl;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.EntityResolution.Dao.MatchingDao;
import com.EntityResolution.Entity.HadoopAlgorithm;
import com.EntityResolution.Entity.HadoopIndexing;
import com.EntityResolution.Entity.HadoopMatching;

@Repository
public class MatchingDaoImpl implements MatchingDao {
	
	@Autowired
	SessionFactory sessionfactory;

	@Override
	@SuppressWarnings("unchecked")
	public List<HadoopAlgorithm> fetchAlgorithmName() throws Exception {	
		Session session = null;
		List<HadoopAlgorithm> algorithmList = null;
		try {
			session = this.sessionfactory.openSession();
			Criteria cr = session.createCriteria(HadoopAlgorithm.class);
			algorithmList = cr.list();
			return algorithmList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return algorithmList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<HadoopMatching> showFileDeleteList() throws Exception {
		Session session = null;
		List<HadoopMatching> filedeleteList = null;
		try {
			session = this.sessionfactory.openSession();
			Criteria cr = session.createCriteria(HadoopMatching.class);
			filedeleteList = cr.list();
			return filedeleteList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return filedeleteList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<HadoopMatching> saveAndShowMatchingData(HadoopMatching hadoopMatching) throws Exception {
		List<HadoopMatching> hadoopMatchingList = new ArrayList<HadoopMatching>();
		boolean isSuccess = false;
		Transaction tx = null;
		Session session = null;
		try {
			session = this.sessionfactory.openSession();
			tx = session.beginTransaction();
			session.save(hadoopMatching);
			tx.commit();
			isSuccess = true;
			if(isSuccess){
				Criteria cr = session.createCriteria(HadoopMatching.class);
				cr.add(Restrictions.eq("id", hadoopMatching.getId()));
				hadoopMatchingList = cr.list();
			}
			return hadoopMatchingList;
		} catch (Exception e) {
			isSuccess = false;
			e.printStackTrace();
		} finally {
			session.close();
		}
		return hadoopMatchingList;
	}


	@Override
	public boolean deleteMatchingData(String filename) throws Exception {
		boolean isSuccess = false;
		Transaction tx = null;
		Session session = null;
		try {
			session = this.sessionfactory.openSession();
			tx = session.beginTransaction();
			String deleteQry = "Delete from HadoopMatching where fileName= :filename";
			System.out.println(deleteQry);
			Query deleteMatchingData = session.createQuery(deleteQry);
			deleteMatchingData.setString("filename", filename );
			deleteMatchingData.executeUpdate();
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

	@Override
	@SuppressWarnings({ "unchecked", "unused" })
	public boolean validateMatchingFile(String filename) {
		boolean filePresent = false;
		Transaction tx = null;
		Session session = null;
		List<String> matchingFileNameLst = new ArrayList<String>();
		try {
			session = this.sessionfactory.openSession();
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(HadoopMatching.class);
			cr.add(Restrictions.eq("fileName", filename));
			matchingFileNameLst = cr.list();
			if(null != matchingFileNameLst && matchingFileNameLst.size()>0){
				filePresent = true;
			}
			return filePresent;
		} catch (Exception e) {
			filePresent = false;
			e.printStackTrace();
		} finally {
			session.close();
		}
		return filePresent;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> fetchIndexingOutputPath() throws Exception {
		Session session = null;
		List<String> indexList = null;
		try {
			session = this.sessionfactory.openSession();
			Criteria cr = session.createCriteria(HadoopIndexing.class);
			ProjectionList proList = Projections.projectionList();
	        proList.add(Projections.property("outputPath"));
			cr.setProjection(proList);
			indexList = cr.list();
			return indexList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return indexList;
	}

}
