package net.persei.dionysus;

import java.io.IOException;

import net.persei.dionysus.exceptions.LibrariesNotFoundException;
import net.persei.dionysus.exceptions.LoaderException;
import net.persei.dionysus.files.FileManager;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class Main {

	public static final String TITLE = "Dionysus";
	
	/**
	 * @throws ClassNotFoundException 
	 * @param args
	 * @throws LibrariesNotFoundException 
	 * @throws IOException 
	 * @throws LoaderException 
	 * @throws  
	 */
	public static void main(String[] args) throws LibrariesNotFoundException, IOException, LoaderException, ClassNotFoundException {
		if (!findLibs())
			throw new LibrariesNotFoundException();
        System.out.println(LibVlc.INSTANCE.libvlc_get_version()); 
        
        Setup setup = new Setup();
        setup.getData().setEntry("test", "bla");
        
        FileManager manager = new FileManager();
        manager.saveSetup("test.dio", setup);
        setup = null;
        setup = manager.loadSetup("test.dio");
        
        System.out.println(setup.getData().getEntry("test"));
	}

	private static boolean findLibs() {
		return new NativeDiscovery().discover();
	}

}
