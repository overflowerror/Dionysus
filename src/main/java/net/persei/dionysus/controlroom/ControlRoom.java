package net.persei.dionysus.controlroom;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.persei.dionysus.Setup;
import net.persei.dionysus.blocks.InitialBlock;

public class ControlRoom extends JFrame {
	
	private ControlRoomPanel panel;
	private Setup setup;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7130672040530068619L;

	public ControlRoom(Setup setup) {
		super();
		this.setup = setup;
		setBounds(50, 50, 1000, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		panel = new ControlRoomPanel();
		
		setContentPane(panel);
	}
	
	private class ControlRoomPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7915937062340285742L;
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponents(g);
			g.setColor(Color.BLACK);
			List<InitialBlock> initBlocks = setup.getInitBlocks();
			
			for (int i = 0; i < initBlocks.size(); i++) {
			
			}
		}
	}
}
