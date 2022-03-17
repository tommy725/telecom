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

    public String decode(String bitsString) {
        StringBuilder he = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int rowResult = 0;
            for (int j = 0; j < 12; j++) {
                int codedBit = Integer.parseInt(bitsString.substring(j, j + 1));
                int matrixBit = hMatrix[i][j];
                rowResult += codedBit * matrixBit;
            }
            he.append(rowResult % 2);
        }
        int diff = -1;
        for (int i = 0; i < 12; i++) {
            if (getColumn(hMatrix, i).equals(he.toString())) {
                diff = i;
            }
        }
        return get8BitsWithChangeOnPosition(bitsString, diff);
    }

    String getColumn(int[][] matrix, int column) {
        int[] bitsArray = Arrays.stream(matrix).mapToInt(ints -> ints[column]).toArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(bitsArray[i]);
        }
        return sb.toString();
    }
}
