package smartspace.data;

import java.util.Date;
import java.util.Map;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import smartspace.dao.rdb.MapToJsonConverter;

@Entity
@Table(name="ELEMENTS")
public class ElementEntity implements SmartspaceEntity<String> {

	private String elementSmartspace;
	private String elementId;
	private Location location;
	private String name;
	private String type;
	private Date creationTimestamp;
	private boolean expired;
	private String creatorSmartspace; // the smartSpace of the other user who create the element
	private String creatorEmail;
	private Map<String,Object> moreAttributes;
	
	public ElementEntity() {
	}
	
	public ElementEntity(String name, String type,Location location,
			Date creationTimestamp,String creatorEmail,
			String creatorSmartspace, boolean expired,
			Map<String, Object> moreAttributes) {		
		super();
		this.location = location;
		this.name = name;
		this.type= type;
		this.creationTimestamp =  creationTimestamp;
		this.expired = expired;
		this.creatorSmartspace = creatorSmartspace;
		this.creatorEmail = creatorEmail;
		this.moreAttributes = moreAttributes;
		
	}
	
	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	
	public String getElementSmartspace() {
		return elementSmartspace;
	}
	
	public void setElementSmartspace(String smartspace) {
		this.elementSmartspace = smartspace;
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

	@Temporal(TemporalType.TIMESTAMP)
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

	@Lob
	@Convert(converter=MapToJsonConverter.class)
	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Id
	@Override
	public String getKey() {
		return this.elementId;
	}

	@Override
	public void setKey(String key) {
		this.elementId = key;
		
	}
	
	@Override
	public String toString() {
		return "ElementEntity [elementSmartspace=" + getElementSmartspace() + ", elementId=" + getKey() + 
				", location=" + getLocation() + ", name=" + getName() + ", type=" + getType() + ", creationTimestamp=" + getCreationTimestamp() + 
				", expired=" + isExpired() + ", creatorSmartspace=" + getCreatorSmartspace() + ", creatorEmail=" + getCreatorEmail() + 
				", moreAttributes=" + getMoreAttributes() + "]";
	}
}