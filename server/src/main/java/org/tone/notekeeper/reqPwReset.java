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

import daos.UserDao;
import daos.UserDaoImpl;

import models.User;
import smtp_mailer.TLSEmail;

import pw_generator.PasswordGenerator;

// Will map the resource to the URL todos
@Path("/reqPwReset")
public class reqPwReset {
	
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
    public Response consumeJSON( String Json) {
    	
    	System.out.println("Starting JSON Endpoint!");
    	
    	// System.out.println(auth);
    	
    	JSONObject jsonObject = new JSONObject(Json);
    	
        return ReqPwReset(jsonObject.getString("email"));
    }
	
	
    //FROM Input
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response consumeFORM(@FormParam("email") String email) throws IOException {
    	
    	System.out.println("Starting FORM Endpoint!");
    	
        return ReqPwReset(email);
        
    }
    
    
    
    private Response ReqPwReset (String email) {
    	
    	System.out.println("Request Password Reset!");
    	
    	String resetKey = PasswordGenerator.generateRandomPassword(32);
        
        String subject = "Reset Your Password!";
        
        String message = "\n\nPlease use the link below to reset your password:" + 
        "\nhttps://java-notekeeper.bithatchery.com/#/resetpassword?key=" + resetKey +
        "&email=" + email;
        
        UserDao userDao = new UserDaoImpl();
        
        User userExists = userDao.getUser(email);
        
        
        try {
        	
        	 if (userExists != null) {
        		 
        	Boolean status = userDao.updateResetKey(email, resetKey);
            
            System.out.println(status);
            
            if (status == true) {
            	
            	Boolean result = TLSEmail.sendMail(email, subject, message);
            	
            	
            	if(result == true) {
            		
            		String res = "{\"status\":\"success\", \"msg\":\"Password Reset Request Successful! Please check your email.\"}";
            		
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
            	String res = "{\"status\":\"failed\", \"msg\":\"Server Error.\"}";
        		
        		return Response
        			      .status(Response.Status.INTERNAL_SERVER_ERROR)
        			      .header("Access-Control-Allow-Origin", "*").entity(res)
        			      .type(MediaType.APPLICATION_JSON)
        			      .build();
            }
        	 } else {
        		 String res = "{\"status\":\"failed\", \"msg\":\"Account Not Found!\"}";
         		
         		return Response
         			      .status(Response.Status.OK)
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
