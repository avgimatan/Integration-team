package smartspace;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import static org.assertj.core.api.Assertions.assertThat;
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
import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;
import smartspace.layout.ActionBoundary;
import smartspace.logic.ActionService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties= {"spring.profiles.active=default"})

public class ActionRestControllerTests {

	private int port;
	private String baseUrl;
	private RestTemplate restTemplate;
	private ActionDao actionDao;
	private ActionService actionService;

	@Autowired
	public void setActionService(ActionService actionService) {
		this.actionService = actionService;
	}


	@Autowired
	public void setActionDao(ActionDao actionDao) {
		this.actionDao = actionDao;
	}

	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init(){
		this.baseUrl = "http://localhost:" + port + "/actiondemo";
		this.restTemplate = new RestTemplate();
	}

	@After
	public void tearDown() {
		this.actionDao
		.deleteAll();
	}

	@Test
	public void testWriteAction() throws Exception{
		// GIVEN the database is clean 

		// WHEN I crate new Action
		ActionBoundary newAction= new ActionBoundary();
		
		newAction.setActionType("TypeTest");
		newAction.setMoreAttributes((new HashMap<String, Object>()));
		
		newAction.setPlayerEmail("guy@walla.com");
		newAction.setPlayerSmartspace("playerSmartspace");
		newAction.setElementSmartspace("2019b.dana.zuka");
		newAction.setElementId("2");
		
		
		ActionBoundary response = this.restTemplate
				.postForObject(
						this.baseUrl, 
						newAction, 
						ActionBoundary.class);

		// THEN the database contains 1 action
		// AND the returned action is similar to the action in the database
		assertThat(
				this.actionDao.readAll())
		.hasSize(1)
		.usingElementComparatorOnFields("actionId")
		.containsExactly(response.convertToEntity());

	}

	@Test
	public void testWriteActionAndValidateReturnedKey() throws Exception{
		// GIVEN the database is clean 

		// WHEN I post a new Action
		ActionBoundary newAction= new ActionBoundary();
		newAction.setActionType("TypeTest");
		newAction.setMoreAttributes((new HashMap<String, Object>()));
		newAction.setPlayerEmail("guy@walla.com");
		newAction.setPlayerSmartspace("playerSmartspace");
		newAction.setElementId("999");
		newAction.setElementSmartspace("ElementSmartspace");
		
		ActionBoundary response = this.restTemplate
				.postForObject(
						this.baseUrl, 
						newAction, 
						ActionBoundary.class);
		
		// THEN the returned Action json contains a key
		assertThat(
				response.getActionSmartspace()+
				response.getActionId())
		.isNotNull()
		.isNotBlank()
		.isNotEmpty();

	}


	@Test
	public void testGetActionsUsingPagination() throws Exception{
		// GIVEN the database contains 38 Actions
		int totalSize = 38;
		//@SuppressWarnings("unchecked")//back again to here 
		List<ActionEntity> all = 
		IntStream.range(1, totalSize + 1)
		.mapToObj(i->"Action #" + i)
		.map(name->new ActionEntity( name,  "ElementID", "playerSmartspace", "guy@gmail.com",
				"actionType", null, new HashMap<>()))
		.map(this.actionService::writeAction)
		.collect(Collectors.toList());

			List<ActionBoundary> lastEight = 
						all
						.stream()
						.skip(30)
						.map(ActionBoundary::new)
						.collect(Collectors.toList());

				// WHEN I getElement using page #3 of size 10
				int size = 10;
				int page = 3;
				ActionBoundary[] results = 
						this.restTemplate
						.getForObject(
								this.baseUrl + "?size={size}&page={page}", 
								ActionBoundary[].class, 
								size, page);


				// THEN the response contains 8 action
				assertThat(results)
				.usingElementComparatorOnFields("actionId", "creationTimestamp")
				.containsExactlyElementsOf(lastEight);
	}


	@Test
	public void testGetActionsUsingPaginationWithNoResult() throws Exception{
		// GIVEN the database contains 30 Elements
		int totalSize = 30;

		IntStream.range(1, totalSize + 1)
		.mapToObj(i->"action #" + i)
		.map(text->new ActionEntity( "ActionSmartspace",  "actionID", "playerSmartspace", "guy@gmail.com",
				"actionType", new Date(),new HashMap<>()))
		.map(this.actionService::writeAction)
		.collect(Collectors.toList());


		// WHEN I getAction using page #3 of size 10
		int size = 10;
		int page = 3;
		ActionBoundary[] results = 
				this.restTemplate
				.getForObject(
						this.baseUrl + "?size={size}&page={page}", 
						ActionBoundary[].class, 
						size, page);


		// THEN the response contains no ElementS
		assertThat(results)
		.isEmpty();
	}

	@Test
	public void testGetActionsUsingPaginationOfFirstPage() throws Exception{
		// GIVEN the database contains 38 Elements
		int totalSize = 38;

		IntStream.range(1, totalSize + 1)
		.mapToObj(i->"action #" + i)
		.map(text->new ActionEntity( "ActionSmartspace",  "actionID", "playerSmartspace", "guy@gmail.com",
				"actionType", new Date(),new HashMap<>()))
		.map(this.actionService::writeAction)
		.collect(Collectors.toList());

		// WHEN I getAction using page #0 of size 100
		int size = 100;
		int page = 0;
		ActionBoundary[] results = 
				this.restTemplate
				.getForObject(
						this.baseUrl + "?size={size}&page={page}", 
						ActionBoundary[].class, 
						size, page);

		// THEN the response contains 38 Action
		assertThat(results)
		.hasSize(totalSize);
	}

	@Test
	public void testGetActionsWithPattern() throws Exception{
		// GIVEN the database contains 3 action with the name "abc"
		// AND the database contains 2 more action without the name "abc"

		String pattern = "abc";

		List<ActionEntity> all = 
				Stream.of("abc", "abxyzabc", "xyabczz",
						"urjmdl", "ababbac")
				.map(text->new ActionEntity(text,  "actionID", "playerSmartspace", "guy@gmail.com",
						"actionType", null,new HashMap<>()))
				.map(this.actionService::writeAction)
				.collect(Collectors.toList());

		List<ActionBoundary> actionWithPattern = 
				all
				.stream()
				.filter(act->act.getActionType().contains(pattern))
				.map(ActionBoundary::new)
				.collect(Collectors.toList());

		// WHEN I getAction using pattern using page #0 of size 100 with pattern "abc"
		int size = 100;
		int page = 0;
		ActionBoundary[] results = 
			this.restTemplate
				.getForObject(
						this.baseUrl + "/{pattern}/{sortBy}?size={size}&page={page}", 
						ActionBoundary[].class, 
						pattern, "elementSmartspace",
						size, page);

		// THEN the response contains 3 Actions with "abc" text pattern
		assertThat(results)
		.usingElementComparatorOnFields("elementSmartspace")
		.containsExactlyInAnyOrderElementsOf(actionWithPattern);
	}
}
