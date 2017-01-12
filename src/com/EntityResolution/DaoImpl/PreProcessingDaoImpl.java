package com.EntityResolution.DaoImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.EntityResolution.Dao.PreProcessingDao;
import com.EntityResolution.Entity.HadoopAddressSegmentation;

@Repository
public class PreProcessingDaoImpl implements PreProcessingDao{

	@Autowired
	SessionFactory sessionfactory;

	@Override
	public boolean saveAddressSegmentation(HadoopAddressSegmentation hadoopAddressSegmentation) throws Exception {
		boolean isSuccess = false;
		Transaction tx = null;
		Session session = null;
		try {
			session = this.sessionfactory.openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(hadoopAddressSegmentation);
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


}





