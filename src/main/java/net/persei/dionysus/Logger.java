/**
 * 
 */
package net.persei.dionysus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author overflow
 *
 */
public class Logger {

	private static final String MUSIC_LOGFILE_NAME = "music.log";
	private static File musicLogFile;
	private static FileWriter musicLogFileWriter;

	static {
		try {
			musicLogFile = new File(MUSIC_LOGFILE_NAME);
			musicLogFile.mkdirs();
			musicLogFile.delete();
			musicLogFile.createNewFile();
			musicLogFileWriter = new FileWriter(musicLogFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void logMusic(String file) {
		try {
			musicLogFileWriter.append(new File(file).toPath().toRealPath().toString() + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void close() {
		try {
			musicLogFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
