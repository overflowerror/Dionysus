package net.persei.dionysus.players;

import uk.co.caprica.vlcj.component.AudioMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;

public class AudioPlayer implements Player {

	private AudioMediaPlayerComponent player;
	private boolean loop = false;
	private String name;
	
	public AudioPlayer(String name) {
		this.name = name;
		player = new AudioMediaPlayerComponent();
	}
	
	public boolean isLoop() {
		return loop;
	}
	
	public void playFile(String file) {
		getMediaPlayer().playMedia(file);
	}
	
	public void play() {
		getMediaPlayer().play();
	}
	
	public void pause() {
		getMediaPlayer().pause();
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
		getMediaPlayer().setRepeat(loop);
	}
	
	public MediaPlayer getMediaPlayer() {
		return player.getMediaPlayer();
	}

	public String getName() {
		return name;
	}
}
