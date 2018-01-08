package persistence;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import util.HibernateUtil;

public class GenericDAOImpl<T, ID extends Serializable> implements Serializable, GenericDAO<T, ID> {

	public GenericDAOImpl() {
		 
	}
	
	protected Session getSession() {
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}
 

	@Override
	public void persist(T entity, Session session) throws Exception {

		try {

			session.persist(entity);

		} catch (Exception ex) {

			throw ex;
		}

	}

	@Override
	public void persist(T entity) throws Exception {

		final Session session = this.getSession();

		Transaction transaction = null;

		try {

			if (session.getTransaction() != null && session.getTransaction().isActive()) {
				transaction = session.getTransaction();
			} else {
				transaction = session.beginTransaction();
			}

			session.persist(entity);

			transaction.commit();

		} catch (Exception ex) {

			if (transaction != null)
				transaction.rollback();
			throw ex;
		}

	}

	@Override
	public void update(T entity, Session session) throws Exception {

		try {

			session.update(entity);

		} catch (Exception ex) {

			throw ex;
		}

	}
	
	@Override
	public void merge(T entity, Session session) throws Exception {

		try {

			session.merge(entity);

		} catch (Exception ex) {

			throw ex;
		}

	}
	
	@Override
	public void merge(T entity) throws Exception {

		final Session session = this.getSession();

		Transaction transaction = null;

		try {

			if (session.getTransaction() != null && session.getTransaction().isActive()) {
				transaction = session.getTransaction();
			} else {
				transaction = session.beginTransaction();
			}

			session.merge(entity);

			transaction.commit();

		} catch (Exception ex) {

			if (transaction != null)
				transaction.rollback();
			throw ex;
		}

	}

	@Override
	public void delete(T entity) throws Exception {
		final Session session = this.getSession();

		Transaction transaction = null;

		try {

			if (session.getTransaction() != null && session.getTransaction().isActive()) {
				transaction = session.getTransaction();
			} else {
				transaction = session.beginTransaction();
			}

			session.delete(entity);

			transaction.commit();

		} catch (Exception ex) {

			if (transaction != null)
				transaction.rollback();
			throw ex;
		}

	}

	@Override
	public List findAll(Class clazz) {
		Session session = this.getSession();
		session.beginTransaction();

		List T = null;
		Query query = session.createQuery("from " + clazz.getName());

		T = query.list();
		session.getTransaction().commit();

		return T;
	}

	@Override
	public List findAll(Session session ,Class clazz) {
	

		List T = null;
		Query query = session.createQuery("from " + clazz.getName());

		T = query.list();
		

		return T;
	}
	
	@Override
	public T findByID(Class clazz, Serializable id) {
		Session session = this.getSession();
		session.beginTransaction();
		T t = null;
		t = (T) session.get(clazz, id);
		session.getTransaction().commit();

		return t;
	}
	
	@Override
	public T findByID(Session session,Class clazz, Serializable id) {
		
		T t = null;
		t = (T) session.get(clazz, id);
		 

		return t;
	}	
	
	
}
