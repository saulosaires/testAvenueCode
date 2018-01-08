package persistence;

import java.io.Serializable;
import java.util.Set;

import org.hibernate.Session;

import model.Image;

public interface ImageDAO {

	public Image findByID(Serializable id);

	public void mergeImages(Set<Image> images, Session session) throws Exception;

	public boolean deleteByProduct(Session session, Serializable id);

	public void persist(Image image) throws Exception;

	public void delete(Image image) throws Exception;

	public void merge(Image image) throws Exception;
}