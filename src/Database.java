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

    // Initialises Connection and Statement objects by connecting to slot_machine_database
    public Database(String dbName) throws SQLException {
        this.dbName = dbName;
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        statement = connection.createStatement();
        create();
    }

    // Adds entry into the game_history database
    public void addEntry(Entry entry) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (Name, Bet, Win, Time) VALUES (?, ?, ?, ?)";
        PreparedStatement st = connection.prepareStatement(sql);
        st.setString(1, entry.getName());
        st.setDouble(2, entry.getBet());
        st.setDouble(3, entry.getWin());
        st.setObject(4, entry.getTime());
        st.executeUpdate();
    }

    // Return all the entry in the game_history database. Only for testing
    public List<Entry> getAllEntry() throws SQLException {
        List<Entry> res = new ArrayList<>();
        ResultSet rs = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
        while(rs.next()) {
            res.add(new Entry(rs.getString(2), rs.getDouble(3),
                    rs.getDouble(4), rs.getObject(5, LocalDateTime.class)));
        }
        return res;
    }


    public int playersInMonth(Month month) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT name)" +
                " FROM " + TABLE_NAME +
                " WHERE MONTH(Time) = ";
        ResultSet rs = getResultSet(sql, month);
        if(rs.next()) return rs.getInt(1);
        return 0;
    }

    public int betInMonth(Month month) throws SQLException {
        String sql = "SELECT SUM(Bet)" +
                " FROM " + TABLE_NAME +
                " WHERE MONTH(Time) = ";
        ResultSet rs = getResultSet(sql, month);
        if(rs.next()) return rs.getInt(1);
        return 0;
    }

    private ResultSet getResultSet(String sql, Month month) throws SQLException {
        int monthValue = month.getValue();
        PreparedStatement st = connection.prepareStatement(sql + monthValue);
        ResultSet rs = st.executeQuery();
        return rs;
    }

    // Retrieves from database the amount of money that is won by players in total for each month
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

    private void delete() throws SQLException {
        statement.executeUpdate("DROP DATABASE IF EXISTS " + dbName);
    }
}
