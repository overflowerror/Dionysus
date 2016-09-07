package net.persei.dionysus.managers;

import java.util.HashMap;
import java.util.Map;

import net.persei.dionysus.players.AudioPlayer;
import net.persei.dionysus.players.Player;
import net.persei.dionysus.players.PlayerType;
import net.persei.dionysus.players.VideoPlayer;

public class PlayerManager {
	static private PlayerManager instance;
	
	static public PlayerManager getInstance() {
		if (instance == null)
			instance = new PlayerManager();
		return instance;
	}
	
	private PlayerManager() {
	}
	
	private Map<String, Player> players = new HashMap<>();
	
	public Player create(PlayerType type, String name) {
		return create(type, name, false);
	}
	
	public Player create(PlayerType type, String name, boolean fullscreen) {
		System.out.println("Adding " + type + " player '" + name + "'" + (fullscreen ? " in fullscreen mode..." : "..."));
		Player player = null;
		switch (type) {
		case Audio:
			player = new AudioPlayer(name);
			break;
		case Video:
			player = new VideoPlayer(name, fullscreen);
		default:
			break;
		}
		if (player != null)
			players.put(name, player);
		return player;
	}
	
	public Player getByName(String name) {
		return players.get(name);
	}
}
