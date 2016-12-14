package net.persei.dionysus.players;

import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import net.persei.dionysus.Main;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class VideoPlayer extends JFrame implements Player {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6068058949441154348L;

	private EmbeddedMediaPlayerComponent player = new EmbeddedMediaPlayerComponent();
	
	private boolean loop = false;
	private boolean fullscreen = false;
	private String name;
	
	public boolean isFullscreen() {
		return fullscreen;
	}

	public VideoPlayer(String name, boolean fullscreen) throws HeadlessException {
		this(name);
		dispose();
		if (fullscreen) {
			setExtendedState(JFrame.MAXIMIZED_BOTH); 
			setUndecorated(true);
		}
		setVisible(true);
	}
	public VideoPlayer(String name) throws HeadlessException {
		super(Main.TITLE + " - " + name);

		this.name = name;
		setBounds(100, 100, 600, 400);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);
		
		setContentPane(player);
		
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                player.release();
                System.out.println("release");
                System.exit(0);
            }
        });
	}
	
	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
		getMediaPlayer().setRepeat(loop);
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
	
	public  EmbeddedMediaPlayer getMediaPlayer() {
		return player.getMediaPlayer();
	}
	
	public String getName() {
		return name;
	}

	@Override
	public void stop() {
		getMediaPlayer().stop();
	}

	@Override
	public boolean isPlaying() {
		return getMediaPlayer().isPlaying();
	}
}
