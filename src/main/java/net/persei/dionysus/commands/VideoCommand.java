package net.persei.dionysus.commands;

import java.io.File;
import java.io.FileNotFoundException;

import net.persei.dionysus.managers.VideoManager;

public class VideoCommand extends Command {
	private String name;
	private VideoManager videoManager;
	private VideoCommandType type;
	private String playerName;
	private String file;
	private boolean loop;

	public VideoCommand(String name, VideoCommandType type, VideoManager videoManager, String playerName, String file,
			boolean loop) throws FileNotFoundException {
		this.name = name;
		this.type = type;
		this.videoManager = videoManager;
		if (file != null)
			if (!new File(file).exists())
				throw new FileNotFoundException(file);
		this.file = file;
		this.playerName = playerName;
		this.loop = loop;
	}

	@Override
	public void execute() {
		switch (type) {
		case pause:
			videoManager.pause(playerName);
			break;
		case play:
			if (file == null)
				videoManager.play(playerName);
			else
				videoManager.play(playerName, file, loop);
			break;
		case stop:
			videoManager.stop(playerName);
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
		VideoCommand command = (VideoCommand) obj;
		if (!command.getName().equals(this.getName()))
			return false;
		if (command.type != type)
			return false;
		if (command.file != null && !command.file.equals(file))
			return false;
		if (command.loop != loop)
			return false;
		if (!command.playerName.equals(playerName))
			return false;
		return true;
	}

}
