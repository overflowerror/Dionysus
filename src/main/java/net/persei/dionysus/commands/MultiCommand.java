package net.persei.dionysus.commands;

import java.util.Arrays;
import java.util.List;

public class MultiCommand extends Command {
	private String name;
	private List<Command> commands;
	
	public MultiCommand(String name, Command... commands) {
		this.name = name;
		this.commands = Arrays.asList(commands);
	}
	
	@Override
	public void execute() {
		for (Command command : commands) {
			System.out.println(name + ": Executing " + command.getName());
			command.execute();
		}
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override 
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!obj.getClass().equals(this.getClass()))
			return false;
		MultiCommand command = (MultiCommand) obj;
		if (!command.getName().equals(this.getName()))
			return false;
		if (!command.commands.equals(commands))
			return false;
		return true;
	}
}
