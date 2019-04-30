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

import smartspace.logic.ActionService;

@RestController
public class ActionController {

	private ActionService actionService;

	@Autowired
	public ActionController(ActionService actionService) {
		this.actionService = actionService;
	}

	@RequestMapping(path = "/actiondemo", 
			method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary writeAction(@RequestBody ActionBoundary action) {
//		ActionEntity input = action.convertToEntity();
//		ActionEntity outputEntity = this.ActionService.writeAction(input);
//		ActionBoundary output = new ActionBoundary(outputEntity);
//		return output;

		return new ActionBoundary(this.actionService.writeAction(action.convertToEntity()));
	}

	@RequestMapping(path = "/actiondemo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] getActions(@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return this.actionService.getActions(size, page).stream().map(ActionBoundary::new).collect(Collectors.toList())
				.toArray(new ActionBoundary[0]);
	}

	@RequestMapping(path = "/actiondemo/{pattern}/{sortBy}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ActionBoundary[] getActions(@PathVariable("pattern") String pattern, @PathVariable("sortBy") String sortBy,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return this.actionService.getActionsByPattern(pattern, sortBy, size, page).stream().map(ActionBoundary::new)
				.collect(Collectors.toList()).toArray(new ActionBoundary[0]);
	}

}
