package net.persei.dionysus.managers;

import java.util.LinkedList;
import java.util.List;

public class CommandManager {
	static private class CommandTuple {
		public final Sequence sequence;
		public final Command command;

		public CommandTuple(Sequence sequence, Command command) {
			this.sequence = sequence;
			this.command = command;
		}
	}

	private List<CommandTuple> commands = new LinkedList<>();
	private Sequence sequence = new Sequence();
	private long lastChange = 0;

	static private long maxInterval = 1 * 1000;

	public boolean registerEvent(Event event) {
		if (System.currentTimeMillis() - lastChange > maxInterval)
			sequence = new Sequence();
		sequence.add(event);
		lastChange = System.currentTimeMillis();
		return checkForCommand();
	}

	private boolean checkForCommand() {
		boolean result = false;
		for (CommandTuple ct : commands) {
			boolean check = true;
			for (Event event : ct.sequence)
				if (!sequence.contains(event)) {
					check = false;
					break;
				}
			if (check) {
				System.out.println("Executing " + ct.command.getName() + "...");
				ct.command.execute();
				result = true;
			}
		}
		return result;

	}

	public void addCommand(Sequence sequence, Command command) {
		System.out.println("Adding command " + command.getName() + " on " + sequence);
		commands.add(new CommandTuple(sequence, command));
	}
}
