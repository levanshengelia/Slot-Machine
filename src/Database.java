import javax.xml.crypto.Data;
import java.sql.*;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/slot_machine_database";
    private static final String USERNAME = "root"; // Change this to your MySQL server username
    private static final String PASSWORD = "levani"; // Change this to your MySQL server password
    private static Connection connection;
    private static Statement statement;

    // Initialises Connection and Statement objects by connecting to slot_machine_database
    public static void connectToDatabase() throws SQLException {
       connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
       statement = connection.createStatement();
    }

    // Adds entry into the game_history database
    public static void addEntry(String name, double bet, double win) throws SQLException {
        statement.executeUpdate("INSERT INTO game_history VALUES" +
                "(10,'" + name + "', " + bet + ", " + win + ")");
    }

    // Deletes entry from game_history database
    public static void deleteEntry(int id) throws SQLException {
        statement.executeUpdate("DELETE FROM game_history WHERE id = " + id);
    }
}
