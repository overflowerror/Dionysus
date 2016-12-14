package net.persei.dionysus.commands;

import java.io.FileNotFoundException;

import net.persei.dionysus.managers.VideoManager;

public class VideoPlayCommand extends VideoCommand {
	public VideoPlayCommand(String name, VideoManager videoManager, String playerName,
			String file, boolean loop) throws FileNotFoundException {
		super(name, VideoCommandType.play, videoManager, playerName, file, loop);
	}

}
