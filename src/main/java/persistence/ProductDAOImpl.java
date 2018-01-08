package persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import model.Product;

public class ProductDAOImpl extends GenericDAOImpl<Product, Long> implements ProductDAO {

	
	public ProductDAOImpl(){}
 
	@Override
	public Product findByID(Serializable id) {

		return super.findByID(Product.class, id);
	}

	@Override
	public Product findByID(Session session,Serializable id) {

		return super.findByID(session,Product.class, id);
	}
 
	@Override
	public void mergeProducts(Set<Product> products,Session session) throws Exception{
		
		for(Product p:products){
			merge(p, session);
		}
		
	}
	
	@Override
	public boolean deleteChildren(Session session,Serializable id) {


		try {

			Query q = session.createQuery("DELETE FROM Product p where p.parent.id= :id");

				 q.setParameter("id", id);
				 
			int num = q.executeUpdate();

		 

			return num > 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}
	}
	
	@Override
	public List<Product> findAllNoChildren() {
		
		Session session = this.getSession();
		session.beginTransaction();

		List<Product> T = null;
		Query query = session.createQuery("SELECT new Product(p.id,p.name,p.description) from Product p" );

		T = query.list();
		session.getTransaction().commit();

		return (List<Product>)T;
	}
	
	@Override
	public Product findByIdNoChildren(Serializable id) {
		
		Session session = this.getSession();
		session.beginTransaction();

		
		Query query = session.createQuery("SELECT new Product(p.id,p.name,p.description) from Product p WHERE p.id=:id" );
		query.setParameter("id",id);
		
		Product p = (Product) query.uniqueResult();
		session.getTransaction().commit();

		return p;
	}

	@Override
	public List<Product> findAll(Session session) {
		 
		return super.findAll(session, Product.class);
	}
	
 
	
}
