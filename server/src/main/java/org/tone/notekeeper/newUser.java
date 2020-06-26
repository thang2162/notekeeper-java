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

import java.sql.Timestamp;
import java.time.Instant;

import models.User;
import smtp_mailer.TLSEmail;

// Will map the resource to the URL todos
@Path("/newUser")
public class newUser {
	
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
    public Response consumeJSON( String Json ) {
    	
    	System.out.println("Starting JSON Endpoint!");
    	
    	// System.out.println(auth);
    	
    	JSONObject jsonObject = new JSONObject(Json);
    	
        return NewUser(jsonObject.getString("email"), jsonObject.getString("password"));
    }
	
	
    //FROM Input
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response consumeFORM(@FormParam("email") String email,
            @FormParam("password") String password) throws IOException {
    	
    	System.out.println("Starting FORM Endpoint!");
    	
        return NewUser(email, password);
        
    }
    
    
    
    private Response NewUser (String email, String password) {
    	
    	System.out.println("New User!");
    	
    	Timestamp curDate = new Timestamp(Instant.now().toEpochMilli());
        
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        
        User user = new User(curDate, email, bcryptHashString);
        
        String subject = "Welcome to NoteKeeper!";
        
        String message = "Hi There, " + "\n\nEmail: " + email + "\nPassword: " + password;
        
        UserDao userDao = new UserDaoImpl();
        
        User userExists = userDao.getUser(email);
        
        
        try {
        	
        	 if (userExists == null) {
             	
             
            Boolean result = TLSEmail.sendMail(email, subject, message);
            
            System.out.println(result);
            
            if (result == true) {
            	
            	System.out.println(user.getCreatedOn());
            	
            	Boolean DaoRes = userDao.newUser(user);
            	
            	if(DaoRes == true) {
            		
            		String res = "{\"status\":\"success\", \"msg\":\"Account successfully created! Please check your email.\"}";
            		
            		return Response
            			      .status(Response.Status.OK)
            			      .header("Access-Control-Allow-Origin", "*").entity(res)
            			      .type(MediaType.APPLICATION_JSON)
            			      .build();
            	} else {
            		String res = "{\"status\":\"failed\", \"msg\":\"Account Already Exists!\"}";
            		
            		return Response
            			      .status(Response.Status.OK)
            			      .header("Access-Control-Allow-Origin", "*").entity(res)
            			      .type(MediaType.APPLICATION_JSON)
            			      .build();
            	}
            } else {
            	String res = "{\"status\":\"failed\", \"msg\":\"Email cannot be verified.\"}";
        		
        		return Response
        			      .status(Response.Status.OK)
        			      .header("Access-Control-Allow-Origin", "*").entity(res)
        			      .type(MediaType.APPLICATION_JSON)
        			      .build();
            }
        	 } else {
        		 String res = "{\"status\":\"failed\", \"msg\":\"Account Already Exists!\"}";
         		
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