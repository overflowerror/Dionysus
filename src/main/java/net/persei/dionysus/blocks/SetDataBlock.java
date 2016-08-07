package net.persei.dionysus.blocks;

import java.util.HashMap;
import java.util.Map;

import net.persei.dionysus.Data;

public class SetDataBlock extends Block {
	private static final long serialVersionUID = 1007781353625719636L;
	private String data;
	private String key;
	
	public SetDataBlock(String key, String data) {
		super();
		name = "SetDateBlock";
		type = BlockType.ActiveBlock;
		this.key = key;
		this.data = data;
		lanes.add(new Lane("lane"));
	}
	
	@Override
	public boolean magic(Data input, Lane lane) throws Exception {
		Map<String, String> blockData = (Map<String, String>) input.getEntry("blockData");
		if (blockData == null) {
			input.setEntry("blockData", new HashMap<String, String>());
			blockData = (Map<String, String>) input.getEntry("blockData");
		}
		blockData.put(key, data);
		
		return this.lanes.get(0).invoke(input);
	}

}
