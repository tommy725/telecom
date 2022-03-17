package pl.tele.backend;

import java.util.Arrays;

public class SingleCorrection extends Correction {
    private int[][] hMatrix = {
            {1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0},
            {1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0},
            {0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0},
            {0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1},
    };

    public SingleCorrection() {
        super(4);
        super.hMatrix = hMatrix;
    }

    /**
     * Decode 12 bits string
     * @param bitsString 12 bits string
     * @return decoded bits string with error corrected
     */
    public String decode(String bitsString) {
        StringBuilder he = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int rowResult = 0;
            for (int j = 0; j < 12; j++) {
                int codedBit = Integer.parseInt(bitsString.substring(j, j + 1)); //Received bit
                int matrixBit = hMatrix[i][j]; //hMatrix bit
                rowResult += codedBit * matrixBit; //Multiply H matrix with R (received)
            }
            he.append(rowResult % 2); //Add row result to HE (HE = HE + HT = H(T+E) = HR)
        }
        int diff = -1;
        for (int i = 0; i < 12; i++) {
            if (getColumn(hMatrix, i).equals(he.toString())) { //Diff occurs on the bit where column is the same as HE
                diff = i;
            }
        }
        return get8BitsWithChangeOnPosition(bitsString, diff); //Change the bit where error occured
    }

    /**
     * Return column of hMatrix
     * @param matrix hMatrix
     * @param column column number
     * @return column with given number
     */
    String getColumn(int[][] matrix, int column) {
        int[] bitsArray = Arrays.stream(matrix).mapToInt(ints -> ints[column]).toArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(bitsArray[i]);
        }
        return sb.toString();
    }
}
