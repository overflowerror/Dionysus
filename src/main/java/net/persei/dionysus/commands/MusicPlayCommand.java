package net.persei.dionysus.commands;

import net.persei.dionysus.managers.MusicManager;

public class MusicPlayCommand extends MusicCommand {
	public MusicPlayCommand(String name, MusicManager musicManager, String file) {
		this(name, musicManager, file, 0);
	}
	
	public MusicPlayCommand(String name, MusicManager musicManager, String file, long duration) {
		this(name, musicManager, file, duration, true);
	}
	
	public MusicPlayCommand(String name, MusicManager musicManager, String file, long duration,
			boolean loop) {
		super(name, musicManager, MusicCommandType.play, file, duration, loop);
	}
}
