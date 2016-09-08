package net.persei.dionysus.managers;

import net.persei.dionysus.players.AudioPlayer;
import net.persei.dionysus.players.PlayerType;
import uk.co.caprica.vlcj.player.MediaPlayer;

public class MusicManager {
	static private int BASE_VOLUME = 100;

	private String name;

	private AudioPlayer[] crossfadePlayers = new AudioPlayer[2];
	private boolean[] isPlaying = new boolean[2];

	public MusicManager(String name) {
		this.name = name;
		PlayerManager playerManager = PlayerManager.getInstance();
		crossfadePlayers[0] = (AudioPlayer) playerManager.create(PlayerType.Audio, "MusicPlayer_" + name + "_fade0");
		crossfadePlayers[1] = (AudioPlayer) playerManager.create(PlayerType.Audio, "MusicPlayer_" + name + "_fade1");
	}

	public void play(String file, long fadeduration, boolean loop) {
		change(file, fadeduration, loop);
	}

	public void change(String file, long fadeduration, boolean loop) {
		if (!isPlaying[0]) {
			// use 0
			stopPlayer(1, fadeduration);
			startPlayer(0, file, fadeduration, loop);
		} else if (!isPlaying[1]) {
			// use 2
			stopPlayer(0, fadeduration);
			startPlayer(1, file, fadeduration, loop);
		} else {
			// no player is applicable
		}
	}

	public void stop(long fadeduration) {
		for (int i = 0; i < crossfadePlayers.length; i++)
			stopPlayer(i, fadeduration);
	}

	public void pause() {
		// TODO resume
		for (AudioPlayer player : crossfadePlayers)
			player.pause();
	}

	private void startPlayer(int playerId, String file, long fadeduration, boolean loop) {
		new Thread() {
			public void run() {
				setPlaying(playerId, true);
				MediaPlayer player = crossfadePlayers[playerId].getMediaPlayer();
				
				crossfadePlayers[playerId].playFile(file);
				crossfadePlayers[playerId].setLoop(loop);
				if (fadeduration == 0) {
					player.setVolume(BASE_VOLUME);
					return;
				}
				player.setVolume(0);

				int targetVolume = BASE_VOLUME;
				int sourceVolume = 0;
				int stepsPerSecond = 10;
				int numberOfSteps = (int) ((fadeduration * stepsPerSecond) / 1000);
				int msPerStep = (int) (fadeduration / numberOfSteps);
				int incrementPerStep = (sourceVolume - targetVolume) / numberOfSteps;

				try {
					for (int i = 0; i < numberOfSteps; i++) {
						player.setVolume(player.getVolume() + incrementPerStep);
						Thread.sleep(msPerStep);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private synchronized void setPlaying(int playerId, boolean playing) {
		isPlaying[playerId] = playing;
	}

	private void stopPlayer(int playerId, long fadeduration) {
		new Thread() {
			public void run() {
				if (!crossfadePlayers[playerId].isPlaying())
					return;
				MediaPlayer player = crossfadePlayers[playerId].getMediaPlayer();
				int targetVolume = 0;
				int sourceVolume = player.getVolume();
				int stepsPerSecond = 10;
				int numberOfSteps = (int) ((fadeduration * stepsPerSecond) / 1000);
				int msPerStep = (int) (fadeduration / numberOfSteps);
				int decrementPerStep = (sourceVolume - targetVolume) / numberOfSteps;

				try {
					for (int i = 0; i < numberOfSteps; i++) {
						player.setVolume(player.getVolume() - decrementPerStep);
						Thread.sleep(msPerStep);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				setPlaying(playerId, false);
				crossfadePlayers[playerId].stop();
			}
		}.start();
	}

}
