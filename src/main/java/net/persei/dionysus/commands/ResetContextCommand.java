package net.persei.dionysus.commands;

import net.persei.dionysus.managers.CommandManager;
import net.persei.dionysus.managers.Context;
import net.persei.dionysus.managers.Sequence;

public class ResetContextCommand extends SetContextCommand {

	public ResetContextCommand(String name, CommandManager commandManager) {
		super(name, commandManager, new Context());
	}

}
