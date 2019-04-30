package smartspace.layout;

import java.util.Date;
import java.util.Map;

import smartspace.data.ElementEntity;
import smartspace.data.Location;


public class ElementBoundary {

	private String elementSmartspace;
	private String elementId;
	private String location;
	private String name;
	private String type;
	private Date creationTimestamp;
	private boolean expired;
	private String creatorSmartspace; // the smartSpace of the other user who create the element
	private String creatorEmail;
	private Map<String, Object> moreAttributes;

	public ElementBoundary() {
		
	}
	
	public ElementBoundary(ElementEntity entity) {
		Location loc=entity.getLocation();
		if (loc!= null) {
		this.location = entity.getLocation().getX()+"#"+entity.getLocation().getY();
		}else {
			this.location=null;
		}
		this.creationTimestamp = entity.getCreationTimestamp();
		this.name = entity.getName();
		this.type = entity.getType();
		this.moreAttributes = entity.getMoreAttributes();
		this.expired = entity.getExpired();
		this.elementSmartspace = entity.getElementSmartspace();// getKey().split("#")[0];
		this.elementId = entity.getElementId();// getKey().split("#")[1];
		this.creatorSmartspace = entity.getCreatorSmartspace();
		this.creatorEmail = entity.getCreatorEmail();

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public String getCreatorSmartspace() {
		return creatorSmartspace;
	}

	public void setCreatorSmartspace(String creatorSmartspace) {
		this.creatorSmartspace = creatorSmartspace;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}

	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public ElementEntity convertToEntity() {
		ElementEntity entity = new ElementEntity();
		entity.setKey(this.elementSmartspace + '#' + this.elementId);
		
		entity.setLocation(null);
		if(this.location!=null&& this.location.contains("#")) {
			

			String[] args = this.location.split("#");
			if (args.length == 2) {
				entity.setLocation(new Location(Double.parseDouble( args[0]),Double.parseDouble( args[1])));
			}
		}
		entity.setCreationTimestamp(this.creationTimestamp);
		entity.setName(this.name);
		entity.setType(this.type);
		
		entity.setMoreAttributes(this.moreAttributes);
		
		entity.setExpired(this.expired);
		entity.setCreatorSmartspace(this.creatorSmartspace);
		entity.setCreatorEmail(this.creatorEmail);
		entity.setElementSmartspace(this.elementSmartspace);
		entity.setElementId(this.elementId);

		return entity;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}

	public String toString() {
		return "ElementBoundary [elementSmartspace=" + elementSmartspace + ", elementId=" + elementId + ", location="
				+ location + ", name=" + name + ", type=" + type + ", creationTimestamp=" + creationTimestamp
				+ ", expired=" + expired + ", creatorSmartspace=" + creatorSmartspace + ", creatorEmail=" + creatorEmail
				+ ", moreAttributes=" + moreAttributes + "]";
	}
}
