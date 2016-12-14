package net.persei.dionysus.commands;

import java.io.FileNotFoundException;

import net.persei.dionysus.managers.MusicManager;

public class MusicPlayCommand extends MusicCommand {
	public MusicPlayCommand(String name, MusicManager musicManager, String file) throws FileNotFoundException {
		this(name, musicManager, file, 0);
	}
	
	public MusicPlayCommand(String name, MusicManager musicManager, String file, long duration) throws FileNotFoundException {
		this(name, musicManager, file, duration, true);
	}
	
	public MusicPlayCommand(String name, MusicManager musicManager, String file, long duration,
			boolean loop) throws FileNotFoundException {
		super(name, musicManager, MusicCommandType.play, file, duration, loop);
	}
}
