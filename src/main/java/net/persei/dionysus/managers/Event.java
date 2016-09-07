package net.persei.dionysus.managers;

public class Event {
	static private boolean velocitySensitive = false;

	private MidiSource source;
	private boolean press;
	private int channel;
	private int x;
	private int y;
	private int v;

	public Event(MidiSource source, boolean press, int channel, int x, int y, int v) {
		this.source = source;
		this.press = press;
		this.channel = channel;
		this.x = x;
		this.y = y;
		this.v = v;
	}

	public static boolean isVelocitySensitive() {
		return velocitySensitive;
	}

	public static void setVelocitySensitive(boolean velocitySensitive) {
		Event.velocitySensitive = velocitySensitive;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Event))
			return false;
		Event event = (Event) obj;
		if (event.source != source)
			return false;
		if (event.press != press)
			return false;
		if (event.channel != channel)
			return false;
		if (event.x != x)
			return false;
		if (event.y != y)
			return false;
		if (velocitySensitive)
			if (event.v != v)
				return false;
		return true;
	}
	
	public String toString() {
		return (press ? "press" : "release") + " on " + source + " x:" + x + ", y:" + y + ", v:" + v;
	}
}