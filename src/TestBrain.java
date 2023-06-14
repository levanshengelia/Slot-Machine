import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestBrain {
    private static final double EPSILON = 0.0000001;
    private static final double DELTA = 0.1;

    /*
     * This method tests the calculateWin method in the Brain class when the player wins the bet.
     * It creates a Brain object with a set of payout values and checks the calculation of winnings
     * for different input matrices and bet amounts.
     */
    @Test
    public void testCalculateWin0() {
        Brain brain = new Brain(new double[]{2, 5, 10, 100, 1000});
        char[][] matrix0 = {{'@', '@', '@'}, {'@', '@', '@'}, {'@', '@', '@'}};
        assertEquals(200.0, brain.calculateWin(matrix0, 100.0), EPSILON);
        assertEquals(7.0, brain.calculateWin(matrix0, 3.5), EPSILON);
        assertEquals(24690.0, brain.calculateWin(matrix0, 12345.0), EPSILON);
        char[][] matrix1 = {{'@', '@', '@'}, {'#', '#', '#'}, {'@', '$', '@'}};
        assertEquals(4000.0, brain.calculateWin(matrix1, 800.0), EPSILON);
        assertEquals(25.0, brain.calculateWin(matrix1, 5.0), EPSILON);
        char[][] matrix2 = {{'@', '&', '$'}, {'$', '$', '$'}, {'&', '$', '@'}};
        assertEquals(990.0, brain.calculateWin(matrix2, 99.0), EPSILON);
        assertEquals(100.0, brain.calculateWin(matrix2, 10.0), EPSILON);
        char[][] matrix3 = {{'@', '&', '$'}, {'%', '%', '%'}, {'&', '$', '@'}};
        assertEquals(12300.0, brain.calculateWin(matrix3, 123.0), EPSILON);
        assertEquals(1200.0, brain.calculateWin(matrix3, 12.0), EPSILON);
        char[][] matrix4 = {{'@', '&', '$'}, {'&', '&', '&'}, {'&', '$', '@'}};
        assertEquals(2000.0, brain.calculateWin(matrix4, 2.0), EPSILON);
        assertEquals(549000.0, brain.calculateWin(matrix4, 549.0), EPSILON);
        assertEquals(0.0, brain.calculateWin(matrix4, 0.0), EPSILON);
    }

    /*
     * This method tests the calculateWin method in the Brain class when the player loses the bet.
     * It creates a Brain object with a set of payout values and checks that the winnings are always
     * zero for different input matrices and bet amounts.
     */
    @Test
    public void testCalculateWin1() {
        Brain brain = new Brain(new double[]{1, 2, 3, 4, 5});
        char[][] matrix0 = {{'@', '@', '@'}, {'@', '@', '#'}, {'@', '@', '@'}};
        assertEquals(0.0, brain.calculateWin(matrix0, 100.0), EPSILON);
        assertEquals(0.0, brain.calculateWin(matrix0, 999.0), EPSILON);
        char[][] matrix1 = {{'@', '@', '@'}, {'&', '#', '#'}, {'@', '$', '@'}};
        assertEquals(0.0, brain.calculateWin(matrix1, 702.0), EPSILON);
        assertEquals(0.0, brain.calculateWin(matrix1, 13.0), EPSILON);
        char[][] matrix2 = {{'@', '&', '$'}, {'%', '@', '$'}, {'&', '$', '@'}};
        assertEquals(0.0, brain.calculateWin(matrix2, 17.0), EPSILON);
        assertEquals(0.0, brain.calculateWin(matrix2, 8.0), EPSILON);
        char[][] matrix3 = {{'@', '&', '$'}, {'#', '#', '&'}, {'&', '$', '@'}};
        assertEquals(0.0, brain.calculateWin(matrix3, 123123.0), EPSILON);
        assertEquals(0.0, brain.calculateWin(matrix3, 888.0), EPSILON);
        char[][] matrix4 = {{'@', '&', '$'}, {'@', '&', '&'}, {'&', '$', '@'}};
        assertEquals(0.0, brain.calculateWin(matrix4, 7.0), EPSILON);
        assertEquals(0.0, brain.calculateWin(matrix4, 613.0), EPSILON);
        assertEquals(0.0, brain.calculateWin(matrix4, 0.0), EPSILON);
    }

    /*
     * This method tests the rolling of the slot machine with a certain return to player (RTP) value.
     * It creates a Brain object with a set of payout values and simulates rolling the slot machine a
     * certain number of times. It then checks that the average winnings over the simulations match the
     * expected winnings based on the RTP value
     */
    @Test
    public void testRollSlotMachine0() {
        Brain brain0 = new Brain(new double[]{2, 5, 10, 100, 1000});
        roll(brain0, 1000000, 1.0, 99, DELTA * 1.0);
        Brain brain1 = new Brain(new double[]{1, 10, 100, 1000, 10000});
        roll(brain1, 1000000, 50.0, 50, DELTA * 50);
        Brain brain2 = new Brain(new double[]{1, 2, 3, 4, 5});
        roll(brain2, 1000000, 19, 200, DELTA * 19);
        Brain brain3 = new Brain(new double[]{1, 1, 1, 1, 1});
        roll(brain3, 1000000, 7.5, 99, DELTA * 7.5);
        Brain brain4 = new Brain(new double[]{1, 2, 1, 2, 1});
        roll(brain4, 1000000, 1000, 110, DELTA * 1000);
        Brain brain5 = new Brain(new double[]{2, 2, 2, 2, 2});
        roll(brain5, 1000000, 100, 9, DELTA * 300);
    }

    /*
     * The roll method is a helper method that simulates the rolling of the slot machine and calculates
     * the average winnings over a certain number of simulations. The method takes a Brain object,
     * the number of simulations, the bet amount, the RTP value, and the allowed deviation from the
     * expected winnings as parameters. The method uses the calculateWin and rollSlotMachine methods
     * in the Brain class to simulate rolling the slot machine and calculate the winnings for each simulation.
     * It then checks that the average winnings over the simulations match the expected winnings based
     * on the RTP value within the allowed deviation.
     */
    private void roll(Brain brain, int simulationNum, double bet, int RSP, double deviation) {
        double win = 0;
        for(int i = 0; i < simulationNum; i++) {
            double a = brain.calculateWin(brain.rollSlotMachine(RSP), bet);
            win += a;
        }
        assertEquals(bet * RSP / 100, win / simulationNum, deviation);
    }
}
