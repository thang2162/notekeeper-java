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
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import daos.UserDao;
import daos.UserDaoImpl;

import java.time.Instant;
import java.util.Date;

import models.User;

//Will map the resource to the URL todos
@Path("/userLogin")
public class userLogin {
	
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
    public Response consumeJSON( /* @HeaderParam("authorization") String auth, */ String Json ) {
    	
    	System.out.println("Starting JSON Endpoint!");
    	
    	// System.out.println(auth);
    	
    	JSONObject jsonObject = new JSONObject(Json);
    	
        return LoginUser(jsonObject.getString("email"), jsonObject.getString("password"));
    }
	
	
    //FROM Input
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response consumeFORM(@FormParam("email") String email,
            @FormParam("password") String password) throws IOException {
    	
    	System.out.println("Starting FORM Endpoint!");
    	
        return LoginUser(email, password);
        
    }
    
    
    
    private Response LoginUser (String email, String password) {
    	
    	System.out.println("User Login!");
    	
    	Date curDate = new Date(Instant.now().toEpochMilli());
    	
    	Date expDate = new Date(Instant.now().toEpochMilli() + ServerConfig.jwtExpIn);
    	
        UserDao userDao = new UserDaoImpl();
        
        User user = userDao.getUser(email);
        	
        	 if (user != null) {
        		 
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword() /*BCrypt Hash*/);
            
            if(result.verified == true) {
            	
            	try {
            	    Algorithm algorithm = Algorithm.HMAC256(ServerConfig.jwtSecret);
            	    String token = JWT.create()
            	        .withIssuer(ServerConfig.jwtIssuer)
            	        .withIssuedAt(curDate)
            	        .withExpiresAt(expDate)
            	        .withClaim("user_id", user.getId())
            	        .withClaim("email", user.getEmail())
            	        .sign(algorithm);
            	    
            	    String res = "{\"status\":\"success\", \"msg\":\"You're Logged In!\", \"jwt\":\"" + token + "\"}";
            		
            		return Response
            			      .status(Response.Status.OK)
            			      .header("Access-Control-Allow-Origin", "*").entity(res)
            			      .type(MediaType.APPLICATION_JSON)
            			      .build();
            	    
            	} catch (JWTCreationException exception){
            		
            		String res = "{\"status\":\"failed\", \"msg\":\"Server Error.\"}";
            	    //Invalid Signing configuration / Couldn't convert Claims.
            		return Response
          			      .status(Response.Status.INTERNAL_SERVER_ERROR)
          			      .header("Access-Control-Allow-Origin", "*").entity(res)
          			      .type(MediaType.APPLICATION_JSON)
          			      .build();
            	}
            	
            } else {
            	String res = "{\"status\":\"failed\", \"msg\":\"Wrong Email or Password!\"}";
        		
        		return Response
        			      .status(Response.Status.OK)
        			      .header("Access-Control-Allow-Origin", "*").entity(res)
        			      .type(MediaType.APPLICATION_JSON)
        			      .build();
            }
        } else {
        	String res = "{\"status\":\"failed\", \"msg\":\"No Account Found!\"}";
    		
    		return Response
    			      .status(Response.Status.OK)
    			      .header("Access-Control-Allow-Origin", "*").entity(res)
    			      .type(MediaType.APPLICATION_JSON)
    			      .build();
          
        }
    }

}
