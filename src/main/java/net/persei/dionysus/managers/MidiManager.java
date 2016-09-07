package net.persei.dionysus.managers;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiDevice.Info;

public class MidiManager {
	
	private CommandManager commandManager;

	public MidiManager(CommandManager cm) throws MidiUnavailableException {
		this.commandManager = cm;
		
		Info[] infos = MidiSystem.getMidiDeviceInfo();

		System.out.println("Searching for midi devices...");
		for (Info info : infos) {
			MidiDevice device = MidiSystem.getMidiDevice(info);
			if (info.getName().contains("MPD18")) {
				System.out.println("Found MPD18: " + info.getName());
				if (device.getMaxTransmitters() == 0)
					continue;
				if (!device.isOpen())
					device.open();
				devices.add(device);
				Transmitter transmitter = device.getTransmitter();
				transmitter.setReceiver(new MPD18Listener());
				continue;
			}
			if (info.getName().contains("S")) {
				System.out.println("Found Launchpad: " + info.getName());
				if (device.getMaxTransmitters() == 0)
					continue;
				if (!device.isOpen())
					device.open();
				devices.add(device);
				Transmitter transmitter = device.getTransmitter();
				transmitter.setReceiver(new LaunchpadListener());
				continue;
			}
			System.out.println("Found nothing: " + info.getName());
		}
	}
	
	public static final int MIDI_NOTE_OFF = 0x80;
	public static final int MIDI_NOTE_ON = 0x90;

	private List<MidiDevice> devices = new LinkedList<>();

	private class MPD18Listener implements Receiver {
		@Override
		public void close() {
		}

		@Override
		public void send(MidiMessage m, long timeStamp) {
			if (!(m instanceof ShortMessage))
				return;
			ShortMessage message = (ShortMessage) m;
			boolean press = message.getCommand() != MIDI_NOTE_OFF;
			int x = message.getData1() & 0x03;
			int y = (message.getData1() >> 2) & 0x03;
			handle(press, MidiSource.MPD18, message.getChannel(), x, y, message.getData2());
		}
	}

	private class LaunchpadListener implements Receiver {
		@Override
		public void close() {
		}

		@Override
		public void send(MidiMessage m, long timeStamp) {
			if (!(m instanceof ShortMessage))
				return;
			ShortMessage message = (ShortMessage) m;
			boolean press = !(message.getCommand() == MIDI_NOTE_OFF || message.getData2() == 0);
			int x = message.getData1() & 0x0F;
			int y = (message.getData1() >> 4) & 0x0F;
			handle(press, MidiSource.LAUNCHPAD, message.getChannel(), x, y, press ? 127 : 0);
		}
	}

	public void close() {
		for (MidiDevice device : devices) {
			device.close();
		}
	}

	private void handle(boolean press, MidiSource source, int channel, int x, int y, int v) {
		System.out.println(press + " on " + source + ": " + x + ", " + y + " - " + v);
		commandManager.registerEvent(new Event(source, press, channel, x, y, v));
	}
}