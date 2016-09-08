package net.persei.dionysus.commands;

public abstract class Command {
	public abstract void execute();
	public abstract String getName();
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!obj.getClass().equals(this.getClass()))
			return false;
		Command command = (Command) obj;
		if (!command.getName().equals(this.getName()))
			return false;
		return true;
	}
}
