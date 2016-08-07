package net.persei.dionysus;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import net.persei.dionysus.blocks.InitialBlock;
import net.persei.dionysus.players.Player;

public class Setup implements Serializable {
	private static final long serialVersionUID = -5974881686360496618L;
	private List<InitialBlock> initBlocks = new LinkedList<InitialBlock>();
	private List<Player> players = new LinkedList<Player>();
	private Data data = new Data();

	// ugly, but necessary for flexible states
	private List<String> states = new LinkedList<String>(){
		private static final long serialVersionUID = 1L;
		{
			push("none");
		}
	};
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
	public Data getData() {
		return data;
	}
}
