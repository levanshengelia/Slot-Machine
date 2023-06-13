import java.util.Scanner;

public class SlotStarter {
    private static final int INITIAL_MONEY = 100;
    private static final int MINIMUM_BET = 1;
    private static final int RTP = 95; // Return to player
    private static final double[] COEFFICIENTS = { 2, 5, 10, 100, 1000 };
    public static void main(String[] args) {
        printInstructions();
        start();
    }

    // This method makes a simulation of playing process, placing bet and calculating win after each round
    private static void start() {
        double balance = INITIAL_MONEY;
        Brain brain = new Brain(COEFFICIENTS);
        while (true) {
            int bet = placeBet(balance);
            balance -= bet;
            char[][] slot = brain.rollSlotMachine(RTP);
            double win = brain.calculateWin(slot, bet);
            balance += win;
            if(balance < MINIMUM_BET) break;
        }
    }

    // This method allow player to make a bet. Checks if the bet is valid
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

    // Giving player the instruction about how to play the game before placing the first bet
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