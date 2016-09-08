package net.persei.dionysus.commands;

import net.persei.dionysus.managers.CommandManager;
import net.persei.dionysus.managers.Context;
import net.persei.dionysus.managers.Sequence;

public class SetContextCommand extends Command {

	private String name;
	private CommandManager commandManager;
	private Context context;
	
	public SetContextCommand(String name, CommandManager commandManager, Context context) {
		this.name = name;
		this.commandManager = commandManager;
		this.context = context;
	}
	
	@Override
	public void execute() {
		if (!commandManager.getContext().equals(context))
			commandManager.setContext(context);
	}

	@Override
	public String getName() {
		return name;
	}

}
