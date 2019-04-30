package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.AdvancedElementDao;
import smartspace.data.ElementEntity;

@Repository
public class RdbElementDao implements AdvancedElementDao<String> {

	private ElementCrud elementCrud;
	private GeneratorIdCrud generatorIdCrud;
	private String smartspace;
	// private AtomicLong serial;

	@Autowired
	public RdbElementDao(ElementCrud elementCrud, GeneratorIdCrud generatorIdCrud) {
		super();
		this.elementCrud = elementCrud;
		// this.serial = new AtomicLong(1000L);
		this.generatorIdCrud = generatorIdCrud;
	}

	@Value("${smartspace.name:smartspace}")
	public void setSmartspace(String smartspace) {
		this.smartspace = smartspace;
	}

	@Override
	@Transactional
	public ElementEntity create(ElementEntity elementEntity) {
		// elementEntity.setKey(smartspace + "#" + serial.getAndIncrement());
		GeneratorId idEntity = this.generatorIdCrud.save(new GeneratorId());
		elementEntity.setKey(smartspace + "#" + idEntity.getId());
		this.generatorIdCrud.delete(idEntity);

		// SQL: INSERT
		if (!this.elementCrud.existsById(elementEntity.getKey())) {
			return this.elementCrud.save(elementEntity);
		} else {
			throw new RuntimeException("entity already exists with key: " + elementEntity.getKey());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ElementEntity> readById(String elementKey) {
		// SQL: SELECT
		return this.elementCrud.findById(elementKey);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readAll() {
		List<ElementEntity> rv = new ArrayList<>();
		// SQL: SELECT
		this.elementCrud.findAll().forEach(rv::add);
		return rv;
	}

	@Override
	@Transactional
	public void update(ElementEntity updateElementEntity) {
		ElementEntity existing = this.readById(updateElementEntity.getKey())
				.orElseThrow(() -> new RuntimeException("no element entity with key: " + updateElementEntity.getKey()));

		if (updateElementEntity.getLocation() != null) {
			existing.setLocation(updateElementEntity.getLocation());
		}
		if (updateElementEntity.getName() != null) {
			existing.setName(updateElementEntity.getName());
		}
		if (updateElementEntity.getType() != null) {
			existing.setType(updateElementEntity.getType());
		}
		if (updateElementEntity.getMoreAttributes() != null) {
			existing.setMoreAttributes(updateElementEntity.getMoreAttributes());
		}
		if (updateElementEntity.getCreationTimestamp() != null) {
			existing.setCreationTimestamp(updateElementEntity.getCreationTimestamp());
		}
		if (updateElementEntity.getCreatorEmail() != null) {
			existing.setCreatorEmail(updateElementEntity.getCreatorEmail());
		}
		if (updateElementEntity.getCreatorSmartspace() != null) {
			existing.setCreatorSmartspace(updateElementEntity.getCreatorSmartspace());
		}
		existing.setExpired(updateElementEntity.getExpired());

		// SQL: UPDATE
		this.elementCrud.save(existing);
	}

	@Override
	@Transactional
	public void deleteByKey(String elementKey) {
		if (this.elementCrud.existsById(elementKey))
			this.elementCrud.deleteById(elementKey);
	}

	@Override
	@Transactional
	public void delete(ElementEntity elementEntity) {
		if (this.elementCrud.existsById(elementEntity.getKey())) {
			this.elementCrud.delete(elementEntity);
		}
	}

	@Override
	@Transactional
	public void deleteAll() {
		this.elementCrud.deleteAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readAll(int size, int page) {
		return this.elementCrud.findAll(PageRequest.of(page, size)).getContent();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readAll(String sortBy, int size, int page) {
		return this.elementCrud.findAll(PageRequest.of(page, size, Direction.ASC, sortBy)).getContent();
	}

	@Override

	@Transactional(readOnly = true)
	public List<ElementEntity> readElementByTextPattern(String pattern, int size, int page) {
		return this.elementCrud.findAllByNameLike("%" + pattern + "%", PageRequest.of(page, size));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ElementEntity> readElementsWithCreationTimestampInRange(Date fromDate, Date toDate, int size, int page) {
		return this.elementCrud.findAllByCreationTimestampBetween(fromDate, toDate, PageRequest.of(page, size));

	}

	@Override
	public List<ElementEntity> readElementByTextPattern(String pattern, String sortBy, int size, int page) {
		return this.elementCrud.findAllByNameLike("%" + pattern + "%", PageRequest.of(page, size, Direction.ASC, sortBy));
	}

}
