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

import com.EntityResolution.Dao.IndexingDao;
import com.EntityResolution.Entity.HadoopAddressSegmentation;
import com.EntityResolution.Entity.HadoopIndexing;
@Repository
public class IndexingDaoImpl implements IndexingDao{
	
	@Autowired
	SessionFactory sessionfactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<HadoopIndexing> saveandShowIndexingData(HadoopIndexing hadoopIndexing) throws Exception {
		List<HadoopIndexing> indexingList = new ArrayList<HadoopIndexing>();
		boolean isSuccess = false;
		Transaction tx = null;
		Session session = null;
		try {
			session = this.sessionfactory.openSession();
			tx = session.beginTransaction();
			session.save(hadoopIndexing);
			tx.commit();
			isSuccess = true;
			if(isSuccess){
				Criteria cr = session.createCriteria(HadoopIndexing.class);
				cr.add(Restrictions.eq("id", hadoopIndexing.getId()));
				indexingList = cr.list();
			}
			return indexingList;
		} catch (Exception e) {
			isSuccess = false;
			e.printStackTrace();
		} finally {
			session.close();
		}
		return indexingList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HadoopIndexing> fetchIndexingQuery() throws Exception {	
		Session session = null;
		List<HadoopIndexing> indexingList = null;
		try {
			session = this.sessionfactory.openSession();
			Criteria cr = session.createCriteria(HadoopIndexing.class);
			indexingList = cr.list();
			return indexingList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return indexingList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> fetchAddrsSegmentationOutputPath() throws Exception {
		Session session = null;
		List<String> outputPathList = null;
		try {
			session = this.sessionfactory.openSession();
			Criteria cr = session.createCriteria(HadoopAddressSegmentation.class);
			ProjectionList proList = Projections.projectionList();
	        proList.add(Projections.property("outputfilepath"));
			cr.setProjection(proList);
			outputPathList = cr.list();
			return outputPathList;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return outputPathList;
	}

}
