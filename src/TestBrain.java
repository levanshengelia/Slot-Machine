import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestBrain {

    private static final double EPSILON = 0.0000001;
    private static final double DELTA = 0.1;


    // Test the calculateWin method in Brain class when player wins
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

    // Test the calculationWin method in Brain class when player loses the bet
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

    // Test the rolling of the slot machine with certain RTP value
    @Test
    public void testRollSlotMachine0() {
        Brain brain0 = new Brain(new double[]{2, 5, 10, 100, 1000});
        roll(brain0, 1000000, 1.0, 99, DELTA * 1.0);
        Brain brain1 = new Brain(new double[]{1, 10, 100, 1000, 10000});
        roll(brain1, 1000000, 50.0, 50, DELTA * 50);
        Brain brain2 = new Brain(new double[]{1, 2, 3, 4, 5});
        roll(brain1, 1000000, 19, 20, DELTA * 19);
    }

    @Test
    public void testRollSlotMachine1() {
        Brain brain0 = new Brain(new double[]{1, 1, 1, 1, 1});
        roll(brain0, 1000000, 7.5, 99, DELTA * 7.5);
        Brain brain1 = new Brain(new double[]{1, 2, 1, 2, 1});
        roll(brain1, 1000000, 1000, 100, DELTA * 1000);
        Brain brain2 = new Brain(new double[]{123, 123, 123, 123, 123});
        roll(brain1, 1000000, 1234, 88, DELTA * 1234);
    }

    // This method makes the rolling simulation and check mathematical expectation in the end
    private void roll(Brain brain, int simulationNum, double bet, int RSP, double deviation) {
        double win = 0;
        for(int i = 0; i < simulationNum; i++) {
            double a = brain.calculateWin(brain.rollSlotMachine(RSP), bet);
            win += a;
        }
        assertEquals(bet * RSP / 100, win / simulationNum, deviation);
    }
}
