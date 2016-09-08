package net.persei.dionysus.managers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.persei.dionysus.players.VideoPlayer;

public class VideoManager {
	private String name;
	private PlayerManager playerManager = PlayerManager.getInstance();
	
	public VideoManager(String name) {
		this.name = name;
	}
	
	public VideoPlayer getVideoPlayer(String name) {
		return (VideoPlayer) playerManager.getByName(name);
	}
	
	public void play(String name, String file, boolean loop) {
		getVideoPlayer(name).playFile(file);
		getVideoPlayer(name).setLoop(loop);
	}
	
	public void play(String name) {
		getVideoPlayer(name).play();
	}
	
	public void stop(String name) {
		getVideoPlayer(name).stop();
	}
	
	public void pause(String name) {
		getVideoPlayer(name).pause();
	}
}
