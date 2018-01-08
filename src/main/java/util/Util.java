package util;

import java.util.Optional;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class Util {

	public static final String STATUS = "status";
	public static final String ERROR = "error";
	public static final String SUCCESS = "success";
	public static final String MSG = "msg";
	public static final String ID = "ID";
	public static final String ARRAY = "array";
	public static final String USER = "user";
	public static final String PRODUCT = "product";
	
	public static boolean emptyParam(String param) {

		Optional<String> op = Optional.ofNullable(param);
		
		 return !op.isPresent();
	}

	public static Response sendSucess() {

		return Response.ok().build();

	}

	public static Response sendSucess(String property, String value) {

		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();

		if (!emptyParam(property) && !emptyParam(value))
			jsonBuilder.add(property, value);

		return Response.ok(jsonBuilder.build()).build();

	}

	public static Response sendBadRequest(String msg) {

		JsonObject json =Json.createObjectBuilder()
							 .add(STATUS, ERROR)
							 .add(MSG, msg).build();
 

		return Response.status(Status.BAD_REQUEST).entity(json).build();

	}

	public static Response sendInternalServerError(String msg) {

		JsonObject json =Json.createObjectBuilder()
				 .add(STATUS, ERROR)
				 .add(MSG, msg).build();


		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(json).build();

	}

}
