package persistence;

import java.io.Serializable;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;

import model.Image;
import model.Product;

public class ImageDAOImpl extends GenericDAOImpl<Image, Long> implements ImageDAO {

	
	public ImageDAOImpl(){}
	
	
 
	@Override
	public Image findByID(Serializable id) {

		return super.findByID(Image.class, id);
	}
	
	@Override
	public void mergeImages(Set<Image> images,Session session) throws Exception{
		
		for(Image i:images){
			merge(i, session);
		}
		
	}
	
	@Override
	public boolean deleteByProduct(Session session,Serializable id) {

		try {

			Query q = session.createQuery("DELETE FROM Image i where i.product.id= :id");

				  q.setParameter("id", id);
				 
			int num = q.executeUpdate();
 

			return num > 0 ? true : false;
			
		} catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}
	}
	
 
	
}
