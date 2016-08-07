package net.persei.dionysus.blocks;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import net.persei.dionysus.Data;

public abstract class Block implements Serializable {
	private static final long serialVersionUID = 2452582891824176260L;
	protected String name;
	protected BlockType type;
	protected List<Lane> lanes = new LinkedList<Lane>();
	
	public List<Lane> getOutputLanes() {
		return lanes;
	}
	public String getName() {
		return name;
	}
	public BlockType getType() {
		return type;
	}
	
	public boolean preMagic (Data input, Lane lane) throws Exception {
		BlockHelper.addPassLog(input, "blocks", this);
		return magic(input, lane);
	}
	
	abstract public boolean magic(Data input, Lane lane) throws Exception;
}
