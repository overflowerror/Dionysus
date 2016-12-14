package net.persei.dionysus.managers;

import java.util.LinkedList;
import java.util.List;

import net.persei.dionysus.Main;
import net.persei.dionysus.commands.Command;
import net.persei.dionysus.events.Event;

public class CommandManager {
	static private class CommandTuple {
		public final Sequence sequence;
		public final Command command;

		public CommandTuple(Sequence sequence, Command command) {
			this.sequence = sequence;
			this.command = command;
		}

		public boolean equals(Object obj) {
			if (!(obj instanceof CommandTuple))
				return false;
			CommandTuple ct = (CommandTuple) obj;
			if (!sequence.equals(ct.sequence))
				return false;
			if (!command.equals(ct.command))
				return false;
			return true;
		}
	}

	private List<CommandTuple> commands = new LinkedList<>();
	private List<CommandTuple> executedCommands = new LinkedList<>();
	private Sequence sequence = new Sequence();

	public Sequence getSequence() {
		return sequence;
	}

	public Context getContext() {
		return context;
	}

	private Context context = new Context();
	private Context nextContext = null;
	private long lastChange = 0;

	public long getLastChange() {
		return lastChange;
	}

	public void setContext(Context context) {
		this.nextContext = context;
	}

	static public long maxInterval = 1 * 1000;

	public boolean registerSequence(Sequence sequence) {
		checkTimeout();
		this.sequence.addAll(sequence);
		lastChange = System.currentTimeMillis();
		return checkForCommand();
	}
	
	private void checkTimeout() {
		if (System.currentTimeMillis() - lastChange > maxInterval) {
			this.sequence = new Sequence(context);
			executedCommands = new LinkedList<>();
		}
	}

	public boolean registerEvent(Event event) {
		if (System.currentTimeMillis() - lastChange > maxInterval) {
			sequence = new Sequence(context);
			executedCommands = new LinkedList<>();
		}
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
				if (executedCommands.contains(ct))
					continue;
				System.out.println("Executing " + ct.command.getName() + "...");
				if (!Main.test)
					ct.command.execute();
				executedCommands.add(ct);
				result = true;
			}
		}
		if (nextContext != null)
			context = nextContext;
		nextContext = null;
		return result;

	}

	public void addCommand(Sequence sequence, Command command) {
		System.out.println("Adding command " + command.getName() + " on " + sequence);
		if (containsSequence(sequence)) {
			System.err.println("#########\nDuplicated sequence!\n#########");
			return;
		}
		commands.add(new CommandTuple(sequence, command));
	}

	private boolean containsSequence(Sequence sequence) {
		for (CommandTuple tuple : commands) {
			if (tuple.sequence.equals(sequence))
				return true;
		}
		return false;
	}

	public List<Event> getEventsOfCommandsInSequence() {
		checkTimeout();
		List<Event> events = new LinkedList<Event>();
		for (CommandTuple tuple : commands) {
			int miss = 0;
			Event missedEvent = null;
			for (Event event : tuple.sequence) {
				if (sequence.contains(event))
					continue;
				if (++miss > 1)
					break;
				missedEvent = event;
			}
			if (miss != 1)
				continue;
			events.add(missedEvent);
		}
		return events;
	}
}
