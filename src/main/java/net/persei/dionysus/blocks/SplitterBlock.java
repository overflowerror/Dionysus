package net.persei.dionysus.blocks;

public class SplitterBlock extends Block {

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
			res = l.invoke(input) && res;
		}
		return res;
	}

}
