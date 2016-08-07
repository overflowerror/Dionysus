package net.persei.dionysus.blocks;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import net.persei.dionysus.Data;

public class Lane implements Serializable {
	private static final long serialVersionUID = -317193861413134584L;
	private List<Block> attachedBlocks = new LinkedList<Block>();
	static private int count = 1;
	final private int id;
	final private String name;
	
	public List<Block> getAttachedBlocks() {
		return attachedBlocks;
	}

	public String getName() {
		return name;
	}

	public Lane(String name) {
		super();
		this.id = count++;
		this.name = name;
	}

	public void attach(Block block) {
		attachedBlocks.add(block);
	}
	
	public boolean invoke(Data input) throws Exception {
		BlockHelper.addPassLog(input, "lanes", this);
		
		boolean res = true;
		for (Block block : attachedBlocks) {
			res = block.preMagic(input, this) & res;
		}
		return res;
	}
}
