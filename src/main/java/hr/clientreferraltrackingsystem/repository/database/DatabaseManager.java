package hr.clientreferraltrackingsystem.repository.database;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manages database connections by reading configuration properties and establishing connections.
 */
public class DatabaseManager {

    /**
     * Default constructor for DatabaseManager.
     */
    DatabaseManager() {}

    /**
     * Establishes and returns a connection to the database using properties from the configuration file.
     *
     * @return a Connection object to the database
     * @throws IOException  if an error occurs while reading the properties file
     * @throws SQLException if a database access error occurs
     */
    public Connection connectToDatabase() throws IOException, SQLException {
        Properties props = new Properties();
        try (FileReader reader = new FileReader("database.properties")) {
            props.load(reader);
        }
        return DriverManager.getConnection(
                props.getProperty("databaseUrl"),
                props.getProperty("username"),
                props.getProperty("password"));
    }
}
