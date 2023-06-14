import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Scanner;

public class SlotStarter {
    private static final int INITIAL_MONEY = 100;
    private static final int MINIMUM_BET = 1;
    private static final int RTP = 95; // Return to player
    private static final String DATABASE_NAME = "slot_machine_database";
    private static final double[] COEFFICIENTS = { 2, 5, 10, 100, 1000 };

    /*
     * This is the entry point of the program. It initializes the database, prints the instructions,
     * starts the game, and asks the user if they want to display the statistics.
     */
    public static void main(String[] args) throws InterruptedException, SQLException {
        Database db = new Database(DATABASE_NAME);
        printInstructions();
        start(db);
        askForStatistics(db);
    }

    /*
     * This method asks the user if they want to display the statistics and calls the
     * displayStatistics method if they answer yes.
     */
    private static void askForStatistics(Database db) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Do you want to display slot machine statistics?\n" +
                "Enter 'Y' or 'N': ");
        char c = sc.next().charAt(0);
        if(c == 'Y' || c == 'y')
            displayStatistics(db);
    }

    /*
     * This method displays the statistics for each month using the Database object passed
     * as a parameter. It prints the number of different players who played the slot machine,
     * the total amount of money bet, and the total amount of money won for each month.
     */
    private static void displayStatistics(Database db) throws SQLException {
        System.out.println();
        for(Month month : Month.values()) {
            System.out.println(month.toString() + ":");
            System.out.println(db.playersInMonth(month) + " different players played slot machine");
            System.out.println(db.betInMonth(month) + " GEL bet was made in total");
            System.out.println(db.winInMonth(month) + " GEL was won by players in total\n");
        }
    }

    /*
     * This method begins the slot machine game. It initializes the player's balance, gets
     * their name, and enters a loop where the player can place bets and play the game.
     * Each time the player makes a bet, the method updates the player's balance,
     * rolls the slot machine, calculates the winnings, and adds an entry to the database.
     */
    private static void start(Database db) throws SQLException {
        double balance = INITIAL_MONEY;
        Brain brain = new Brain(COEFFICIENTS);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scanner.next();
        while (true) {
            Entry entry = new Entry();
            entry.setName(name);
            entry.setBet(placeBet(balance));
            balance -= entry.getBet();
            char[][] slot = brain.rollSlotMachine(RTP);
            printTheSlot(slot);
            entry.setWin(brain.calculateWin(slot, entry.getBet()));
            entry.setTime(LocalDateTime.now());
            db.addEntry(entry);
            balance += entry.getWin();
            printWinningMessage(entry.getWin(), balance, entry.getBet());
            if(balance < MINIMUM_BET) break;
            if(!askToContinue(balance)) return;
        }
        System.out.println("You don't have the minimum amount of money to bet");
    }

    /*
     * This method is a helper method that prints the slot machine on the console.
     */
    private static void printTheSlot(char[][] slot) {
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                System.out.print(" " + slot[i][j]);
            }
            System.out.println();
        }
    }

    /*
     * This method asks the user if they want to continue playing the game and
     * returns true or false depending on their answer.
     */
    private static boolean askToContinue(double balance) {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("Enter 'Y' or 'N' if you want to continue or not: ");
            char c = scanner.next().charAt(0);
            if (c == 'Y' || c == 'y') return true;
            if (c == 'N' || c == 'n') return false;
        }
    }

    /*
     * This method prints a message to the console depending on whether the player
     * won or lost and updates their balance accordingly.
     */
    private static void printWinningMessage(double win, double balance, double bet) {
        if(win == 0.0)
            System.out.println("You lost, your current balance is: " + balance);
        else
            System.out.println("You won " + win / bet + "x, your current balance is: " + balance);
    }

    /*
     * This method asks the user to place a bet and returns the amount of money they bet.
     */
    private static int placeBet(double balance) {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("Enter the amount of money you want to bet: ");
            int bet = scanner.nextInt();
            if(bet > balance)
                System.out.println("You only have " + balance + "GEL.");
            else if(bet < MINIMUM_BET)
                System.out.println("The minimum bet is " + MINIMUM_BET + "GEL.");
            else return bet;
        }
    }

    /*
     * This method prints the instructions for how to play the game to the console.
     */
    private static void printInstructions() {
        System.out.println("\nWelcome to the slot game!\n\n" +
                "Here's how to play:\n\n" +
                "* You start with " + INITIAL_MONEY + "GEL.\n" +
                "* Place a bet to start the game. The minimum bet is " + MINIMUM_BET + "GEL.\n" +
                "* The slot machine will randomly generate symbols on a 3x3 matrix.\n" +
                "* If the symbols on the second horizontal line match, you win!\n" +
                "* There are five different symbols in the game:\n" +
                "   1. @: This symbol has a " + COEFFICIENTS[0] + "x coefficient.\n" +
                "   2. #: This symbol has a " + COEFFICIENTS[1] + "x coefficient.\n" +
                "   3. $: This symbol has a " + COEFFICIENTS[2] + "x coefficient.\n" +
                "   4. %: This symbol has a " + COEFFICIENTS[3] + "x coefficient.\n" +
                "   5. &: This symbol has a " + COEFFICIENTS[4] + "x coefficient.\n" +
                "* The different symbols have different winning chances, with the '&' symbol being\n" +
                "  the rarest and most valuable.\n" +
                "* If you win, your winnings will be calculated by multiplying you bet by the \n" +
                "  coefficient of the winning symbol.\n" +
                "* You can continue playing as long as you have money in you account.\n" +
                "* Good luck and have fun!\n");
    }
}