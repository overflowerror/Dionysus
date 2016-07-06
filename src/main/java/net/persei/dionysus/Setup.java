package net.persei.dionysus;

import java.util.LinkedList;
import java.util.List;

import net.persei.dionysus.blocks.InitialBlock;

public class Setup {
	private List<InitialBlock> initBlocks = new LinkedList<InitialBlock>();
	private List<Player> players = new LinkedList<Player>();
	
	// ugly, but necessary for flexible states
	private List<String> states = new LinkedList<String>(){{
		push("none");
	}};
	private int state = 0;
	
	public List<InitialBlock> getInitBlocks() {
		return initBlocks;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public String getState() {
		return states.get(state);
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public List<String> getStates() {
		return states;
	}
}
