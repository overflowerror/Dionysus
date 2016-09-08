package net.persei.dionysus.commands;

import net.persei.dionysus.managers.MusicManager;

public class MusicCommand extends Command {
	private String name;
	private MusicCommandType type;
	private MusicManager musicManager;
	private String file;
	private long duration;
	private boolean loop;
	
	public MusicCommand(String name, MusicManager musicManager, MusicCommandType type, String file, long duration, boolean loop) {
		this.name = name;
		this.musicManager = musicManager;
		this.type = type;
		this.file = file;
		this.duration = duration;
		this.loop = loop;
	}
	
	public MusicCommand(String name) {
		this.name = name;
	}
	
	@Override
	public void execute() {
		switch (type) {
		case change:
			musicManager.change(file, duration, loop);
			break;
		case pause:
			musicManager.pause();
			break;
		case play:
			musicManager.play(file, duration, loop);
			break;
		case stop:
			musicManager.stop(duration);
			break;
		default:
			break;
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override 
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!obj.getClass().equals(this.getClass()))
			return false;
		MusicCommand command = (MusicCommand) obj;
		if (!command.getName().equals(this.getName()))
			return false;
		if (command.type != type)
			return false;
		if (command.duration != duration)
			return false;
		if (command.file != null && !command.file.equals(file))
			return false;
		if (command.loop != loop)
			return false;
		return true;
	}
}
