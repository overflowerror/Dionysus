package net.persei.dionysus.events;

import net.persei.dionysus.managers.MidiSource;

public class LaunchpadEvent extends Event {

	public LaunchpadEvent(boolean press, int x, int y) {
		super(MidiSource.LAUNCHPAD, press, 0, x, y, 0);
	}

}
