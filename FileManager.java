package util;

import model.UniversityManager;
import java.io.*;

public class FileManager {
    private static final String DEFAULT_FILE = "university_data.ser";

    // Save to default location
    public static void save(UniversityManager manager) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DEFAULT_FILE))) {
            oos.writeObject(manager);
        }
    }

    // Load from dfltr location
    public static UniversityManager load() throws IOException, ClassNotFoundException {
        File f = new File(DEFAULT_FILE);
        if (!f.exists()) return new UniversityManager();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DEFAULT_FILE))) {
            return (UniversityManager) ois.readObject();
        }
    }

    //Load from a customm file selected by the user. did it if you lready have a file and want to load
    public static UniversityManager load(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (UniversityManager) ois.readObject();
        }
    }
}
