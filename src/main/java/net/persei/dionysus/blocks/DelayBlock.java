package net.persei.dionysus.blocks;

import net.persei.dionysus.Data;

public class DelayBlock extends Block {
	private static final long serialVersionUID = -3892822705334086078L;
	private long sleepTime = 1000;
	
	public long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public DelayBlock() {
		super();
		name = "DelayBlock";
		type = BlockType.PassiveBlock;
		lanes.add(new Lane("delayed lane"));
	}

	public boolean magic(Data input, Lane lane) throws Exception {
		Thread.sleep(sleepTime);
		
		return lanes.get(0).invoke(input);
	}

}
