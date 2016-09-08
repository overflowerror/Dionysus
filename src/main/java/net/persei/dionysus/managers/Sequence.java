package net.persei.dionysus.managers;

import java.util.LinkedList;

import net.persei.dionysus.events.Event;

public class Sequence extends LinkedList<Event> {
	public Sequence(Sequence context) {
		super(context);
	}

	public Sequence() {
		super();
	}

	public Sequence append(Event event) {
		add(event);
		return this;
	}

	public Sequence append(Sequence sequence) {
		addAll(sequence);
		return this;
	}
}
