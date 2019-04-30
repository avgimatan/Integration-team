package smartspace.logic;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.AdvancedElementDao;
import smartspace.data.ElementEntity;

@Service
public class ElementServiceImpl implements ElementService{

	private AdvancedElementDao<String> elements;
	
	@Autowired	
	public ElementServiceImpl(AdvancedElementDao<String> elements) {
		this.elements = elements;
	}
	
	@Transactional
	@Override
	public ElementEntity writeElement(ElementEntity elementEntity) {
		if (validate(elementEntity)) {
			elementEntity.setCreationTimestamp(new Date());
			return this.elements
				.create(elementEntity);
		}else {
			throw new RuntimeException("invalid element input");
		}
	}

	private boolean validate(ElementEntity elementEntity) {
		return 
		//elementEntity.getElementId() != null &&
				//elementEntity.getElementSmartspace() != null &&
				elementEntity.getName() != null &&
				elementEntity.getType() != null &&
				//elementEntity.getCreationTimestamp() != null &&
				elementEntity.getCreatorSmartspace() != null &&
				elementEntity.getCreatorEmail() != null 
				&&elementEntity.getMoreAttributes()!=null
				&&String.valueOf(elementEntity.getLocation().getX()) != null
				&& String.valueOf(elementEntity.getLocation().getY()) != null; 
	}

	@Override
	public List<ElementEntity> getElements(int size, int page) {
		return this.elements
				.readAll(size, page);
	}
	
	@Override
	public List<ElementEntity> getElements(String sortBy, int size, int page) {
		return this.elements
				.readAll(sortBy, size, page);
	}

	@Override
	public List<ElementEntity> getElementsByPattern(String pattern, String sortBy, int size, int page) {
		switch (sortBy) {
		case "elementSmartspace":
		case "elementId":
		case "location":
		case "name":
		case "type":
		case "creationTimestamp":
		case "expired":
		case "creatorSmartspace":
		case "creatorEmail":
		break;

		default:
			throw new RuntimeException("illegal sortBy value: " + sortBy);
//			break;
	}
	return this.elements
			.readElementByTextPattern(
					pattern, 
					sortBy,
					size, 
					page);
	}

}
