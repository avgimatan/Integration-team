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

import smartspace.dao.ElementDao;

import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.layout.ElementBoundary;
import smartspace.logic.ElementService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class ElementRestControllerTests {


	private int port;
	private String baseUrl;
	private RestTemplate restTemplate;
	private ElementDao<String> elementDao;
	private ElementService elementService;
	
	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}
	
	@Autowired
	public void setElementDao(ElementDao<String> elementDao) {
		this.elementDao = elementDao;
	}
	
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init(){
		this.baseUrl = "http://localhost:" + port + "/elementdemo";
		this.restTemplate = new RestTemplate();
	}

	@After
	public void tearDown() {
		this.elementDao
			.deleteAll();
	}

	@Test
	public void testWriteElement() throws Exception{
		// GIVEN the database is clean 
		
		// WHEN I post a new element
		ElementBoundary newElement= new ElementBoundary();
		newElement.setExpired(false);
		newElement.setLocation("1.2#3.3");
		newElement.setMoreAttributes((new HashMap<String, Object>()));
		newElement.setName("test");
		newElement.setType("task");
		newElement.setCreatorEmail("tav@gmail");
		newElement.setCreatorSmartspace("2019b.dana.zuka");
		
		ElementBoundary response = this.restTemplate
			.postForObject(
					this.baseUrl, 
					newElement, 
					ElementBoundary.class);
		
		// THEN the database contains 1 element
		// AND the returned element is similar to the message in the database
		assertThat(
				this.elementDao.readAll())
			.hasSize(1)
			.usingElementComparatorOnFields("name")
			.containsExactly(response.convertToEntity());
		
	}
	
	@Test
	public void testWriteElementAndValidateReturnedKey() throws Exception{
		// GIVEN the database is clean 
		
		// WHEN I post a new message
		ElementBoundary newElement = new ElementBoundary();
		newElement.setExpired(false);
		newElement.setLocation("1.2#3.3");
		newElement.setMoreAttributes((new HashMap<String, Object>()));
		newElement.setName("test");
		newElement.setType("task");
		newElement.setCreatorEmail("tav@gmail");
		newElement.setCreatorSmartspace("2019b.dana.zuka");
		
		ElementBoundary response = this.restTemplate
				
			.postForObject(
					this.baseUrl, 
					newElement, 
					ElementBoundary.class);
		
		// THEN the returned message json contains a key
		assertThat(
				response.getElementId())
			.isNotNull()
			.isNotBlank()
			.isNotEmpty();
		
	}


	@Test
	public void testGetElementsUsingPagination() throws Exception{
		// GIVEN the database contains 38 Element
		int totalSize = 38;
		List<ElementEntity> all = 
		IntStream.range(1, totalSize+1 )
			.mapToObj(i->"Element #" + i)
			 .map(name->new ElementEntity(
					 name, "task",new Location(1.2,1.0),null,"jane@gmail.com","2019b.dana.zuka",
					 false,	new HashMap<>()))
			.map(this.elementService::writeElement)
			.collect(Collectors.toList());
		
		List<ElementBoundary> lastEight = 
				all
				.stream()
				.skip(30)
				.map(ElementBoundary::new)
				.collect(Collectors.toList());
		
		// WHEN I getElement using page #3 of size 10
		int size = 10;
		int page = 3;
		ElementBoundary[] results = 
		  this.restTemplate
			.getForObject(
					this.baseUrl + "?size={size}&page={page}", 
					ElementBoundary[].class, 
					size, page);
		
		
		// THEN the response contains 8 elements
		assertThat(results)
			.usingElementComparatorOnFields("name")
			.containsExactlyElementsOf(lastEight);
	}


	@Test
	public void testGetElementsUsingPaginationWithNoResult() throws Exception{
		// GIVEN the database contains 30 Elements
		int totalSize = 30;

		IntStream.range(1, totalSize + 1)
			.mapToObj(i->"element #" + i)
			.map(text->new ElementEntity(
					text, "task",new Location(1.2,1.0),new Date(),"jane@gmail.com","2019b.dana.zuka",
				false,	new HashMap<>()))
			.map(this.elementService::writeElement)
			.collect(Collectors.toList());
		
		
		// WHEN I getElements using page #3 of size 10
		int size = 10;
		int page = 3;
		ElementBoundary[] results = 
		  this.restTemplate
			.getForObject(
					this.baseUrl + "?size={size}&page={page}", 
					ElementBoundary[].class, 
					size, page);
		
		
		// THEN the response contains no ElementS
		assertThat(results)
			.isEmpty();
	}

	@Test
	public void testGetElementsUsingPaginationOfFirstPage() throws Exception{
		// GIVEN the database contains 38 Elements
		int totalSize = 38;

		IntStream.range(1, totalSize + 1)
			.mapToObj(i->"element #" + i)
			.map(name->new ElementEntity(
					name, "task",new Location(1.2,1.0),new Date(),"jane@gmail.com","2019b.dana.zuka",
				false,	new HashMap<>()))
			.map(this.elementService::writeElement)
			.collect(Collectors.toList());
		
		// WHEN I getElements using page #0 of size 100
		int size = 100;
		int page = 0;
		ElementBoundary[] results = 
		  this.restTemplate
			.getForObject(
					this.baseUrl + "?size={size}&page={page}", 
					ElementBoundary[].class, 
					size, page);
		
		
		// THEN the response contains 38 messages
		assertThat(results)
			.hasSize(totalSize);
	}


	@Test
	public void testGetElementsWithPattern() throws Exception{
		// GIVEN the database contains 3 Elements with the name "abc"
		// AND the database contains 2 more Elements without the name "abc"
		
		String pattern = "abc";
		
		List<ElementEntity> all = 
		  Stream.of("abc", "abxyzabc", "xyabczz",
				"urjmdl", "ababbac")
			.map(text->new ElementEntity(
					text, "task",new Location(1.2,1.0),new Date(),"jane@gmail.com","2019b.dana.zuka",
				false,	new HashMap<>()))
			.map(this.elementService::writeElement)
			.collect(Collectors.toList());
		
		List<ElementBoundary> elementWithPattern = 
				all
				.stream()
				.filter(elm->elm.getName().contains(pattern))
				.map(ElementBoundary::new)
				.collect(Collectors.toList());
		
		// WHEN I getElement using pattern using page #0 of size 100 with pattern "abc"
		int size = 100;
		int page = 0;
		ElementBoundary[] results = 
		  this.restTemplate
			.getForObject(
					this.baseUrl + "/{pattern}/{sortBy}?size={size}&page={page}", 
					ElementBoundary[].class, 
					pattern, "name",
					size, page);
		
		
		// THEN the response contains 3 messages with "abc" text pattern
		assertThat(results)
			.usingElementComparatorOnFields("name")
			.containsExactlyInAnyOrderElementsOf(elementWithPattern);
	}


	
}
