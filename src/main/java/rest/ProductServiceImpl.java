package rest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Hibernate;
import org.hibernate.Session;

import model.Image;
import model.Product;
import persistence.ImageDAO;
import persistence.ImageDAOImpl;
import persistence.ProductDAO;
import persistence.ProductDAOImpl;
import util.HibernateUtil;
import util.JsonParser;
import util.Util;



@Path("product")
public class ProductServiceImpl implements ProductService {

	 private  ProductDAO productDAO;
	 private  ImageDAO   imageDAO;
	 
	 {   //MELHOR USAR CDI, PARA COMO É SO UM EXERCICIO SERVE AO PROPOSITO !
		 productDAO = new ProductDAOImpl();
		 imageDAO   = new ImageDAOImpl();
	 }
 
	 
	@Override
	@POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
	public Response create(String body) {

		Product p = (Product) JsonParser.fromJson(body, Product.class);

		try {

			setImagesProduct( p);

			setChildrenProductsParent( p);

			productDAO.persist(p);

			return Util.sendSucess(Util.ID, p.getId().toString());

		} catch (Exception e) {
			e.printStackTrace();
		
			return Util.sendInternalServerError(e.getLocalizedMessage());

		}

	}	
	
	@Override
	@PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Long id, String body) {

		Optional<Long> productId = Optional.ofNullable(id);
		
		if(!productId.isPresent()){
			return Util.sendBadRequest("id not valid");
		}
		
		Product p = (Product) JsonParser.fromJson(body, Product.class);
 
		try {
 
			 
			Product product = productDAO.findByID( id);
					product.setName(p.getName());
					product.setDescription(p.getDescription());
			
		   Optional<Product> parent = Optional.ofNullable(p.getParent());
	
		   if(parent.isPresent()){
			   
			  Optional<Long> parentId = Optional.ofNullable(parent.get().getId());
			  
			  if(parentId.isPresent())
			    product.setParent(productDAO.findByID(parentId.get())); 
			   
		   }else{
			   product.setParent(null);
		   }

			productDAO.merge(product);
	 
			return Util.sendSucess(Util.ID, product.getId().toString());

		} catch (Exception e) {
			e.printStackTrace();
		 
			return Util.sendInternalServerError(e.getLocalizedMessage());

		}

	}
	
	@Override
	@DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") Long id) {
		
		Optional<Long> productId = Optional.ofNullable(id);
		
		if(!productId.isPresent()){
			return Util.sendBadRequest("id not valid");
		}
		

		Product product = productDAO.findByID(id);
		
		try {
			productDAO.delete(product);
			
			return Util.sendSucess(Util.ID, product.getId().toString());
		} catch (Exception e) {
			e.printStackTrace();
			return Util.sendInternalServerError(e.getLocalizedMessage());
		}
		
	}
	
	@Override
	@GET
    @Path("/findAllNoChildren")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAllNoChildren() {
	
		try {
			
			List<Product> list = productDAO.findAllNoChildren();

			String str = JsonParser.toJsonExcludeFields(list);

			return Util.sendSucess(Util.ARRAY, str);

		} catch (Exception e) {
			e.printStackTrace();
			return Util.sendInternalServerError(e.getLocalizedMessage());
		}
		
		
	}
	
	@Override
	@GET
    @Path("/{id}/findNoChildren")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findNoChildren(@PathParam("id") Long id) {
	
		try {
			

			if (!productIdValid(id)) {
				return Util.sendBadRequest("product id not valid");
			}
			
			Product product = productDAO.findByIdNoChildren(id);

			String str = JsonParser.toJsonExcludeFields(product);

			return Util.sendSucess(Util.ARRAY, str);

		} catch (Exception e) {
			e.printStackTrace();
			return Util.sendInternalServerError(e.getLocalizedMessage());
		}
		
		
	}
	
	@Override
	@GET
    @Path("/findAllWithChildren")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findAllWithChildren( @QueryParam("loadimage") Boolean loadimage,@QueryParam("loadchildren") Boolean loadchildren) {
		
		Session session=null;
		try{
			    session = HibernateUtil.getSessionFactory().openSession();
			    
				List<Product> list = productDAO.findAll(session);
				
				for (Product product : list) {
	
					Optional<Boolean> loadImg = Optional.ofNullable(loadimage);
	
					if (loadImg.isPresent() && loadImg.get()) {
						Hibernate.initialize(product.getImages());
					} else {
						product.setImages(null);
					}
	
					Optional<Boolean> loadSubordinates = Optional.ofNullable(loadchildren);
	
					if (loadSubordinates.isPresent() && loadSubordinates.get()) {
						Hibernate.initialize(product.getSubordinates());
					} else {
						product.setSubordinates(null);
					}
				}
				
				String str = JsonParser.toJsonExcludeFields(list);

				return Util.sendSucess(Util.ARRAY, str);
				
		} catch (Exception e) {
			e.printStackTrace();
		 
			return Util.sendInternalServerError(e.getLocalizedMessage());
		
		}finally{
			
			if(session!=null){
			   session.close();
			}
		}

		
	}
	
	@Override
	@GET
    @Path("/{id}/findWithChildren")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findWithChildren(@PathParam("id") Long id, @QueryParam("loadimage") Boolean loadimage,@QueryParam("loadchildren") Boolean loadchildren) {
		
		
		Session session = null;
		try {
			
			if (!productIdValid(id)) {
				return Util.sendBadRequest("product id not valid");
			}
	
			
	
			session = HibernateUtil.getSessionFactory().openSession();

			Product product = productDAO.findByID(session, id);

			Optional<Boolean> loadImg = Optional.ofNullable(loadimage);

			if (loadImg.isPresent() && loadImg.get()) {
				Hibernate.initialize(product.getImages());
			} else {
				product.setImages(null);
			}

			Optional<Boolean> loadSubordinates = Optional.ofNullable(loadchildren);

			if (loadSubordinates.isPresent() && loadSubordinates.get()) {
				Hibernate.initialize(product.getSubordinates());
			} else {
				product.setSubordinates(null);
			}

			String str = JsonParser.toJsonExcludeFields(product);

			return Util.sendSucess(Util.PRODUCT, str);

		} catch (Exception e) {
			e.printStackTrace();

			return Util.sendInternalServerError(e.getLocalizedMessage());

		} finally {

			if (session != null) {
				session.close();
			}
		}

		
	}

	@Override
	@GET
    @Path("/{id}/images")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findImages(@PathParam("id") Long id){

		if (!productIdValid(id)) {
			return Util.sendBadRequest("product id not valid");
		}

		Session session = null;

		try {

			session = HibernateUtil.getSessionFactory().openSession();

			Product product = productDAO.findByID(session, id);

			Hibernate.initialize(product.getImages());

			String str = JsonParser.toJsonExcludeFields(product.getImages());

			return Util.sendSucess(Util.ARRAY, str);

		} catch (Exception e) {
			e.printStackTrace();

			return Util.sendInternalServerError(e.getLocalizedMessage());

		} finally {

			if (session != null) {
				session.close();
			}
		}

	}
	
	@Override
	@GET
    @Path("/{id}/children")
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response findChildren(@PathParam("id") Long id){

		if (!productIdValid(id)) {
			return Util.sendBadRequest("product id not valid");
		}

		Session session = null;

		try {

			session = HibernateUtil.getSessionFactory().openSession();

			Product product = productDAO.findByID(session, id);

			Hibernate.initialize(product.getSubordinates());

			String str = JsonParser.toJsonExcludeFields(product.getSubordinates());

			return Util.sendSucess(Util.ARRAY, str);

		} catch (Exception e) {
			e.printStackTrace();

			return Util.sendInternalServerError(e.getLocalizedMessage());

		} finally {

			if (session != null) {
				session.close();
			}
		}

	}


	@Override
	@POST
    @Path("/{id}/image")
    @Produces(MediaType.APPLICATION_JSON)
	public Response createImage(@PathParam("id") Long id,String body) {
		
		try {
			
			
			if (!productIdValid(id)) {
				return Util.sendBadRequest("product id not valid");
			}

			Product product = productDAO.findByID(id);

			Image image = (Image) JsonParser.fromJson(body, Image.class);

			image.setProduct(product);

			imageDAO.persist(image);

			return Util.sendSucess(Util.ID, image.getId().toString());
			
		} catch (Exception e) {
			e.printStackTrace();

			return Util.sendInternalServerError(e.getLocalizedMessage());

		}
		 
		 
	}
	
	@Override
	@PUT
    @Path("/{id}/image/{idImage}")
    @Produces(MediaType.APPLICATION_JSON)
	public Response updateImage(@PathParam("id") Long id,@PathParam("idImage") Long idImage,String body) {
		
		
		try {

			if (!productIdValid(id)) {
				return Util.sendBadRequest("product id not valid");
			}

			if (!imagetIdValid(idImage)) {
				return Util.sendBadRequest("Image id not valid");
			}

			Product product = productDAO.findByID(id);
			Image image = imageDAO.findByID(idImage);

			Image img = (Image) JsonParser.fromJson(body, Image.class);

			image.setType(img.getType());
			image.setProduct(product);

			imageDAO.merge(image);
			
			return Util.sendSucess(Util.ID, image.getId().toString());

		} catch (Exception e) {
			e.printStackTrace();

			return Util.sendInternalServerError(e.getLocalizedMessage());

		}
		
		
		
	}
	

	@Override
	@DELETE
    @Path("/{id}/image/{idImage}")
    @Produces(MediaType.APPLICATION_JSON)
	public Response deleteImage(@PathParam("id") Long id,@PathParam("idImage") Long idImage,String body) {
		
		try {
			
			if (!productIdValid(id)) {
				return Util.sendBadRequest("product id not valid");
			}

			if (!imagetIdValid(idImage)) {
				return Util.sendBadRequest("Image id not valid");
			}

			Image image = imageDAO.findByID(idImage);

			if (!image.getProduct().getId().equals(id)) {

				return Util.sendBadRequest("There is no relations between Image and Product");
			}
			
			
			imageDAO.delete(image);
			
			return Util.sendSucess(Util.ID, image.getId().toString());
	
		} catch (Exception e) {
			e.printStackTrace();

			return Util.sendInternalServerError(e.getLocalizedMessage());

		}
		
	}
	
	/**
	 * =======================================================================================
	 * **/
	
	private boolean productIdValid(Long id){
		
		Optional<Long> productId = Optional.ofNullable(id);
		
		if(!productId.isPresent()){
			return false;
		}	
		
		 Optional<Product> product = Optional.ofNullable(productDAO.findByID(productId.get()));
			
		 return product.isPresent();
	 
	}
	
	private boolean imagetIdValid(Long id){
		
		Optional<Long> ImagetId = Optional.ofNullable(id);
		
		if(!ImagetId.isPresent()){
			return false;
		}	
		
		 Optional<Image> image = Optional.ofNullable(imageDAO.findByID(ImagetId.get()));
			
		 return image.isPresent();
	 
	}
	
	private void setImagesProduct( Product product) throws Exception {

		Optional<Set<Image>> imgs = Optional.ofNullable(product.getImages());

		if (imgs.isPresent()) {

			Set<Image> listImg = imgs.get();

			for (Image img : listImg) {

				img.setProduct(product);


			}

		}

	}

	private void setChildrenProductsParent(Product product) throws Exception {

		Optional<Set<Product>> products = Optional.ofNullable(product.getSubordinates());

		if (products.isPresent()) {
			Set<Product> listProduct = products.get();
			for (Product p : listProduct) {

				p.setParent(product);
			

			}
		}
	}	
	
}
