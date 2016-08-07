package net.persei.dionysus.controlroom;

import java.awt.Graphics;

public interface ControlPanelBlock extends ControlPanelComponent, Clickable {
	default void draw(Graphics graphics) {
		
	}
}
