package smartspace.layout;

import java.util.Date;
import java.util.Map;

import smartspace.data.ActionEntity;

public class ActionBoundary {

	private String actionSmartspace;
	private String actionId;
	private String elementSmartspace;
	private String elementId;
	private String playerSmartspace;
	private String playerEmail;
	private String actionType;
	private Date creationTimestamp;
	private Map<String, Object> moreAttributes;

	public ActionBoundary() {
	}

	public ActionBoundary(ActionEntity action) {
		this.actionSmartspace = action.getActionSmartspace();
		this.actionId = action.getActionId();
		this.elementSmartspace = action.getElementSmartspace();
		this.elementId = action.getElementId();
		this.playerSmartspace = action.getPlayerSmartspace();
		this.playerEmail = action.getPlayerEmail();
		this.actionType = action.getActionType();
		this.creationTimestamp = action.getCreationTimestamp();
		this.moreAttributes = action.getMoreAttributes();

	}

	public ActionEntity convertToEntity() {
		ActionEntity action = new ActionEntity();
		action.setKey(this.actionSmartspace + "#" + this.actionId);
		action.setActionSmartspace(this.getActionSmartspace());
		action.setActionId(this.getActionId());
		action.setElementSmartspace(this.getElementSmartspace());
		action.setElementId(this.getElementId());
		action.setPlayerSmartspace(this.getPlayerSmartspace());
		action.setPlayerEmail(this.getPlayerEmail());
		action.setActionType(this.getActionType());
		action.setCreationTimestamp(this.getCreationTimestamp());
		action.setMoreAttributes(this.getMoreAttributes());

		return action;
	}

	@Override
	public String toString() {
		return "ActionBoundary [actionSmartspace=" + actionSmartspace + ", userId=" + actionId + ", elementSmartspace="
				+ elementSmartspace + ", elementId=" + elementId + ", playerSmartspace=" + playerSmartspace
				+ ", playerEmail=" + playerEmail + ",actionType=" + actionType + ",creationTimestamp"
				+ creationTimestamp + ",moreAttributes" + moreAttributes + "]";
	}

	public String getActionSmartspace() {
		return actionSmartspace;
	}

	public void setActionSmartspace(String actionSmartspace) {
		this.actionSmartspace = actionSmartspace;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getElementSmartspace() {
		return elementSmartspace;
	}

	public void setElementSmartspace(String elementSmartspace) {
		this.elementSmartspace = elementSmartspace;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getPlayerSmartspace() {
		return playerSmartspace;
	}

	public void setPlayerSmartspace(String playerSmartspace) {
		this.playerSmartspace = playerSmartspace;
	}

	public String getPlayerEmail() {
		return playerEmail;
	}

	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}

}
