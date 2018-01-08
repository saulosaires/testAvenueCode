package rest;

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

public interface ProductService {

	Response create(String body);

	Response update(Long id, String body);

	Response delete(Long id);

	Response findAllNoChildren();

	Response findNoChildren(Long id);

	Response findAllWithChildren(Boolean loadimage, Boolean loadchildren);

	Response findWithChildren(Long id, Boolean loadimage, Boolean loadchildren);

	Response findImages(Long id);

	Response findChildren(Long id);

	/**
	 * =======================IMAGE ENDPOINTS ===============================================
	 * **/

	Response createImage(Long id, String body);

	Response updateImage(Long id, Long idImage, String body);

	Response deleteImage(Long id, Long idImage, String body);

}