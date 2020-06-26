package org.tone.notekeeper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import daos.NoteDao;
import daos.NoteDaoImpl;

import java.util.List;

import models.Note;

import javax.ws.rs.HeaderParam;

import JWTValidator.*;

@Path("/getNotes")
public class getNotes {
	
	// Allows to insert contextual objects into the class,
    // e.g. ServletContext, Request, Response, UriInfo
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response consumeFORM(@HeaderParam("authorization") String auth){
    	
    	System.out.println("Starting GET Endpoint!");
    	
    	return GetNotes(auth);
        
    }
    
    
    
    private Response GetNotes (String jwt) {
    	
    	try {
    	
    	System.out.println("Get Notes!");
    	
    	JWTObject validateJwt = Validator.validate(jwt);
    	
    	 NoteDao noteDao = new NoteDaoImpl();
    	 
    	 System.out.println(validateJwt.getIsValid());
    	
    	if(validateJwt.getIsValid()) {
    		
    		System.out.println(validateJwt.getEmail());
    		
    		List<Note> DaoRes = noteDao.getAllNotes(validateJwt.getEmail());
    		
    		if(DaoRes != null) {
        		
        		return Response
        			      .status(Response.Status.OK)
        			      .entity(DaoRes)
        			      .type(MediaType.APPLICATION_JSON)
        			      .build();
    		} else {
        		
        		return Response
        			      .status(Response.Status.OK)
        			      .entity("[]")
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
