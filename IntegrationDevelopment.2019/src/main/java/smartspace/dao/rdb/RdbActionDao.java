package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;

@Repository
public class RdbActionDao implements ActionDao{
	
	private ActionCrud actionCrud;
	private GeneratorIdCrud generatorIdCrud;
	private String smartspace;
	
	//private AtomicLong serial;
	
	@Autowired
	public RdbActionDao(ActionCrud actionCrud, GeneratorIdCrud generatorIdCrud) {
		this.actionCrud = actionCrud;

		//this.serial = new AtomicLong(1000L);
		this.generatorIdCrud = generatorIdCrud;
	}
	
	@Value("${smartspace.name:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}
	
	@Override
	@Transactional
	public ActionEntity create(ActionEntity actionEntity) {
		//actionEntity.setKey(smartspace + "#" + serial.getAndIncrement());
		GeneratorId idEntity = this.generatorIdCrud.save(new GeneratorId());
		actionEntity.setKey(smartspace + "#" + idEntity.getId());
		this.generatorIdCrud.delete(idEntity);
		
		// SQL: INSERT
		if (!this.actionCrud.existsById(actionEntity.getKey())) {
			return this.actionCrud.save(actionEntity);
		}else {
			throw new RuntimeException("entity already exists with key: " + actionEntity.getKey());
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readAll() {
		List<ActionEntity> rv = new ArrayList<ActionEntity>();
		// SQL: SELECT
		this.actionCrud.findAll()
			.forEach(rv::add);
		return rv;
	}

	@Override
	@Transactional
	public void deleteAll() {
		// SQL: DELETE
		this.actionCrud.deleteAll();
	}

}
