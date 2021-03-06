package net.persei.dionysus.players;

import uk.co.caprica.vlcj.player.MediaPlayer;

public interface Player {
	boolean isLoop();
	void playFile(String file);
	void play();
	boolean isPlaying();
	void stop();
	void pause();
	void setLoop(boolean loop);
	MediaPlayer getMediaPlayer();
	String getName();
}
