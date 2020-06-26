package org.tone.notekeeper;

import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import JWTValidator.JWTObject;
import JWTValidator.Validator;
import daos.*;

@Path("/deleteUser")
public class deleteUser {
	
	// Allows to insert contextual objects into the class,
    // e.g. ServletContext, Request, Response, UriInfo
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    
    //JSON Input
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response consumeJSON(@HeaderParam("authorization") String auth) {
    	
    	System.out.println("Starting JSON Endpoint!");
        return DeleteUser(auth);
    }
    
    
    private Response DeleteUser (String jwt) {
    	
    	System.out.println("Delete User!");
    	
    	JWTObject validateJwt = Validator.validate(jwt);
    	
    	 NoteDao noteDao = new NoteDaoImpl();
    	 UserDao userDao = new UserDaoImpl();
    	 
    	 try {
    	
    	if(validateJwt.getIsValid()) {
    		
    		noteDao.deleteAllNotes(validateJwt.getEmail());
    		userDao.deleteUser(validateJwt.getUserId());
    				
    		String res = "{\"status\":\"success\", \"msg\":\"User accoount sucessfully deleted\"}";
            		
            return Response
            	  .status(Response.Status.OK)
            	  .header("Access-Control-Allow-Origin", "*").entity(res)
            	  .type(MediaType.APPLICATION_JSON)
            	  .build();
    			
    		
    		
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
