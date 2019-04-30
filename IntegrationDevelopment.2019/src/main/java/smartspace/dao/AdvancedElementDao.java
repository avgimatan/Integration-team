package smartspace.dao;

import java.util.Date;
import java.util.List;

import smartspace.data.ElementEntity;

// TODO
public interface AdvancedElementDao<K> extends ElementDao<K> {
	
	public List<ElementEntity> readAll(int size, int page);

	public List<ElementEntity> readAll(String sortBy, int size, int page);

	public List<ElementEntity> readElementByTextPattern(String pattern, int size, int page);
	
	public List<ElementEntity> readElementByTextPattern(String pattern, String sortBy, int size, int page);

	public List<ElementEntity> readElementsWithCreationTimestampInRange(Date fromDate, Date toDate, int size, int page);

}
