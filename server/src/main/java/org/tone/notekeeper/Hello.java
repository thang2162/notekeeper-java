package org.tone.notekeeper;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import pw_generator.PasswordGenerator;

// Plain old Java Object it does not extend as class or implements
// an interface

// The class registers its methods for the HTTP GET request using the @GET annotation.
// Using the @Produces annotation, it defines that it can deliver several MIME types,
// text, XML and HTML.

// The browser requests per default the HTML MIME type.

//Sets the path to base URL + /hello
@Path("/hello")
public class Hello {

  // This method is called if TEXT_PLAIN is request
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String sayPlainTextHello() {
    return "Hello Jersey";
  }

  // This method is called if XML is request
  @GET
  @Produces(MediaType.TEXT_XML)
  public String sayXMLHello() {
    return "<?xml version=\"1.0\"?>" + "<hello> Hello Jersey" + "</hello>";
  }

  // This method is called if HTML is request
  @GET
  @Produces(MediaType.TEXT_HTML)
  public String sayHtmlHello() {
    return "<html> " + "<title>" + "Hello Jersey" + "</title>"
        + "<body><h1>" + "Hello Jersey" + "</body></h1>" + "</html> ";
  }
  
  @GET
  @Path("/generate_pw/{len}")
  @Produces(MediaType.APPLICATION_JSON)
  public String generatePW (@PathParam("len") Integer len) {
	  
	  return PasswordGenerator.generateRandomPassword(len);
	  
  }
  
//Creates a new user
  @POST
  @Path("/shuffle/")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response new_user(@FormParam("str") String str) throws IOException {
  	
	  String res = "{\"status\":\"success\", \"msg\":\"" + PasswordGenerator.shuffleString(str) + "\"}";
          		
	  return Response
          	 .status(Response.Status.OK)
          	 .entity(res)
          	 .type(MediaType.APPLICATION_JSON)
             .build();
          	
  }

}