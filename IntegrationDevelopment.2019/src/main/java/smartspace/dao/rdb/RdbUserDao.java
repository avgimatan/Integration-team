package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.UserDao;
import smartspace.data.UserEntity;

@Repository
public class RdbUserDao implements UserDao<String> {

	private UserCrud userCrud;
	private String smartspace;

	@Autowired
	public RdbUserDao(UserCrud userCrud) {
		super();
		this.userCrud = userCrud;
	}

	@Value("${smartspace.name:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	@Override
	@Transactional
	public UserEntity create(UserEntity user) {
		user.setKey(smartspace + "#" + user.getUserEmail());
		user.setUserSmartspace(smartspace);

		// SQL: INSERT
		if (!this.userCrud.existsById(user.getKey())) {
			return this.userCrud.save(user);
		} else {
			throw new RuntimeException("message already exists with key: " + user.getKey());
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Optional<UserEntity> readById(String key) {
		// SQL: SELECT
		return this.userCrud.findById(key);
	}

	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> readAll() {
		List<UserEntity> rv = new ArrayList<>();
		// SQL: SELECT
		this.userCrud.findAll()
			.forEach(rv::add);
		return rv;
	}

	@Override
	@Transactional
	public void update(UserEntity user) {
		UserEntity existing = 
				this.readById(user.getKey())
				.orElseThrow(()->new RuntimeException("no message entity with key: " + user.getKey()));
			
		if (user.getAvatar() != null) {
			existing.setAvatar(user.getAvatar());
		}

		if (user.getRole() != null) {
			existing.setRole(user.getRole());
		}
		
		if (user.getUserEmail()!= null) {
			existing.setUserEmail(user.getUserEmail());
		}
		
		if (user.getUsername() != null) {
			existing.setUsername(user.getUsername());
		}

		// SQL: UPDATE
		this.userCrud.save(existing);
	}

	@Override
	@Transactional
	public void deleteAll() {
		this.userCrud.deleteAll();
	}

}
