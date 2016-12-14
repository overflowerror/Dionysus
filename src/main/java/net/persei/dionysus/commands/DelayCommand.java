package net.persei.dionysus.commands;

public class DelayCommand extends Command {

	private Command command;
	private long time;
	private String name;
	
	public DelayCommand(String name, Command command, long time) {
		this.command = command;
		this.time = time;
		this.name = name;
	}
	
	@Override
	public void execute() {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(time);
					System.out.println(name + ": Executing " + command.getName());
					command.execute();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public String getName() {
		return name;
	}

}
