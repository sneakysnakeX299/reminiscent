package reminiscent;

import java.io.*;
import java.util.Scanner;

public class ConfigHandler {
    public static String[] lines = new String[1000];
    public static String writeLine;
    public static File configFile;
    public static File configDir;
    public static int counter = 0;

    public static void ConfigHandle() throws IOException {
        String homedir = System.getProperty("user.home");
        configDir = new File(homedir + "/.config/reminiscent");
        configFile = new File(homedir + "/.config/reminiscent/reminiscent.conf");
        try {
            if (!configDir.exists()) {
                configDir.mkdir();
                configFile.createNewFile();
            }
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
        } catch (Exception e) {
            System.out.println("Encountered an error during IO operation. Committing suicide.");
            System.exit(69);
        }
        BufferedReader configReader = new BufferedReader(new FileReader(configFile));
        String line = configReader.readLine();
        while (line != null) {
            lines[counter] = line;
            line = configReader.readLine();
            counter++;
        }
    }
}
