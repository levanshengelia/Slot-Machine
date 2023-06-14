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

    // Test addEntry() method in database
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

    @Test
    public void testPlayersInMonth0() throws SQLException {
        for(int i = 0; i < 100; i++) {
            Entry entry = new Entry("Levani " + i, i, i, LocalDateTime.now());
            testDatabase.addEntry(entry);
        }
        assertEquals(100, testDatabase.playersInMonth(LocalDate.now().getMonth()));
    }

    @Test
    public void testPlayersInMonth1() throws SQLException {
        for(int i = 0; i < 10; i++) {
            testDatabase.addEntry(new Entry("Levani", i, i, LocalDateTime.now()));
        }
        assertEquals(1, testDatabase.playersInMonth(LocalDate.now().getMonth()));
        testDatabase.addEntry(new Entry("Lasha", 200, 300, LocalDateTime.now()));
        assertEquals(2, testDatabase.playersInMonth(LocalDate.now().getMonth()));
    }

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

    private LocalDateTime getDate(Month month) {
        return LocalDateTime.of(2023, month, 1, 0, 0);
    }

    @After
    public void cleanDatabase() throws SQLException {
        testDatabase.cleanDatabase();
    }
}
