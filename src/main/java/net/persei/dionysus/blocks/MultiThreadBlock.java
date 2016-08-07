package net.persei.dionysus.blocks;

import net.persei.dionysus.Data;

public class MultiThreadBlock extends Block {
	private static final long serialVersionUID = -7687603755832903813L;

	public MultiThreadBlock(int n) {
		super();
		for (int i = 0; i < n; i++) {
			this.lanes.add(new Lane("thread lane " + i));
		}
		this.name = "MultiThreadBlock";
		this.type = BlockType.MultiplexingBlock;
	}
	
	private class LaneThread extends Thread {
		private Lane lane;
		private Data input;
		private Block parent;
		
		public LaneThread (Data input, Lane lane, Block parent) {
			this.input = input;
			this.lane = lane;
			this.parent = parent;
		}
		
		public void run() {
			try {
				lane.invoke(input);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean magic(Data input, Lane lane) throws Exception {
		for (Lane l : lanes) {
			new LaneThread(input, l, this).start();
		}
		return true;
	}

}
