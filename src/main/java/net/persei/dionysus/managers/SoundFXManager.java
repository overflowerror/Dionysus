package net.persei.dionysus.managers;

import java.util.LinkedList;
import java.util.List;

import net.persei.dionysus.players.AudioPlayer;
import net.persei.dionysus.players.PlayerType;

public class SoundFXManager {
	private String name;
	private List<AudioPlayer> players = new LinkedList<>();

	public  SoundFXManager(String name) {
		this.name = name;
	}
	
	public void play(String file) {
		for (AudioPlayer player : players) {
			if (player.isPlaying())
				continue;
			player.playFile(file);
			return;
		}
		AudioPlayer player = (AudioPlayer) PlayerManager.getInstance().create(PlayerType.Audio,
				"FXPlayer_" + name + "_" + players.size());
		players.add(player);
		player.playFile(file);
	}
	
	public void stopAll() {
		for (AudioPlayer player : players) {
			player.stop();
		}
	}
}
