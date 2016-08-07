package net.persei.dionysus.blocks;

import net.persei.dionysus.Data;

public class IfElseBlock extends ConditionalBlock {
	private static final long serialVersionUID = 851005550879292074L;
	private BlockCondition condition;
	
	public IfElseBlock(BlockCondition condition) {
		super();
		this.name = "IfElseBlock";
		this.type = BlockType.ConditionalBlock;
		
		this.condition = condition;
		this.lanes.add(new Lane("false lane")); // 0
		this.lanes.add(new Lane("true lane")); // 1
	}
	
	@Override
	public boolean magic(Data input, Lane lane) throws Exception {
		if (condition.parse(input))
			return lanes.get(1).invoke(input);
		else
			return lanes.get(0).invoke(input);
	}
}
