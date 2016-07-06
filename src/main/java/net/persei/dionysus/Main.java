package net.persei.dionysus;

import net.persei.dionysus.exceptions.LibrariesNotFoundException;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class Main {

	public static final String TITLE = "Dionysus";
	private static PlayerGUI playerGUI;
	
	/**
	 * @param args
	 * @throws LibrariesNotFoundException 
	 */
	public static void main(String[] args) throws LibrariesNotFoundException {
		if (!findLibs())
			throw new LibrariesNotFoundException();

        System.out.println(LibVlc.INSTANCE.libvlc_get_version()); 
	}

	private static boolean findLibs() {
		return new NativeDiscovery().discover();
	}

}
