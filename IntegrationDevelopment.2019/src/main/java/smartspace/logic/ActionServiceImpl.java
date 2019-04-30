package smartspace.logic;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import smartspace.dao.AdvancedActionDao;
import smartspace.data.ActionEntity;

@Service
public class ActionServiceImpl implements ActionService{
	
	private AdvancedActionDao actions;
	
	@Autowired	
	public ActionServiceImpl(AdvancedActionDao actions) {
		this.setActions(actions);
	}

	@Override
	public ActionEntity writeAction(ActionEntity actionEntity) {
		if (validate(actionEntity)) {
			actionEntity.setCreationTimestamp(new Date());
			return this.actions
				.create(actionEntity);
		}else {
			throw new RuntimeException("invalid action input");
		}
	}
	
	private boolean validate(ActionEntity actionEntity) {
		return 
				//actionEntity.getActionSmartspace() != null &&
				//actionEntity.getActionId() != null &&
				actionEntity.getElementSmartspace() != null &&
				actionEntity.getElementId() != null &&
				actionEntity.getPlayerSmartspace() != null &&
				actionEntity.getPlayerEmail() != null &&
				actionEntity.getActionType() != null&&
				actionEntity.getMoreAttributes()!=nullValue();
	}


	@Override
	public List<ActionEntity> getActions(int size, int page) {
		return this.actions
				.readAll(size, page);
	}
	
	@Override
	public List<ActionEntity> getActions(String sortBy, int size, int page) {
		return this.actions
				.readAll(sortBy, size, page);
	}

	@Override
	public List<ActionEntity> getActionsByPattern(String pattern, String sortBy, int size, int page) {
		
		switch (sortBy) {
		//case "actionSmartspace":
		//case "actionId":
		case "elementSmartspace":
		case "elementId":
		case "playerSmartspace":
		case "playerEmail":
		case "actionType":
		case "creationTimestamp":
		case "moreAttributes":
		break;
		
		default:
			throw new RuntimeException("illegal sortBy value: " + sortBy);
//			break;
		}
		return this.actions.readActionsByIdPattern(
						pattern,
						sortBy,
						size, page);
	}

	public AdvancedActionDao getActions() {
		return actions;
	}

	public void setActions(AdvancedActionDao actions) {
		this.actions = actions;
	}
	
	

}
