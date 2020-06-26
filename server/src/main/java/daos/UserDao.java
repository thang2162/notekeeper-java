package daos;

import models.User;

public interface UserDao {
	public User getUser (String email);
	public Boolean newUser(User user);
	public Boolean updatePassword(User user, String new_password);
	public Boolean deleteUser (Integer id);
	public Boolean updateResetKey (String email, String resetKey);
	public Boolean resetPassword(User user, String new_password);
}
