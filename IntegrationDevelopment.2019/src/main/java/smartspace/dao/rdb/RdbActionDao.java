package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.AdvancedActionDao;
import smartspace.data.ActionEntity;

@Repository
public class RdbActionDao implements AdvancedActionDao {

	private ActionCrud actionCrud;
	private GeneratorIdCrud generatorIdCrud;
	private String smartspace;

	// private AtomicLong serial;

	@Autowired
	public RdbActionDao(ActionCrud actionCrud, GeneratorIdCrud generatorIdCrud) {
		super();
		this.actionCrud = actionCrud;

		// this.serial = new AtomicLong(1000L);
		this.generatorIdCrud = generatorIdCrud;
	}

	@Value("${smartspace.name:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	@Override
	@Transactional
	public ActionEntity create(ActionEntity actionEntity) {
		// actionEntity.setKey(smartspace + "#" + serial.getAndIncrement());
		GeneratorId idEntity = this.generatorIdCrud.save(new GeneratorId());
		actionEntity.setKey(smartspace + "#" + idEntity.getId());
		actionEntity.setActionSmartspace(smartspace);
		this.generatorIdCrud.delete(idEntity);

		// SQL: INSERT
		if (!this.actionCrud.existsById(actionEntity.getKey())) {
			return this.actionCrud.save(actionEntity);
		} else {
			throw new RuntimeException("entity already exists with key: " + actionEntity.getKey());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ActionEntity> readAll() {
		List<ActionEntity> rv = new ArrayList<ActionEntity>();
		// SQL: SELECT
		this.actionCrud.findAll().forEach(rv::add);
		return rv;
	}

	@Override
	@Transactional
	public void deleteAll() {
		// SQL: DELETE
		this.actionCrud.deleteAll();
	}

	// finish all method

	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readAll(int size, int page) {
		return this.actionCrud.findAll
				(PageRequest.of(page, size)).getContent();
	}

	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readAll(String sortBy, int size, int page) {
		return this.actionCrud.findAll
				(PageRequest.of(page, size, Direction.ASC, sortBy)).getContent();
	}

	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readActionsByPlayerEmailPattern(String pattern, int size, int page) {
		return this.actionCrud.findAllByPlayerEmailLike(
				"%" + pattern + "%", PageRequest.of(page, size));
	}

	// check if necessary
	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readActionsByIdPattern(String pattern, int size, int page) {
		return this.actionCrud.findByElementIdLike(
				"%" + pattern + "%", PageRequest.of(page, size));
	}
	
	@Override
	public List<ActionEntity> readActionsByIdPattern(String pattern, String sortBy, int size, int page) {
		// change 
		return this.actionCrud.findByElementIdLike(
				"%" + pattern + "%", PageRequest.of(page, size, Direction.ASC, sortBy));
	}

	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readActionsWithAvailableFromInRange(Date fromDate, Date toDate, int size, int page) {
		return this.actionCrud.findAllByCreationTimestampBetween(
				fromDate, toDate, PageRequest.of(page, size));
	}



}
