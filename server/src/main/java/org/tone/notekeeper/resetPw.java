package org.tone.notekeeper;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONObject;

import at.favre.lib.crypto.bcrypt.BCrypt;

import daos.UserDao;
import daos.UserDaoImpl;

import models.User;

// Will map the resource to the URL todos
@Path("/resetPw")
public class resetPw {
	

	// Allows to insert contextual objects into the class,
    // e.g. ServletContext, Request, Response, UriInfo
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    
    //JSON Input
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response consumeJSON(String Json) {
    	
    	System.out.println("Starting JSON Endpoint!");
    	
    	// System.out.println(auth);
    	
    	JSONObject jsonObject = new JSONObject(Json);
    	
        return ResetPassword(jsonObject.getString("key"), jsonObject.getString("email"), jsonObject.getString("password"));
    }
	
	
    //FROM Input
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response consumeFORM(@FormParam("key") String resetKey,
    @FormParam("email") String email, @FormParam("password") String newPassword) throws IOException {
    	
    	System.out.println("Starting FORM Endpoint!");
    	
    	return ResetPassword(resetKey, email, newPassword);
        
    }
    
    
    
    private Response ResetPassword (String resetKey, String email, String newPassword) {
    	
    	System.out.println("Reset Password!");
    	
    	 try {
    	
    	UserDao userDao = new UserDaoImpl();
    	
    	User user = userDao.getUser(email);
    	
    		 System.out.println(user.getResetKey() != null);
    		 System.out.println(user.getResetKey().equals(resetKey));
    	
    	if(user.getResetKey() != null && user.getResetKey().equals(resetKey)) {
    		
    			System.out.println("key validated");
    		
                	 String bcryptHashString = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
                	 
                	 System.out.println(bcryptHashString);
                	 
                	 Boolean DaoRes = userDao.resetPassword(user, bcryptHashString);
                	 
                	 System.out.println(DaoRes);
                	 
                	 if(DaoRes == true) {
             			String res = "{\"status\":\"success\", \"msg\":\"Password sucessfully reset!\"}";
                 		
                 		return Response
                 			      .status(Response.Status.OK)
                 			      .header("Access-Control-Allow-Origin", "*").entity(res)
                 			      .type(MediaType.APPLICATION_JSON)
                 			      .build();
             		} else {
             			String res = "{\"status\":\"failed\", \"msg\":\"Server Error.\"}";
                 		
                 		return Response
                 			      .status(Response.Status.INTERNAL_SERVER_ERROR)
                 			      .header("Access-Control-Allow-Origin", "*").entity(res)
                 			      .type(MediaType.APPLICATION_JSON)
                 			      .build();
             		}
                 
                 } else {
                	String res = "{\"status\":\"failed\", \"msg\":\"Invalid Reset Key!\"}";
              		
              		return Response
              			      .status(Response.Status.OK)
              			      .header("Access-Control-Allow-Origin", "*").entity(res)
              			      .type(MediaType.APPLICATION_JSON)
              			      .build();
                 }
       
    	
    } catch (Throwable t) {
    	String res = "{\"status\":\"failed\", \"msg\":\"Invalid Reset Key!\"}";
  		
  		return Response
  			      .status(Response.Status.OK)
  			      .header("Access-Control-Allow-Origin", "*").entity(res)
  			      .type(MediaType.APPLICATION_JSON)
  			      .build();
    } 
    }

}
