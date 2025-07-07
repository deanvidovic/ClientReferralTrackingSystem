package hr.clientreferraltrackingsystem.serialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class responsible for serializing and deserializing {@link ChangeLogHolder} objects
 * to and from a file.
 */
public class ChangeLogManager {
    private static final String FILE_PATH_LOG = "dat/changes.dat";
    private static final Logger log = LoggerFactory.getLogger(ChangeLogManager.class);

    private ChangeLogManager() {}

    /**
     * Serializes a new change by adding it to the existing list of changes and
     * saving the updated list to the file.
     *
     * @param change the {@link ChangeLogHolder} object to serialize
     */
    public static void serializeChange(ChangeLogHolder change) {
        List<ChangeLogHolder> existingChanges = deserializeChanges();
        existingChanges.add(change);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH_LOG))) {
            oos.writeObject(existingChanges);
        } catch (IOException e) {
            log.error("Error occurred while serializing changes", e);
        }
    }

    /**
     * Deserializes the list of changes from the file.
     *
     * @return a list of {@link ChangeLogHolder} objects, or an empty list if an error occurs
     */
    public static List<ChangeLogHolder> deserializeChanges() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH_LOG))) {
            return (List<ChangeLogHolder>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error occurred while deserializing changes", e);
            return new ArrayList<>();
        }
    }
}
