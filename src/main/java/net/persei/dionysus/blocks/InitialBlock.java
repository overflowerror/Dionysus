package net.persei.dionysus.blocks;

import java.util.LinkedList;
import java.util.List;

import net.persei.dionysus.MidiTrigger;
import net.persei.dionysus.Setup;
import net.persei.dionysus.exceptions.UnexpectedDataTypeException;

public abstract class InitialBlock extends Block {
	protected List<MidiTrigger> triggers = new LinkedList<MidiTrigger>();
	
	public List<MidiTrigger> getTriggers() {
		return triggers;
	}
	
	public boolean trigger(MidiTrigger trigger, Setup setup) throws Exception {
		Data input = new Data();
		input.setEntry("trigger", trigger);
		input.setEntry("setup", setup);
		return preMagic(input, null);
	}
}
