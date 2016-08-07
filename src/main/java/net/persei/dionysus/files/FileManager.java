package net.persei.dionysus.files;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.persei.dionysus.Setup;
import net.persei.dionysus.exceptions.LoaderException;

public class FileManager {
	public Setup loadSetup(String file) throws IOException, ClassNotFoundException, LoaderException {
		FileInputStream fStream = new FileInputStream(file);
		ObjectInputStream oStream = new ObjectInputStream(fStream);
		Object obj = oStream.readObject();
		oStream.close();
		fStream.close();
		
		if (!(obj instanceof SetupFileHelper))
			throw new NoSetupObjectException();
		
		SetupFileHelper helper = (SetupFileHelper) obj;
		
		return helper.getSetup();
	}
	
	public void saveSetup(String file, Setup setup) throws IOException {
		SetupFileHelper holder = new SetupFileHelper();
		holder.setSetup(setup);
		
		FileOutputStream fStream = new FileOutputStream(file);
		ObjectOutputStream oStream = new ObjectOutputStream(fStream);
		oStream.writeObject(holder);
		oStream.close();
		fStream.close();
	}
}
