package net.persei.dionysus.blocks;

import net.persei.dionysus.Setup;
import net.persei.dionysus.exceptions.MalformedSetupFileException;
import net.persei.dionysus.exceptions.WhatTheFuckException;

public class SetupParser {
	private enum Mode {
		none, players, triggers, blocks
	}
	static public Setup parse(String file) throws MalformedSetupFileException, WhatTheFuckException {
		String lines[] = file.split("\n");
		
		Mode mode = Mode.none;
		
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.length() == 0)
				continue;
			String tokens[] = line.split(" ");
			for (int j = 0; j < tokens.length; j++) {
				String token = tokens[j];
				if (token.length() == 0)
					continue;
				
				// comment line
				if (token.substring(0, 1).equals("#"))
					break;
				
				// mode selection
				if (token.equals("*players")) {
					mode = Mode.players;
					break;
				}
				if (token.equals("*triggers")) {
					mode = Mode.triggers;
					break;
				}
				if (token.equals("*blocks")) {
					mode = Mode.blocks;
					break;
				}
				
				// TODO
				switch(mode) {
				case blocks:
					break;
				case players:
					break;
				case triggers:
					break;
				case none:
					throw new MalformedSetupFileException();
				default:
					throw new WhatTheFuckException();
				}
			}
		}
		
		
		return null;
	}
}
