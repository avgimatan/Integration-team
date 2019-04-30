package smartspace.dao;

import java.util.List;

import smartspace.data.UserEntity;

public interface AdvancedUserDao<UserKey> extends UserDao<UserKey> {

	public List<UserEntity> readAll(int size, int page);

	public List<UserEntity> readAll(String sortBy, int size, int page);

	public List<UserEntity> readUsersByUsernamePattern(String username, int size, int page);

	public List<UserEntity> readUsersByUsernamePattern(String username, String sortBy, int size, int page);
	
	public List<UserEntity> readUsersByUserEmailPattern(String userEmail, int size, int page);

	public List<UserEntity> readUsersByUserEmailPattern(String userEmail, String sortBy, int size, int page);
}
