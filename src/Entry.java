import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Entry {

    private String name;
    private double bet;
    private double win;
    private LocalDateTime time;

    public Entry() {}

    public Entry(String name, double bet, double win, LocalDateTime time) {
        this.name = name;
        this.bet = bet;
        this.win = win;
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBet(double bet) {
        this.bet = bet;
    }

    public double getBet() {
        return bet;
    }

    public void setWin(double win) {
        this.win = win;
    }

    public double getWin() {
        return win;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    // get the copy of the instance variable
    public LocalDateTime getTime() {
        return LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(), time.getHour(), time.getMinute());
    }
}
