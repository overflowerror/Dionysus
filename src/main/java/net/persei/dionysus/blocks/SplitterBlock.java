package net.persei.dionysus.blocks;

import net.persei.dionysus.Data;

public class SplitterBlock extends Block {
	private static final long serialVersionUID = -5591009739089256605L;

	public SplitterBlock(int n) {
		super();
		for (int i = 0; i < n; i++) {
			this.lanes.add(new Lane("split lane " + i));
		}
		this.name = "SplitterBlock";
		this.type = BlockType.MultiplexingBlock;
	}
	
	@Override
	public boolean magic(Data input, Lane lane) throws Exception {
		boolean res = true;
		for (Lane l : lanes) {
			res = l.invoke(new Data(input)) && res;
		}
		return res;
	}

}
