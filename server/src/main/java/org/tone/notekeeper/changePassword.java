package org.tone.notekeeper;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.json.JSONObject;

import JWTValidator.JWTObject;
import JWTValidator.Validator;
import at.favre.lib.crypto.bcrypt.BCrypt;
import daos.UserDao;
import daos.UserDaoImpl;
import models.User;

//Will map the resource to the URL todos
@Path("/changePassword")
public class changePassword {
	
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
    public Response consumeJSON(@HeaderParam("authorization") String auth, String Json) {
    	
    	System.out.println("Starting JSON Endpoint!");
    	
    	// System.out.println(auth);
    	
    	JSONObject jsonObject = new JSONObject(Json);
    	
        return ChangePassword(auth, jsonObject.getString("oldPassword"), jsonObject.getString("newPassword"));
    }
	
	
    //FROM Input
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response consumeFORM(@HeaderParam("authorization") String auth,
    @FormParam("oldPassword") String oldPassword, @FormParam("newPassword") String newPassword) throws IOException {
    	
    	System.out.println("Starting FORM Endpoint!");
    	
    	return ChangePassword(auth, oldPassword, newPassword);
        
    }
    
    
    
    private Response ChangePassword (String jwt, String oldPassword, String newPassword) {
    	
    	System.out.println("Change Password!");
    	
    	JWTObject validateJwt = Validator.validate(jwt);
    	
    	UserDao userDao = new UserDaoImpl();
    	
    	
    	 try {
    	
    	if(validateJwt.getIsValid()) {
    		
    		 User user = userDao.getUser(validateJwt.getEmail());
    	     	
        	 if (user != null) {
        		 BCrypt.Result result = BCrypt.verifyer().verify(oldPassword.toCharArray(), user.getPassword() /*BCrypt Hash*/);
                 
                 if(result.verified == true) {
                	 
                	 String bcryptHashString = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray());
                	 
                	 Boolean DaoRes = userDao.updatePassword(user, bcryptHashString);
                	 
                	 if(DaoRes == true) {
             			String res = "{\"status\":\"success\", \"msg\":\"Password sucessfully changed!\"}";
                 		
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
                	String res = "{\"status\":\"failed\", \"msg\":\"Incorrect Password!\"}";
              		
              		return Response
              			      .status(Response.Status.OK)
              			      .header("Access-Control-Allow-Origin", "*").entity(res)
              			      .type(MediaType.APPLICATION_JSON)
              			      .build();
                 }
        	 } else {
        		String res = "{\"status\":\"failed\", \"msg\":\"User Not Found!\"}";
          		
          		return Response
          			      .status(Response.Status.OK)
          			      .header("Access-Control-Allow-Origin", "*").entity(res)
          			      .type(MediaType.APPLICATION_JSON)
          			      .build();
        	 }
        	 
    		
    	} else {
    		
    		String res = "{\"status\":\"failed\", \"msg\":\"Invalid Token!\"}";
    		
    		return Response
    			      .status(Response.Status.UNAUTHORIZED)
    			      .header("Access-Control-Allow-Origin", "*").entity(res)
    			      .type(MediaType.APPLICATION_JSON)
    			      .build();
    		
    	}
    	
    } catch (Throwable t) {
    	String res = "{\"status\":\"failed\", \"msg\":\"Server Error.\"}";
		
		return Response
			      .status(Response.Status.INTERNAL_SERVER_ERROR)
			      .header("Access-Control-Allow-Origin", "*").entity(res)
			      .type(MediaType.APPLICATION_JSON)
			      .build();
    } 
    }

}
