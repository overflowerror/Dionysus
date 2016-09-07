package net.persei.dionysus.managers;

import java.util.LinkedList;

public class Sequence extends LinkedList<Event> {
	public Sequence append(Event event) {
		add(event);
		return this;
	}
}
