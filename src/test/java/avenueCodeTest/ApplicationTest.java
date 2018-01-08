package avenueCodeTest;

 
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Image;
import model.Product;
import persistence.ImageDAO;
import persistence.ImageDAOImpl;
import persistence.ProductDAO;
import persistence.ProductDAOImpl;
import rest.ProductServiceImpl;
import util.HibernateUtil;
import util.Util;
 

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationTest extends JerseyTest{

	 private  ProductDAO productDAO;
	 private  ImageDAO   imageDAO;
	 
	 {   //MELHOR USAR CDI, PARA COMO É SO UM EXERCICIO SERVE AO PROPOSITO !
		 productDAO = new ProductDAOImpl();
		 imageDAO   = new ImageDAOImpl();
	 }
	
	static Product productTest;
	static Long idImage;
	
	@Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(ProductServiceImpl.class);
    }
 
	
	@BeforeClass
	public  static void beforeTests(){
		
		productTest = new Product();
		productTest.setName("Name");
		productTest.setDescription("description");
		
		Image img1 = new Image("type1");
		Image img2 = new Image("type2");
		
		productTest.getImages().add(img1);
		productTest.getImages().add(img2);
		
		Product subProduct1 = new Product();
				subProduct1.setName("Name_subProduct1");
				subProduct1.setDescription("description_subProduct1");
				
		Product subProduct2 = new Product();
				subProduct2.setName("Name_subProduct2");
				subProduct2.setDescription("description_subProduct2");
				
		
		productTest.getSubordinates().add(subProduct1);		
		productTest.getSubordinates().add(subProduct2);				
	}
	
	
	@Test
	public void test1CreateProduct(){
		
		  Response output = target("/product/")
			                .request()
			                .post(Entity.entity(productTest, MediaType.APPLICATION_JSON));
		  
		  assertEquals("Should return status 200", 200, output.getStatus());
	      assertNotNull("Should return the ID", output.getEntity());
	      
	      String json=output.readEntity(String.class);
	      
	      Long id =parseJsonWithId(json);
 
	      assertNotNull(id);
	      
	      productTest.setId(id);
	      
	}
	
	@Test
	public void test2UpdateProduct(){
 
			productTest.setName("Name_updated");
			productTest.setDescription("description_updated");
 		
		
		  Response output = target("/product/"+productTest.getId())
	                .request()
	                .put(Entity.entity(productTest, MediaType.APPLICATION_JSON));

			assertEquals("Should return status 200", 200, output.getStatus());
			assertNotNull("Should return the ID updated", output.getEntity());
			
			String json=output.readEntity(String.class);
			
			Long id =parseJsonWithId(json);
			
			Product prod= productDAO.findByID(id);
			  
			assertEquals(productTest.getId(),id);
			assertEquals(prod.getName(),	   productTest.getName());
			assertEquals(prod.getDescription(),productTest.getDescription());
 
		
	}

	@Test
	public void test3FindAllNoChildren(){
 
		
		  Response output = target("/product/findAllNoChildren").request().get();

			assertEquals("Should return status 200", 200, output.getStatus());
			assertNotNull("Should return the ID updated", output.getEntity());
			
			String json=output.readEntity(String.class);
  
			 
			assertNotNull(json);
			
	}
	
	@Test
	public void test4FindNoChildren(){
 
		
		  Response output = target("/product/"+productTest.getId()+"/findNoChildren").request().get();

			assertEquals("Should return status 200", 200, output.getStatus());
			assertNotNull("Should return the ID updated", output.getEntity());
			
			String json=output.readEntity(String.class);
  
			 
			assertNotNull(json);
			
	}	 

	@Test
	public void test5FindAllWithChildren(){
 
		
		Response output = target("/product/findAllWithChildren").request().get();

		assertEquals("Should return status 200", 200, output.getStatus());
		assertNotNull("Should return the ID updated", output.getEntity());

		String json = output.readEntity(String.class);

		assertNotNull(json);
			
	}

	@Test
	public void test6FindWithChildren(){
 
		
		Response output = target("/product/"+productTest.getId()+"/findWithChildren").request().get();

		assertEquals("Should return status 200", 200, output.getStatus());
		assertNotNull("Should return the ID updated", output.getEntity());

		String json = output.readEntity(String.class);

		assertNotNull(json);
			
	} 

	@Test
	public void test7FindImages(){
		
		Response output = target("/product/"+productTest.getId()+"/images").request().get();

		assertEquals("Should return status 200", 200, output.getStatus());
		assertNotNull("Should return the ID updated", output.getEntity());

		String json = output.readEntity(String.class);

		assertNotNull(json);
			
	} 
	
	@Test
	public void test8FindChildren(){
		
		Response output = target("/product/"+productTest.getId()+"/children").request().get();

		assertEquals("Should return status 200", 200, output.getStatus());
		assertNotNull("Should return the ID updated", output.getEntity());

		String json = output.readEntity(String.class);

		assertNotNull(json);
			
	}

	@Test
	public void test9Image1Create(){
		
		  Image img = new Image("new_type");
		  
		  Session session = HibernateUtil.getSessionFactory().openSession();
		  
		  Product prod1= productDAO.findByID(session,productTest.getId());
		  Hibernate.initialize(prod1.getImages());
		  int imgSize=prod1.getImages().size();
		  session.close();
		  
		  Response output = target("/product/"+productTest.getId()+"/image")
			                .request()
			                .post(Entity.entity(img, MediaType.APPLICATION_JSON));
		  
		  assertEquals("Should return status 200", 200, output.getStatus());
	      assertNotNull("Should return the ID", output.getEntity());
	      
	      String json=output.readEntity(String.class);
	      
	      idImage =parseJsonWithId(json);
 
	      assertNotNull(idImage);
	      
	      Session session2 = HibernateUtil.getSessionFactory().openSession();
	      Product prod2= productDAO.findByID(session2,productTest.getId());
	      Hibernate.initialize(prod2.getImages());
		  int imgSize2=prod2.getImages().size();	       
		  session2.close();
		  
		  
		  assertEquals(imgSize+1,imgSize2);

		
	}	
	
	@Test
	public void test9Image2Update(){
		
		  Image img = new Image("new_type_updated");
 
		  Response output = target("/product/"+productTest.getId()+"/image/"+idImage)
			                .request()
			                .put(Entity.entity(img, MediaType.APPLICATION_JSON));
		  
		  assertEquals("Should return status 200", 200, output.getStatus());
	      assertNotNull("Should return the ID", output.getEntity());
	      
	      
	      
	     Image img2= imageDAO.findByID(idImage);
		  
		  
		  assertEquals(img.getType(),img2.getType());

		
	}		
	
	@Test
	public void test9Image3Delete(){
		
		   
 
		  Response output = target("/product/"+productTest.getId()+"/image/"+idImage)
			                .request()
			                .delete();
		  
		  assertEquals("Should return status 200", 200, output.getStatus());
	      assertNotNull("Should return the ID", output.getEntity());
	       
	      
	     Image img= imageDAO.findByID(idImage);
		  
		  
		  assertEquals(img,null);

		
	}	
	
	@Test
	public void wtestDeleteProduct(){
		 
		Response output = target("/product/"+productTest.getId())
	                .request()
	                .delete();

		assertEquals("Should return status 200", 200, output.getStatus());
		assertNotNull("Should return the ID deleted ", output.getEntity());
 
		
		Product p = productDAO.findByID(productTest.getId());
		
		assertEquals(p,null);
	 
	}
	
	private Long parseJsonWithId(String json){
 
		      
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(json).getAsJsonObject();

		return obj.get(Util.ID).getAsLong();
	}
	
	
}
