
- How to compile and run the application with an example for each call.
	to create the war file just type: "mvn package"
	to run the app just type: "mvn jetty:run"

endpoints sample:

Create Product:
@POST
http://localhost:8080/webapi/product/

body:
{
	
	"name":"product_1",
	"description":"product_1_description",

	"images":[
		    	{"type":"type_1_img"},
		    	{"type":"type_2_img"}
		     ],
    "subordinates":[
    	         {"name":"sub_1_product_1","description":"sub_1_product_1_description_sub"},
    			 {"name":"sub_2_product_1","description":"sub_2_product_1_description_sub"}
    		   ]	
	
}


Update Product:
@PUT
http://localhost:8080/webapi/product/{id}

body:
	{
		"name":"name_2",
		"description":"description_2"
	}

	
Delete Product:
@DELETE
http://localhost:8080/webapi/product/{id}	
	
	
Get all products excluding relationships 	
@GET http://localhost:8080/webapi/product/findAllNoChildren		
	
Get one product excluding relationships
@GET http://localhost:8080/webapi/product/{id}/findNoChildren		
	
Get all products including specified relationships 
@GET http://localhost:8080/webapi/product/findAllWithChildren?loadimage=true&loadchildren=true	

Get one product  including specified relationships
@GET http://localhost:8080/webapi/product/findWithChildren?loadimage=true&loadchildren=true	

Get set of images for specific product	
@GET http://localhost:8080/webapi/product/{id}/images	

Get set of child products for specific product 
@GET http://localhost:8080/webapi/product/{id}/children	

	
Create Image:
@POST
http://localhost:8080/webapi/product/{id}/image	
	
{
	
	"type":"type_created"
}	
	
Update Image:
@PUT
http://localhost:8080/webapi/product/{id}/image/{idImage}
	
{
	
	"type":"type_created_updated"
}	
	
DELETE Image:
@DELETE
http://localhost:8080/webapi/product/{id}/image/{idImage}	
 
	
- How to run the suite of automated tests.
        just run: "mvn test"

- Mention anything that was asked but not delivered and why, and any additional comments.

        *I just did a few simple tests, to improve it would take a parse on the return and check field by field what was returned
         * This test could have far fewer endpoints and continue at the same level of difficulty