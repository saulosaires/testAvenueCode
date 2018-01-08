package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
 

public class JsonParser {

	static Gson gsonExcludeFields = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	static Gson gson= new GsonBuilder().create();
	
	public static Object fromJsonExcludeFields(String json, Class<?> c){
	
		return gsonExcludeFields.fromJson(json,c);
		
	}
	
	public static String toJsonExcludeFields(Object obj){
		 
		return gsonExcludeFields.toJson(obj);
		
	}
	
	public static Object fromJson(String json, Class<?> c){
		
		return gson.fromJson(json,c);
		
	}
	
	public static String toJson(Object obj){
		 
		return gson.toJson(obj);
		
	}
	
}
