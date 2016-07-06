package net.persei.dionysus;

import uk.co.caprica.vlcj.player.MediaPlayer;

public interface Player {
	boolean isLoop();
	void playFile(String file);
	void play();
	void pause();
	void setLoop(boolean loop);
	MediaPlayer getMediaPlayer();
	String getName();
}
