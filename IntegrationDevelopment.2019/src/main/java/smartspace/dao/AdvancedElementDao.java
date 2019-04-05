package smartspace.dao;

import java.util.Date;
import java.util.List;

import smartspace.data.ElementEntity;
// TODO
public interface AdvancedElementDao {
	public List<ElementEntity> readAll(int size, int page);
	public List<ElementEntity> readAll(String sortBy, int size, int page);
	
	public List<ElementEntity> readMessagesByTextPattern (String pattern, int size, int page);
	
	public List<ElementEntity> readMessagesWithAvailableFromInRange(
			Date fromDate,
			Date toDate,
			int size, 
			int page); 
}
