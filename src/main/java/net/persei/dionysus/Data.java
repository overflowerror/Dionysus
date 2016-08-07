package net.persei.dionysus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Data implements Serializable {
	private static final long serialVersionUID = 825230953015875521L;
	private Map<String, Object> map = new HashMap<String, Object>();
	
	public Data(Data input) {
		super();
		this.map = new HashMap<String, Object>(input.map);
	}
	public Data() {
		super();
	}
	public Object getEntry(String key) {
		return map.get(key);
	}
	public Object setEntry(String key, Object obj) {
		return map.put(key, obj);
	}
}
