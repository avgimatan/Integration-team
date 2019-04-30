package smartspace.logic;

import java.util.List;

import smartspace.data.ElementEntity;

public interface ElementService {
	
	public ElementEntity writeElement(ElementEntity elementEntity);

	public List<ElementEntity> getElements(int size, int page);
	
	public List<ElementEntity> getElements(String sortBy, int size, int page);

	public List<ElementEntity> getElementsByPattern(String pattern, String sortBy, int size, int page);

}