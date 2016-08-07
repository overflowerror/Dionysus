package net.persei.dionysus.blocks;

import net.persei.dionysus.Data;
import net.persei.dionysus.Setup;

public class SetStateBlock extends Block {
	private static final long serialVersionUID = -6111464916913814484L;
	private String state;
	
	public SetStateBlock(String status) {
		super();
		name = "SetStateBlock";
		type = BlockType.ActiveBlock;
		this.state = status;
		lanes.add(new Lane("lane"));
	}
	
	@Override
	public boolean magic(Data input, Lane lane) throws Exception {
		Setup setup = ((Setup) input.getEntry("setup"));
		int i = setup.getStates().indexOf(state);
		if (i >= 0)
			setup.setState(i);
		else {
			setup.getStates().add(state);
			setup.setState(setup.getStates().indexOf(state));
		}
		return this.lanes.get(0).invoke(input);
	}

}
