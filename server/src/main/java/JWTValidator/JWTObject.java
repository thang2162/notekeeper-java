package JWTValidator;

public class JWTObject {
	private Boolean isValid;
	private Integer userId;
	private String email;
	
	public JWTObject (Boolean isValid) {
		this.isValid = isValid;
	}
	
	public JWTObject (Boolean isValid, Integer userId, String email) {
		this.isValid = isValid;
		this.userId = userId;
		this.email = email;
	}
	
	public void setIsValid (Boolean isValid) {
		this.isValid = isValid;
	}
	
	public void setUserId (Integer userId) {
		this.userId = userId;
	}
	
	public void setEmail (String email) {
		this.email = email;
	}
	
	public Boolean getIsValid () {
		return isValid;
	}
	
	public Integer getUserId () {
		return userId;
	}
	
	public String getEmail () {
		return email;
	}
}
