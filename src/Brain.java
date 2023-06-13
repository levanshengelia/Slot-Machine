import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public class Brain {
    private Random rand;
    private Map<Character, Double> coefficientMapping;
    public Brain(double[] coefficients) {
        rand = new Random();
        coefficientMapping = new HashMap<>();
        coefficientMapping.put('@', coefficients[0]);
        coefficientMapping.put('#', coefficients[1]);
        coefficientMapping.put('$', coefficients[2]);
        coefficientMapping.put('%', coefficients[3]);
        coefficientMapping.put('&', coefficients[4]);
    }

    // Rolling simulation of the slot machine. This method generates random symbols on 3x3 matrix
    // based on the probabilities
    public char[][] rollSlotMachine(int RTP) {
        char[][] res = new char[3][3];
        char[] charSet = {'@', '#', '$', '%', '&'};
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(i == 1)
                    continue;
                res[i][j] = getRandomCharacter(charSet);
            }
        }
        fillMiddleRow(res, RTP, charSet);
        return res;
    }

    // Method fills the middle row of the matrix based on combination probabilities
    private void fillMiddleRow(char[][] res, int RTP, char[] charSet) {
        double randNum = rand.nextDouble();
        double[] probabilitiesPrefixSum = getPrefixSum(getProbabilities(RTP));
        if(randNum < probabilitiesPrefixSum[0])
            fillRow(res, '@');
        else if(randNum < probabilitiesPrefixSum[1])
            fillRow(res, '#');
        else if(randNum < probabilitiesPrefixSum[2])
            fillRow(res, '$');
        else if(randNum < probabilitiesPrefixSum[3])
            fillRow(res, '%');
        else if(randNum < probabilitiesPrefixSum[4])
            fillRow(res, '&');
        else {
            res[1][0] = getRandomCharacter(charSet);
            res[1][1] = getRandomCharacter(exclude(res[1][0], charSet));
            res[1][2] = getRandomCharacter(charSet);
        }
    }

    // Method fills second row of the matrix with same character
    private void fillRow(char[][] res, char c) {
        res[1][0] = c;
        res[1][1] = c;
        res[1][2] = c;
    }

    // Calculating prefix sum for the probability array
    public static double[] getPrefixSum(double[] arr) {
        return IntStream.rangeClosed(0, arr.length - 1)
                .mapToDouble(i -> Arrays.stream(arr, 0, i + 1).sum())
                .toArray();
    }

    // This method return the probabilities of combinations
    private double[] getProbabilities(int RTP) {
        return coefficientMapping.keySet().stream()
                .map(symbol -> RTP / (coefficientMapping.get(symbol) * 500.0))
                .mapToDouble(Double::doubleValue)
                .toArray();
    }

    // Method returns all the character from charSet except 'c'
    private char[] exclude(char c, char[] charSet) {
        char[] res = new char[charSet.length - 1];
        int index = 0;
        for(char ch : charSet) {
            if(ch != c)
                res[index++] = ch;
        }
        return res;
    }

    // Setting random characters on non-important cells in matrix
    private char getRandomCharacter(char[] charSet) {
        int randomNum = rand.nextInt(charSet.length);
        return charSet[randomNum];
    }

    // Given the slot machine and a bet, this method calculates how much player won
    public double calculateWin(char[][] slot, double bet) {
        if(slot[1][0] == slot[1][1] && slot[1][1] == slot[1][2]) {
            return bet * coefficientMapping.get(slot[1][0]);
        } else {
            return 0;
        }
    }
}
