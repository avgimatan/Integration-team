package smartspace.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.AdvancedUserDao;
import smartspace.data.UserEntity;

@Service
public class UserServiceImpl implements UserService {
	private AdvancedUserDao<String> users;
	
	@Autowired	
	public UserServiceImpl(AdvancedUserDao<String> users) {
		this.users = users;
	}
	
	@Transactional
	@Override
	public UserEntity writeUser(UserEntity userEntity) {
		if (validate(userEntity)) {
			return this.users
				.create(userEntity);
		}else {
			throw new RuntimeException("invalid user input");
		}
	}
	
	
	private boolean validate(UserEntity userEntity) {
		//TODO LONG
		return userEntity.getUserEmail() != null &&
				userEntity.getUsername() != null;// &&
			//	userEntity.getRole() != null
			//;//	&&
				//Long.parseUnsignedLong(String.valueOf(userEntity.getPoints()).isEmpty());
	}

	@Override
	public List<UserEntity> getUsers(int size, int page) {
		return this.users
				.readAll(size, page);
	}
	
	@Override
	public List<UserEntity> getUsers(String sortBy, int size, int page) {
		return this.users
				.readAll(sortBy, size, page);
	}

	@Override
	public List<UserEntity> getUsersByPattern(String pattern, String sortBy, int size, int page) {
		switch (sortBy) {
		case "key":
		case "username":
		case "avatar":
		case "role":
		case "points":
		break;
		
		default:
			throw new RuntimeException("illegal sortBy value: " + sortBy);
//			break;
		}
		return this.users
				.readUsersByUserEmailPattern(
						pattern,
						sortBy,
						size, page);
	}

}
