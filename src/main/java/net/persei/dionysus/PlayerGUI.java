package net.persei.dionysus;

import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class PlayerGUI extends JFrame implements Player {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6068058949441154348L;

	private EmbeddedMediaPlayerComponent player = new EmbeddedMediaPlayerComponent();
	
	private boolean loop = false;
	private boolean fullscreen = false;
	
	public boolean isFullscreen() {
		return fullscreen;
	}

	public PlayerGUI(boolean fullscreen) throws HeadlessException {
		this();
		dispose();
		if (fullscreen) {
			setExtendedState(JFrame.MAXIMIZED_BOTH); 
			setUndecorated(true);
		}
		setVisible(true);
	}
	public PlayerGUI() throws HeadlessException {
		super(Main.TITLE);
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
}
