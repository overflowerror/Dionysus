package net.persei.dionysus.commands;

import net.persei.dionysus.managers.CommandManager;
import net.persei.dionysus.managers.Sequence;

public class ProvokeCommand extends Command {

	private Sequence sequence;
	private String name;
	private CommandManager commandManager;
	
	public ProvokeCommand(String name, Sequence sequence, CommandManager commandManager) {
		this.sequence = sequence;
		this.name = name;
		this.commandManager = commandManager;
	}
	
	@Override
	public void execute() {
		commandManager.registerSequence(sequence);
	}

	@Override
	public String getName() {
		return name;
	}

}
