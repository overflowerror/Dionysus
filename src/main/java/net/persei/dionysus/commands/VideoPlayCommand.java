package net.persei.dionysus.commands;

import net.persei.dionysus.managers.VideoManager;

public class VideoPlayCommand extends VideoCommand {
	public VideoPlayCommand(String name, VideoManager videoManager, String playerName,
			String file, boolean loop) {
		super(name, VideoCommandType.play, videoManager, playerName, file, loop);
	}

}
