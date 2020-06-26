package org.tone.notekeeper;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

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
import daos.NoteDao;
import daos.NoteDaoImpl;
import models.Note;

//Will map the resource to the URL todos
@Path("/editNote")
public class editNote {
	
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
    	
        return EditNote(auth, jsonObject.getInt("id"), jsonObject.getString("title"), jsonObject.getString("note"));
    }
	
	
    //FROM Input
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response consumeFORM(@HeaderParam("authorization") String auth,
    @FormParam("title") String title, @FormParam("note") String note, @FormParam("id") Integer id) throws IOException {
    	
    	System.out.println("Starting FORM Endpoint!");
    	
    	return EditNote(auth, id, title, note);
        
    }
    
    
    
    private Response EditNote (String jwt, Integer id, String title, String note_txt) {
    	
    	System.out.println("Edit Note!");
    	
    	JWTObject validateJwt = Validator.validate(jwt);
    	
    	 NoteDao noteDao = new NoteDaoImpl();
    	 
    	 try {
    	
    	if(validateJwt.getIsValid()) {
    		
    		Timestamp curDate = new Timestamp(Instant.now().toEpochMilli());
    		
    		Note note = new Note(curDate, validateJwt.getEmail(), title, note_txt, id);
    		
    		Boolean DaoRes = noteDao.updateNote(note);
    		
    		if(DaoRes == true) {
    			String res = "{\"status\":\"success\", \"msg\":\"Note sucessfully edited\"}";
        		
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
