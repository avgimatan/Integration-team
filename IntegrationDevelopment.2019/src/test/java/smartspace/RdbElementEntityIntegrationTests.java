package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.dao.ElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.util.EntityFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class RdbElementEntityIntegrationTests {
	
	private ElementDao<String> elementDao;
	private EntityFactory factory;
	
	@Autowired
	public void setMessageDao(ElementDao<String> elementDao) {
		this.elementDao = elementDao;
	}
	
	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}

	@After
	public void teardown() {
		this.elementDao.deleteAll();
	}

	@Test
	public void testCreateSimpleElement () throws Exception{
		
		// GIVEN the database is clean
		
		// WHEN we create a new message with text "Test" and store it in DB
		String text = "Test";
		ElementEntity elementEntity = 
			this.factory.createNewElement(text, "Task", new Location(1.0,1.0),
					new Date(), "tavb@gmail.com", "2019b.dana.zuka",false, new HashMap<>());
		ElementEntity actual = this.elementDao.create(elementEntity);
				
		// THEN the element is stored 
		assertThat(this.elementDao.readById(actual.getKey()))
			.isPresent()
			.get()
			.extracting("elementId", "name")
			.containsExactly(actual.getKey(), text);
	}
}