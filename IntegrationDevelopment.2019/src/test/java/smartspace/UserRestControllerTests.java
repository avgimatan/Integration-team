package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import smartspace.dao.UserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.layout.UserBoundary;
import smartspace.logic.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class UserRestControllerTests {
	
	private int port;
	private String baseUrl;
	private RestTemplate restTemplate;
	private UserDao<String> userDao;
	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	@Autowired
	public void setUserDao(UserDao<String> userDao) {
		this.userDao = userDao;
	}

	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init(){
		this.baseUrl = "http://localhost:" + port + "/userdemo";
		this.restTemplate = new RestTemplate();
	}

	@After
	public void tearDown() {
		this.userDao
		.deleteAll();
	}

	@Test
	public void testWriteUser() throws Exception{
		// GIVEN the database is clean 
		
		// WHEN I crate new User
		UserBoundary newUser= new UserBoundary();
		
		newUser.setUsername("username");
		newUser.setAvatar("avatar");
		newUser.setPoints(10);
		newUser.setRole(UserRole.PLAYER.name());
		newUser.setUserEmail("jane@gmail.com");
		
		UserBoundary response = this.restTemplate
				.postForObject(
						this.baseUrl, 
						newUser, 
						UserBoundary.class);

		// THEN the database contains 1 user
		// AND the returned user is similar to the user in the database
		assertThat(
				this.userDao.readAll())
		.hasSize(1)
		.usingElementComparatorOnFields("username")
		.containsExactly(response.convertToEntity());

	}

	@Test
	public void testWriteUserAndValidateReturnedKey() throws Exception{
		// GIVEN the database is clean 

		// WHEN I post a new User
		UserBoundary newUser= new UserBoundary();
		newUser.setUsername("username");
		newUser.setAvatar("avatar");
		newUser.setPoints(10);
		newUser.setRole(UserRole.PLAYER.name());
		newUser.setUserEmail("jane@gmail.com");
		
		UserBoundary response = this.restTemplate
				.postForObject(
						this.baseUrl, 
						newUser, 
						UserBoundary.class);
		
		// THEN the returned User json contains a key
		assertThat(
				response.getUserSmartspace()+
				response.getUserEmail())
		.isNotNull()
		.isNotBlank()
		.isNotEmpty();

	}


	@Test
	public void testGetUsersUsingPagination() throws Exception{
		// GIVEN the database contains 38 Users
		int totalSize = 38;
		//@SuppressWarnings("unchecked")//back again to here 
		List<UserEntity> all = 
		IntStream.range(1, totalSize+1 )
			.mapToObj(i->"User@mail" + i)
			.map(name->new UserEntity(
					name, "username", "avatar", UserRole.PLAYER, 10))
			.map(this.userService::writeUser)
			.collect(Collectors.toList());

			List<UserBoundary> lastEight = 
						all
						.stream()
						.skip(30)
						.map(UserBoundary::new)
						.collect(Collectors.toList());

				// WHEN I getUser using page #3 of size 10
				int size = 10;
				int page = 3;
				UserBoundary[] results = 
						this.restTemplate
						.getForObject(
								this.baseUrl + "?size={size}&page={page}", 
								UserBoundary[].class, 
								size, page);


				// THEN the response contains 8 user
				assertThat(results)
				.usingElementComparatorOnFields("userEmail")
				.containsExactlyElementsOf(lastEight);
	}


	@Test
	public void testGetUsersUsingPaginationWithNoResult() throws Exception{
		// GIVEN the database contains 30 users
		int totalSize = 30;

		IntStream.range(1, totalSize + 1)
		.mapToObj(i->"user@mail" + i)
		.map(name->new UserEntity(name, "username", "avatar", UserRole.PLAYER, 10))
		.map(this.userService::writeUser)
		.collect(Collectors.toList());


		// WHEN I getUser using page #3 of size 10
		int size = 10;
		int page = 3;
		UserBoundary[] results = 
				this.restTemplate
				.getForObject(
						this.baseUrl + "?size={size}&page={page}", 
						UserBoundary[].class, 
						size, page);


		// THEN the response contains no users
		assertThat(results)
		.isEmpty();
	}

	@Test
	public void testGetUsersUsingPaginationOfFirstPage() throws Exception{
		// GIVEN the database contains 38 users
		int totalSize = 38;

		IntStream.range(1, totalSize + 1)
		.mapToObj(i->"user@mail" + i)
		.map(name->new UserEntity(name, "username", "avatar", UserRole.PLAYER, 10))
		.map(this.userService::writeUser)
		.collect(Collectors.toList());

		// WHEN I getUser using page #0 of size 100
		int size = 100;
		int page = 0;
		UserBoundary[] results = 
				this.restTemplate
				.getForObject(
						this.baseUrl + "?size={size}&page={page}", 
						UserBoundary[].class, 
						size, page);

		// THEN the response contains 38 User
		assertThat(results)
		.hasSize(totalSize);
	}

	@Test
	public void testGetUsersWithPattern() throws Exception{
		// GIVEN the database contains 3 user with the name "abc"
		// AND the database contains 2 more user without the name "abc"

		String pattern = "abc";

		List<UserEntity> all = 
				Stream.of("abc@mail", "abxyzabc@mail", "xyabczz@mail",
						"urjmdl@mail", "ababbac@mail")
				.map(name->new UserEntity(name, "username", "avatar", UserRole.PLAYER, 10))
				.map(this.userService::writeUser)
				.collect(Collectors.toList());

		List<UserBoundary> userWithPattern = 
				all
				.stream()
				.filter(act->act.getUserEmail().contains(pattern))
				.map(UserBoundary::new)
				.collect(Collectors.toList());

		// WHEN I getUser using pattern using page #0 of size 100 with pattern "abc"
		int size = 100;
		int page = 0;
		UserBoundary[] results = 
			this.restTemplate
				.getForObject(
						this.baseUrl + "/{pattern}/{sortBy}?size={size}&page={page}", 
						UserBoundary[].class, 
						pattern, "key",
						size, page);

		// THEN the response contains 3 Users with "abc" text pattern
		assertThat(results)
		.usingElementComparatorOnFields("userEmail")
		.containsExactlyInAnyOrderElementsOf(userWithPattern);
	}
	
}
