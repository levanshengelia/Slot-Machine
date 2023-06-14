import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USERNAME = "root"; // Change this to your MySQL server username
    private static final String PASSWORD = "levani"; // Change this to your MySQL server password
    private static final String TABLE_NAME = "game_history";
    private Connection connection;
    private Statement statement;
    private String dbName;

    /*
     * This is a constructor for the Database class that takes a String parameter representing
     * the name of the database to connect to. It initializes a new Database object by connecting
     * to the specified database using the DriverManager.getConnection() method and creating a
     * Statement object to execute SQL queries.
     */
    public Database(String dbName) throws SQLException {
        this.dbName = dbName;
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        statement = connection.createStatement();
        create();
    }

    /*
     * This method adds an Entry object to the game_history table in the connected database.
     * It takes an Entry object as a parameter and inserts its name, bet, win, and time
     * properties into the table using a prepared statement.
     */
    public void addEntry(Entry entry) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (Name, Bet, Win, Time) VALUES (?, ?, ?, ?)";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1, entry.getName());
        st.setDouble(2, entry.getBet());
        st.setDouble(3, entry.getWin());
        st.setObject(4, entry.getTime());
        st.executeUpdate();
    }

    /*
     * This method returns a list of all Entry objects in the game_history table in the connected database.
     * It creates a new ArrayList to hold the Entry objects, executes a SQL query to retrieve
     * all rows from the table using a ResultSet, and uses the data in each row to create new Entry
     * objects that are added to the list.
     */
    public List<Entry> getAllEntry() throws SQLException {
        List<Entry> res = new ArrayList<>();
        ResultSet rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
        while(rs.next()) {
            res.add(new Entry(rs.getString(2), rs.getDouble(3),
                    rs.getDouble(4), rs.getObject(5, LocalDateTime.class)));
        }
        return res;
    }

    /*
     * This method returns the number of unique players who made at least one bet in the specified month.
     * It takes a Month parameter, creates a SQL query to retrieve the count of distinct name values in
     * the game_history table where the Time value is in the specified month, and executes the query
     * using a helper method called getResultSet().
     */
    public int playersInMonth(Month month) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT name)" +
                " FROM " + TABLE_NAME +
                " WHERE MONTH(Time) = ";
        ResultSet rs = getResultSet(sql, month);
        if(rs.next()) return rs.getInt(1);
        return 0;
    }

    /*
     * This method returns the total amount of money bet by all players in the specified month.
     * It takes a Month parameter, creates a SQL query to retrieve the sum of all Bet values in the
     * game_history table where the Time value is in the specified month, and executes the query
     * using the getResultSet() helper method.
     */
    public int betInMonth(Month month) throws SQLException {
        String sql = "SELECT SUM(Bet)" +
                " FROM " + TABLE_NAME +
                " WHERE MONTH(Time) = ";
        ResultSet rs = getResultSet(sql, month);
        if(rs.next()) return rs.getInt(1);
        return 0;
    }

    /*
     * This is a helper method that executes a SQL query with the specified month value and returns a
     * ResultSet object. It takes a String parameter representing a SQL query template and a Month
     * parameter representing the month value to substitute into the query. It creates a prepared
     * statement with the substituted month value and executes it to retrieve a ResultSet object.
     */
    private ResultSet getResultSet(String sql, Month month) throws SQLException {
        int monthValue = month.getValue();
        PreparedStatement st = connection.prepareStatement(sql + monthValue);
        ResultSet rs = st.executeQuery();
        return rs;
    }

    /*
     * This method deletes all data from the game_history table in the connected database and
     * recreates the table. It is intended for testing purposes only and should not be used
     * in a production environment.
     */
    public int winInMonth(Month month) throws SQLException {
        String sql = "SELECT SUM(Win)" +
                " FROM " + TABLE_NAME +
                " WHERE MONTH(Time) = ";
        ResultSet rs = getResultSet(sql, month);
        if(rs.next()) return rs.getInt(1);
        return 0;
    }

    // Empty the database. Only for testing
    public void cleanDatabase() throws SQLException {
        delete();
        create();
    }

    /*
     * This is a helper method that creates the game_history table in the connected database if it
     * does not already exist. It uses the statement.executeUpdate() method to execute SQL commands
     * to create the table with the specified schema.
     */
    private void create() throws SQLException {
        statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
        statement.executeUpdate("USE " + dbName);
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS game_history (\n" +
                "        ID INT NOT NULL AUTO_INCREMENT,\n" +
                "        Name VARCHAR(20) NOT NULL,\n" +
                "        Bet DOUBLE NOT NULL,\n" +
                "        Win DOUBLE NOT NULL,\n" +
                "        Time VARCHAR(20) NOT NULL,\n" +
                "        PRIMARY KEY (ID)\n" +
                "    );");
    }

    /*
     * This is a helper method that deletes the connected database.
     * It is intended for testing purposes only and should not be used in a production environment.
     */
    private void delete() throws SQLException {
        statement.executeUpdate("DROP DATABASE IF EXISTS " + dbName);
    }
}
