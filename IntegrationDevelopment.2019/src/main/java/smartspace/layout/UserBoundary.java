package smartspace.layout;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public class UserBoundary {

	private String userSmartspace;
	private String userEmail;
	private String username;
	private String avatar;
	private String role;
	private long points;
	
	public UserBoundary() {
	}

	public UserBoundary(UserEntity user) {
		this.userSmartspace = user.getUserSmartspace();
		this.userEmail = user.getUserEmail();
		this.username = user.getUsername();
		this.avatar = user.getAvatar();

		if (user.getRole() != null)
			this.role = user.getRole().name();
		else
			this.role = null;
		
		this.points = user.getPoints();
	}

	public String getUserSmartspace() {
		return userSmartspace;
	}

	public void setUserSmartspace(String userSmartspace) {
		this.userSmartspace = userSmartspace;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}
	
	public UserEntity convertToEntity() {
		UserEntity user = new UserEntity();
		user.setKey(this.userSmartspace + "#" + this.userEmail);
		
		user.setAvatar(this.avatar);
		user.setPoints(this.points);
		user.setUserEmail(this.userEmail);
		user.setUserSmartspace(this.userSmartspace);
		user.setUsername(this.username);
		if(this.role != null) {
			user.setRole(UserRole.valueOf(this.role));
		}
		else {
			user.setRole(null);
		}
		
		return user;
	}

	@Override
	public String toString() {
		return "UserBoundary [userSmartspace=" + userSmartspace + ", userEmail=" + userEmail + ", username=" + username
				+ ", avatar=" + avatar + ", role=" + role + ", points=" + points + "]";
	}
	
	

}
