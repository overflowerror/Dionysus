package net.persei.dionysus.blocks;

import java.util.LinkedList;
import java.util.List;

import net.persei.dionysus.exceptions.UnexpectedDataTypeException;

public class BlockHelper {
	static void addPassLog(Data input, String type, Object object) throws UnexpectedDataTypeException {
		Object obj = input.getEntry(type);
		List<Object> log;
		if (obj == null) {
			log = new LinkedList<Object>();
		} else if (! (obj instanceof List)) {
			throw new UnexpectedDataTypeException(type);
		} else {
			 log = (List<Object>) obj;
		}
		log.add(object);
	}
}
