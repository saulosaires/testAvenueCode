package persistence;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

public interface GenericDAO<T, ID extends Serializable> {

	void persist(T entity, Session session) throws Exception;

	void persist(T entity) throws Exception;

	void update(T entity, Session session) throws Exception;

	void merge(T entity, Session session) throws Exception;

	void merge(T entity) throws Exception;

	void delete(T entity) throws Exception;

	List findAll(Class clazz);

	List findAll(Session session, Class clazz);

	T findByID(Class clazz, Serializable id);

	T findByID(Session session, Class clazz, Serializable id);
 

}