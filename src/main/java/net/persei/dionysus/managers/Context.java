package net.persei.dionysus.managers;

import net.persei.dionysus.events.Event;

public class Context extends Sequence {
	public Context(Context context) {
		super();
		addAll(context);
	}
	public Context() {
		super();
	}
	public Context append(Event event) {
		add(event);
		return this;
	}
	public Context append(Context context) {
		addAll(context);
		return this;
	}
	public Context append(Sequence sequence) {
		addAll(sequence);
		return this;
	}
}
