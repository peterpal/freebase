package sk.stuba.fiit.freebase.hadoop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Topic {
	private String id;
	private Map<String, Collection<String>> predicatsToObjectsMap;
	
	public Topic(String id) {
		this(id, new HashMap<String, Collection<String>>());
	}
	
	public Topic(String id, Map<String, Collection<String>> predicatsToObjectsMap) {
		super();
		this.id = id;
		this.predicatsToObjectsMap = predicatsToObjectsMap;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Collection<String> getPredicats() {
		return predicatsToObjectsMap.keySet();
	}
	
	public Collection<String> getObjects(String subject) {
		return predicatsToObjectsMap.get(subject);
	}
	
	public boolean addObject(String predicat, String object) {
		if(!predicatsToObjectsMap.containsKey(predicat))
			predicatsToObjectsMap.put(predicat, new ArrayList<String>());
		
		Collection<String> objects = predicatsToObjectsMap.get(predicat);
		if(predicatsToObjectsMap.containsKey(object))
			return false;
		return objects.add(object);
	}

	@Override
	public String toString() {
		return "Topic [id=" + id + ", subjectsToObjectsMap="
				+ predicatsToObjectsMap + "]";
	}
	
	
}
