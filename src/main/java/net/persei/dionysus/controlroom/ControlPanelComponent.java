package net.persei.dionysus.controlroom;

import java.awt.Dimension;

public interface ControlPanelComponent extends Drawable {
	public Dimension getDimension();
	public void setPosition(Position position);
	public Position getPosition();
}
