package net.persei.dionysus.managers;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiDevice.Info;

import net.persei.dionysus.events.Event;

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
			if (info.getName().contains("S ")) {
				System.out.println("Found Launchpad: " + info.getName());
				if (device.getMaxTransmitters() != 0) {
					System.out.println("... transmitter");
					if (!device.isOpen())
						device.open();
					devices.add(device);
					Transmitter transmitter = device.getTransmitter();
					transmitter.setReceiver(new LaunchpadListener());
					continue;
				}
				if (device.getMaxReceivers() != 0) {
					System.out.println("... receiver");
					if (!device.isOpen())
						device.open();
					devices.add(device);
					continue;
				}
			}
			System.out.println("Found nothing: " + info.getName());
		}
		feedbackSequence();
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

	public void feedbackSequence() {
		MidiDevice launchpadTmp = null;
		for (MidiDevice device : devices) {
			if (device.getDeviceInfo().getName().contains("S ")) {
				if (device.getMaxReceivers() == 0) {
					System.out.println("Launchpad found, but no receivers. " + device.getDeviceInfo().getName());
					continue;
				}
				launchpadTmp = device;
				break;
			}
		}
		if (launchpadTmp == null)
			return;
		final MidiDevice launchpad = launchpadTmp;

		Receiver receiver;
		try {
			receiver = launchpad.getReceiver();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
			return;
		}
		new Thread() {
			public void run() {
				try {
					while (true) {
						List<Event> events = new Sequence(commandManager.getSequence());
						List<Event> context = commandManager.getContext();
						for (Event event : context) {
							if (events.contains(event))
								events.remove(event);
						}

						int intensity = 0x03
								- (int) ((3.0 * (System.currentTimeMillis() - commandManager.getLastChange())
										/ CommandManager.maxInterval));
						
						clearLaunchpad(launchpad);
						for (Event event : context) {
							setLaunchpad(launchpad, event.getX(), event.getY(), 0x00, 0x03);
						}
						if (intensity > 0) {
							for (Event event : events) {
								setLaunchpad(launchpad, event.getX(), event.getY(), intensity, 0x00);
							}
						}
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void clearLaunchpad(MidiDevice launchpad) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 8; j++) {
				setLaunchpad(launchpad, i, j, 0, 0);
			}
		}
	}

	private void setLaunchpad(MidiDevice launchpad, int x, int y, int r, int g) {
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(((r | g) != 0) ? MIDI_NOTE_ON : MIDI_NOTE_OFF, 0, ((y & 0x07) << 4) | (x & 0x0F),
					(r & 0x03) | ((g & 0x03) << 4));
			launchpad.getReceiver().send(message, System.currentTimeMillis());
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
}