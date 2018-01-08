package persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Session;

import model.Product;

public interface ProductDAO {

	Product findByID(Serializable id);

	Product findByID(Session session, Serializable id);

	void mergeProducts(Set<Product> products, Session session) throws Exception;

	boolean deleteChildren(Session session, Serializable id);

	List<Product> findAllNoChildren();

	Product findByIdNoChildren(Serializable id);

	List<Product> findAll(Session session);

	void persist(Product p) throws Exception;

	void delete(Product product) throws Exception;

	void merge(Product product) throws Exception;

}