package pl.tele.backend;
import java.util.Arrays;

public class DoubleCorrection extends Correction{
    private int[][] hMatrix = {
            {0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
            {1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
            {1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
            {0, 1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
    };

    public DoubleCorrection() {
        super(8);
        super.hMatrix = hMatrix;
    }

    /**
     * Decode 16 bits string
     * @param bitsString 16 bits string
     * @return decoded bits string with error corrected
     */
    public String decode(String bitsString) {
        StringBuilder he = new StringBuilder();
        for (int i = 0; i < columns; i++) {
            int rowResult = 0;
            for (int j = 0; j < 16; j++) {
                int codedBit = Integer.parseInt(bitsString.substring(j, j + 1)); //Received bit
                int matrixBit = hMatrix[i][j]; //hMatrix bit
                rowResult += codedBit * matrixBit; //Multiply H matrix with R (received)
            }
            he.append(rowResult % 2); //Add row result to HE (HE = HE + HT = H(T+E) = HR)
        }
        int diff1 = -1;
        int diff2 = -1;
        for (int i = 0; i < 16; i++) { //Searching where diff occured
            if (he.toString().equals(getColumn(hMatrix, i))) { //Search for 1 error (HE column same with one of HMatrix column)
                diff1 = i;
                break;
            }
            for (int j = i + 1; j < 16; j++) {
                if (he.toString().equals(getColumnSum(hMatrix, i, j))) { //Search for 2 error (HE column same with sum of two HMatrix columns)
                    diff1 = i;
                    diff2 = j;
                    break;
                }
            }
        }
        return get8BitsWithChangeOnPosition( //Change the bit where error occured
                get8BitsWithChangeOnPosition(bitsString, diff1),
                diff2
        );
    }

    /**
     * Return sum of 2 given columns
     * @param matrix hMatrix
     * @param column1 column 1 number
     * @param column2 column 2 number
     * @return columns 1 and 2 sum
     */
    String getColumnSum(int[][] matrix, int column1, int column2) {
        int[] bitsArray1 = Arrays.stream(matrix).mapToInt(ints -> ints[column1]).toArray();
        int[] bitsArray2 = Arrays.stream(matrix).mapToInt(ints -> ints[column2]).toArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (bitsArray1[i] + bitsArray2[i] == 2) {
                sb.append(0);
            } else {
                sb.append(bitsArray1[i] + bitsArray2[i]);
            }
        }
        return sb.toString();
    }
}
