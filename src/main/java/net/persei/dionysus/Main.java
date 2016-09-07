package net.persei.dionysus;

import javax.sound.midi.MidiUnavailableException;

import net.persei.dionysus.exceptions.LibrariesNotFoundException;
import net.persei.dionysus.managers.Command;
import net.persei.dionysus.managers.CommandManager;
import net.persei.dionysus.managers.Event;
import net.persei.dionysus.managers.MidiManager;
import net.persei.dionysus.managers.MidiSource;
import net.persei.dionysus.managers.PlayerManager;
import net.persei.dionysus.managers.Sequence;
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

		System.out.println("Adding players...");
		PlayerManager.getInstance().create(PlayerType.Video, "TestVideo", true);
		PlayerManager.getInstance().create(PlayerType.Audio, "TestAudio", true);

		System.out.println("Instancing command manager...");
		CommandManager commandManager = new CommandManager();
		System.out.println("Instancing midi manager...");
		MidiManager midiManager = new MidiManager(commandManager);

		System.out.println("Adding commands...");
		commandManager.addCommand(new Sequence().append(new Event(MidiSource.LAUNCHPAD, true, 0, 0, 0, 0)),
				new Command() {
					@Override
					public String getName() {
						return "Test Command";
					}

					@Override
					public void execute() {
						PlayerManager.getInstance().getByName("TestVideo").playFile("test.mp4");
						System.out.println("Hallo Welt");
					}
				});
		commandManager.addCommand(new Sequence().append(new Event(MidiSource.LAUNCHPAD, true, 0, 1, 0, 0))
				.append(new Event(MidiSource.LAUNCHPAD, true, 0, 2, 0, 0)), new Command() {
					@Override
					public String getName() {
						return "Test Sequence Command";
					}

					@Override
					public void execute() {
						PlayerManager.getInstance().getByName("TestAudio").playFile("test.mp3");
						System.out.println("Sequence Test");
					}
				});

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
