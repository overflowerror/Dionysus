package net.persei.dionysus.files;

import java.io.Serializable;

import net.persei.dionysus.Setup;

public class SetupFileHelper implements Serializable {
	private static final long serialVersionUID = 223487487950538107L;

	private Setup setup;

	public Setup getSetup() {
		return setup;
	}

	public void setSetup(Setup setup) {
		this.setup = setup;
	}
	
}
