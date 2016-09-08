package net.persei.dionysus.commands;

import net.persei.dionysus.managers.MusicManager;
import net.persei.dionysus.managers.SoundFXManager;

public class SoundFXCommand extends Command {
	private String name;
	private SoundFXComanndType type;
	private SoundFXManager soundManager;
	private String file;
	
	public SoundFXCommand(String name, SoundFXManager soundManager, MusicCommandType type, String file) {
		this.name = name;
		this.soundManager = soundManager;
		this.file = file;
	}
	
	public SoundFXCommand(String name) {
		this.name = name;
	}
	
	@Override
	public void execute() {
		switch (type) {
		case play:
			soundManager.play(file);
			break;
		case stop:
			soundManager.stopAll();
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
		SoundFXCommand command = (SoundFXCommand) obj;
		if (!command.getName().equals(this.getName()))
			return false;
		if (command.type != type)
			return false;
		if (command.file != null && !command.file.equals(file))
			return false;
		return true;
	}
}
