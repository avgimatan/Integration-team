package smartspace.layout;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smartspace.logic.UserService;

@RestController
public class UserController {

	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(path = "/userdemo",
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary writeUser(@RequestBody UserBoundary user) {
//		UserEntity input = user.convertToEntity();
//		UserEntity outputEntity = this.UserService.writeUser(input);
//		UserBoundary output = new UserBoundary(outputEntity);
//		return output;

		return new UserBoundary(this.userService.writeUser(user.convertToEntity()));
	}

	@RequestMapping(path = "/userdemo",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getUsers(
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return this.userService
				.getUsers(size, page)
				.stream()
				.map(UserBoundary::new)
				.collect(Collectors.toList())
				.toArray(new UserBoundary[0]);
	}

	@RequestMapping(path = "/userdemo/{pattern}/{sortBy}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary[] getUsers(
			@PathVariable("pattern") String pattern,
			@PathVariable("sortBy") String sortBy,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return this.userService
				.getUsersByPattern(pattern, sortBy, size, page)
				.stream()
				.map(UserBoundary::new)
				.collect(Collectors.toList())
				.toArray(new UserBoundary[0]);
	}
}
