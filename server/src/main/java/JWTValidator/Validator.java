package JWTValidator;

import org.tone.notekeeper.ServerConfig;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class Validator {
	
	public static JWTObject validate (String jwt) {
		try {
    	    Algorithm algorithm = Algorithm.HMAC256(ServerConfig.jwtSecret);
    	    JWTVerifier verifier = JWT.require(algorithm)
    	        .withIssuer(ServerConfig.jwtIssuer)
    	        .build(); //Reusable verifier instance
    	    
    	    DecodedJWT decoded = verifier.verify(jwt);
    	    
    	    System.out.println(decoded);
    	    
    	    Claim userIdClaim = decoded.getClaim("user_id");
    	    Claim emailClaim = decoded.getClaim("email");
    	    
    	    Integer userId = userIdClaim.asInt();
    	    String email = emailClaim.asString();
    	    
    	    JWTObject jwtObj = new JWTObject(true, userId, email);
    	    
    	    return jwtObj;
    	    
    	    
    	} catch (JWTVerificationException exception){
    	    //Invalid signature/claims
    		JWTObject jwtObj = new JWTObject(false);
    	    
    	    return jwtObj;
    	}
	}

}
