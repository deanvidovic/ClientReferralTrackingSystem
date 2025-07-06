package hr.clientreferraltrackingsystem.serialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChangeLogManager {
    private static final String FILE_PATH_LOG = "dat/changes.dat";
    private static final Logger log = LoggerFactory.getLogger(ChangeLogManager.class);

    private ChangeLogManager() {}


    public static void serializeChange(ChangeLogHolder change) {
        List<ChangeLogHolder> existingChanges = deserializeChanges();
        existingChanges.add(change);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH_LOG))) {
            oos.writeObject(existingChanges);
        } catch (IOException e) {
            log.error("Error occurred while serializing changes", e);
        }
    }


    public static List<ChangeLogHolder> deserializeChanges() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH_LOG))) {
            return (List<ChangeLogHolder>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error occurred while deserializing changes", e);
            return new ArrayList<>();
        }
    }
}
