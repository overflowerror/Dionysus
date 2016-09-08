package net.persei.dionysus;

import javax.sound.midi.MidiUnavailableException;

import net.persei.dionysus.commands.Command;
import net.persei.dionysus.commands.MusicPlayCommand;
import net.persei.dionysus.commands.ResetContextCommand;
import net.persei.dionysus.commands.SetContextCommand;
import net.persei.dionysus.commands.VideoPlayCommand;
import net.persei.dionysus.events.Event;
import net.persei.dionysus.exceptions.LibrariesNotFoundException;
import net.persei.dionysus.managers.CommandManager;
import net.persei.dionysus.managers.Context;
import net.persei.dionysus.managers.MidiManager;
import net.persei.dionysus.managers.MidiSource;
import net.persei.dionysus.managers.MusicManager;
import net.persei.dionysus.managers.PlayerManager;
import net.persei.dionysus.managers.Sequence;
import net.persei.dionysus.managers.VideoManager;
import net.persei.dionysus.players.PlayerType;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class Main {

	public static final String TITLE = "Dionysus";

	public static void main(String[] args)
			throws LibrariesNotFoundException, MidiUnavailableException, InterruptedException {
		if (!findLibs())
			throw new LibrariesNotFoundException();
		System.out.println(LibVlc.INSTANCE.libvlc_get_version());

		System.out.println("Instancing video manager...");
		VideoManager videoManager = new VideoManager("MainVideoManager");
		PlayerManager.getInstance().create(PlayerType.Video, "primary", true);
		
		System.out.println("Instancing music manager...");
		MusicManager musicManager = new MusicManager("MainMusicManager");
		
		System.out.println("Instancing command manager...");
		CommandManager commandManager = new CommandManager();
		System.out.println("Instancing midi manager...");
		MidiManager midiManager = new MidiManager(commandManager);
		
		System.out.println("Adding commands...");

		Sequence contextSequence = new Sequence().append(new Event(MidiSource.LAUNCHPAD, true, 0, 8, 7, 0));
		Sequence resetContext = new Sequence().append(new Event(MidiSource.LAUNCHPAD, true, 0, 8, 6, 0));

		Context musicContext = new Context().append(new Event(MidiSource.LAUNCHPAD, true, 0, 8, 0, 0));
		Context fxContext = new Context().append(new Event(MidiSource.LAUNCHPAD, true, 0, 8, 1, 0));
		Context videoContext = new Context().append(new Event(MidiSource.LAUNCHPAD, true, 0, 8, 2, 0));
		Context unusedContext = new Context().append(new Event(MidiSource.LAUNCHPAD, true, 0, 8, 3, 0));

		commandManager.addCommand(resetContext, new ResetContextCommand("ResetContext", commandManager));

		commandManager.addCommand(new Sequence(contextSequence).append(musicContext),
				new SetContextCommand("MusicContext", commandManager, musicContext));
		commandManager.addCommand(new Sequence(contextSequence).append(fxContext),
				new SetContextCommand("FXContext", commandManager, fxContext));
		commandManager.addCommand(new Sequence(contextSequence).append(videoContext),
				new SetContextCommand("VideoContext", commandManager, videoContext));
		commandManager.addCommand(new Sequence(contextSequence).append(unusedContext),
				new SetContextCommand("UnusedContext", commandManager, unusedContext));

		commandManager.addCommand(new Sequence(musicContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 7, 0)),
				new MusicPlayCommand("Play_Merlins's_Study", musicManager,
						"resources/audio/camelot/Merlin's Study.mp3"));

		commandManager.addCommand(new Sequence(videoContext).append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 6, 0)),
				new VideoPlayCommand("Show_Mountains", videoManager, "primary",
						"resources/video/mountains.mp4", true));
		
		System.out.println("Adding shutdown hook...");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				midiManager.close();
			}
		});

		while (true)
			Thread.sleep(Long.MAX_VALUE);
	}

	private static boolean findLibs() {
		return new NativeDiscovery().discover();
	}

}
