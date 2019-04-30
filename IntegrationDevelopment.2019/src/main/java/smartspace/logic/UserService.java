package smartspace.logic;

import java.util.List;

import smartspace.data.UserEntity;

public interface UserService {
	
	public UserEntity writeUser(UserEntity userEntity);

	public List<UserEntity> getUsers(int size, int page);
	
	public List<UserEntity> getUsers(String sortBy, int size, int page);

	public List<UserEntity> getUsersByPattern(String pattern, String sortBy, int size, int page);

}