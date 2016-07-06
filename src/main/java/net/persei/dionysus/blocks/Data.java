package net.persei.dionysus.blocks;

import java.util.HashMap;
import java.util.Map;

public class Data {
	private Map<String, Object> map = new HashMap<String, Object>();
	
	public Object getEntry(String key) {
		return map.get(key);
	}
	public Object setEntry(String key, Object obj) {
		return map.put(key, obj);
	}
}
