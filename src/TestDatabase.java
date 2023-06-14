import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDatabase {

    private static final String DB_NAME = "slot_machine_database_test";
    private Database testDatabase;

    @Before
    public void initDatabase() throws SQLException {
        testDatabase = new Database(DB_NAME);
        cleanDatabase();
    }

    /*
     * This method tests the addEntry() and getAllEntry() methods in the Database class.
     * It adds 100 Entry objects to the test database and retrieves them using getAllEntry().
     * It then compares the retrieved entries to the original entries to ensure they match.
     */
    @Test
    public void testAddEntry() throws SQLException {
        List<Entry> entries = new ArrayList<>();
        for(int i = 1; i <= 100; i++) {
            Entry entry = new Entry("Levani " + i, i, i, LocalDateTime.now());
            testDatabase.addEntry(entry);
            entries.add(entry);
        }
        List<Entry> result = testDatabase.getAllEntry();
        for(int i = 0; i < 100; i++) {
            assertEquals(entries.get(i).getName(), result.get(i).getName());
            assertEquals(entries.get(i).getBet(), result.get(i).getBet());
            assertEquals(entries.get(i).getWin(), result.get(i).getWin());
            assertEquals(entries.get(i).getTime().getDayOfMonth(), result.get(i).getTime().getDayOfMonth());
        }
    }

    /*
     * This method tests the playersInMonth() method in the Database class.
     * It adds 100 Entry objects to the test database with the current month as their time
     * and calls playersInMonth() with the current month as the argument. It then compares
     * the returned value to the expected value of 100.
     */
    @Test
    public void testPlayersInMonth0() throws SQLException {
        for(int i = 0; i < 100; i++) {
            Entry entry = new Entry("Levani " + i, i, i, LocalDateTime.now());
            testDatabase.addEntry(entry);
        }
        assertEquals(100, testDatabase.playersInMonth(LocalDate.now().getMonth()));
    }

    /*
     * This method tests the playersInMonth() method in the Database class.
     * It adds 10 Entry objects to the test database with the current month as their
     * time and the same name, bet, and win values. It then adds another Entry object
     * with a different name and higher bet and win values. It calls playersInMonth() twice,
     * first with the current month as the argument and then again after adding the new entry.
     * It compares the returned values to ensure they match the expected values.
     */
    @Test
    public void testPlayersInMonth1() throws SQLException {
        for(int i = 0; i < 10; i++) {
            testDatabase.addEntry(new Entry("Levani", i, i, LocalDateTime.now()));
        }
        assertEquals(1, testDatabase.playersInMonth(LocalDate.now().getMonth()));
        testDatabase.addEntry(new Entry("Lasha", 200, 300, LocalDateTime.now()));
        assertEquals(2, testDatabase.playersInMonth(LocalDate.now().getMonth()));
    }

    /*
     * This method tests the playersInMonth() method in the Database class.
     * It adds 10 Entry objects to the test database with the month of June as their time.
     * It then calls playersInMonth() for each month of the year and compares the returned
     * values to ensure they match the expected values.
     */
    @Test
    public void testPlayersInMonth2() throws SQLException {
        for(int i = 0; i < 10; i++) {
            testDatabase.addEntry(new Entry("Levani " + i, i, i, getDate(Month.JUNE)));
        }
        assertEquals(0, testDatabase.playersInMonth(Month.JANUARY));
        assertEquals(0, testDatabase.playersInMonth(Month.FEBRUARY));
        assertEquals(0, testDatabase.playersInMonth(Month.MARCH));
        assertEquals(0, testDatabase.playersInMonth(Month.APRIL));
        assertEquals(0, testDatabase.playersInMonth(Month.MAY));
        assertEquals(10, testDatabase.playersInMonth(Month.JUNE));
        assertEquals(0, testDatabase.playersInMonth(Month.JULY));
        assertEquals(0, testDatabase.playersInMonth(Month.AUGUST));
        assertEquals(0, testDatabase.playersInMonth(Month.SEPTEMBER));
        assertEquals(0, testDatabase.playersInMonth(Month.OCTOBER));
        assertEquals(0, testDatabase.playersInMonth(Month.NOVEMBER));
        assertEquals(0, testDatabase.playersInMonth(Month.DECEMBER));
    }

    /*
     * This method tests the betInMonth() method in the Database class. It adds 100 Entry objects
     * to the test database with the month of April as their time and a bet value of 2.
     * It then adds 200 more Entry objects with the month of March as their time and a bet value of 4.
     * It calls betInMonth() twice, first with April as the argument and then again with March as the argument.
     * It compares the returned values to ensure they match the expected values.
     */
    @Test
    public void testBetInMonth0() throws SQLException {
        for(int i = 0; i < 100; i++) {
            testDatabase.addEntry(new Entry("Levani " + i, 2, i, getDate(Month.APRIL)));
        }
        assertEquals(200, testDatabase.betInMonth(Month.APRIL));
        for(int i = 0; i < 200; i++) {
            testDatabase.addEntry(new Entry("Levani " + i, 4, i, getDate(Month.MARCH)));
        }
        assertEquals(800, testDatabase.betInMonth(Month.MARCH));
    }

    /*
     * This method tests the betInMonth() method in the Database class. It adds 100 Entry
     * objects to the test database with the month of June and a bet value of 1000, and 50
     * Entry objects to the test database with the month of December and a bet value of 1.
     * It then calls betInMonth() for each month of the year and compares the returned values
     * to ensure they match the expected values. Specifically, it asserts that the total bet
     * amount for June is 100000 and the total bet amount for December is 50, while the total
     * bet amount for all other months is 0.
     */
    @Test
    public void testBetInMonth1() throws SQLException {
        for(int i = 0; i < 100; i++) {
            testDatabase.addEntry(new Entry("Levani " + i, 1000, i, getDate(Month.JUNE)));
        }
        for(int i = 0; i < 50; i++) {
            testDatabase.addEntry(new Entry("Levani " + i, 1, i, getDate(Month.DECEMBER)));
        }
        assertEquals(100000, testDatabase.betInMonth(Month.JUNE));
        assertEquals(0, testDatabase.betInMonth(Month.JANUARY));
        assertEquals(0, testDatabase.betInMonth(Month.FEBRUARY));
        assertEquals(0, testDatabase.betInMonth(Month.MARCH));
        assertEquals(0, testDatabase.betInMonth(Month.APRIL));
        assertEquals(0, testDatabase.betInMonth(Month.MAY));
        assertEquals(0, testDatabase.betInMonth(Month.JULY));
        assertEquals(0, testDatabase.betInMonth(Month.AUGUST));
        assertEquals(0, testDatabase.betInMonth(Month.SEPTEMBER));
        assertEquals(0, testDatabase.betInMonth(Month.OCTOBER));
        assertEquals(0, testDatabase.betInMonth(Month.NOVEMBER));
        assertEquals(50, testDatabase.betInMonth(Month.DECEMBER));
    }

    /*
     * This method tests the winInMonth() method in the Database class. It adds 100 Entry
     * objects to the test database with the month of April as their time and a win value of 100.
     * It then adds 200 more Entry objects with the month of March as their time and a win value of 200.
     * It calls winInMonth() twice, first with April as the argument and then again with March as
     * the argument. It compares the returned values to ensure they match the expected values.
     */
    @Test
    public void testWinInMonth0() throws SQLException {
        for(int i = 0; i < 100; i++) {
            testDatabase.addEntry(new Entry("Levani " + i, i, 100, getDate(Month.APRIL)));
        }
        assertEquals(10000, testDatabase.winInMonth(Month.APRIL));
        for(int i = 0; i < 200; i++) {
            testDatabase.addEntry(new Entry("Levani " + i, i, 200, getDate(Month.MARCH)));
        }
        assertEquals(40000, testDatabase.winInMonth(Month.MARCH));
    }

    /*
     * This method tests the winInMonth() method in the Database class.
     * It adds 100 Entry objects to the test database with the month of June as their time and a win value of 30.
     * It then adds 50 more Entry objects with the month of December as their time and a win value of 12.
     * It calls winInMonth() twice, first with June as the argument and then again with December as the argument.
     * It compares the returned values to ensure they match the expected values.
     */
    @Test
    public void testWinInMonth1() throws SQLException {
        for(int i = 0; i < 100; i++) {
            testDatabase.addEntry(new Entry("Levani " + i, i, 30, getDate(Month.JUNE)));
        }
        for(int i = 0; i < 50; i++) {
            testDatabase.addEntry(new Entry("Levani " + i, i, 12, getDate(Month.DECEMBER)));
        }
        assertEquals(3000, testDatabase.winInMonth(Month.JUNE));
        assertEquals(0, testDatabase.winInMonth(Month.JANUARY));
        assertEquals(0, testDatabase.winInMonth(Month.FEBRUARY));
        assertEquals(0, testDatabase.winInMonth(Month.MARCH));
        assertEquals(0, testDatabase.winInMonth(Month.APRIL));
        assertEquals(0, testDatabase.winInMonth(Month.MAY));
        assertEquals(0, testDatabase.winInMonth(Month.JULY));
        assertEquals(0, testDatabase.winInMonth(Month.AUGUST));
        assertEquals(0, testDatabase.winInMonth(Month.SEPTEMBER));
        assertEquals(0, testDatabase.winInMonth(Month.OCTOBER));
        assertEquals(0, testDatabase.winInMonth(Month.NOVEMBER));
        assertEquals(600, testDatabase.winInMonth(Month.DECEMBER));
    }

    /*
     * This method generates a LocalDate object representing the current date.
     * It is used by several of the test methods to set the time for the Entry objects that
     * are added to the test database.
     */
    private LocalDateTime getDate(Month month) {
        return LocalDateTime.of(2023, month, 1, 0, 0);
    }

    /*
     * This method is annotated with @After and is executed after each test method.
     * It clears all the entries from the test database to ensure that each test method starts with a clean slate.
     */
    @After
    public void cleanDatabase() throws SQLException {
        testDatabase.cleanDatabase();
    }
}
