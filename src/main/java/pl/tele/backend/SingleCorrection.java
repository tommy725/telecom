package pl.tele.backend;

import java.util.stream.IntStream;

public class SingleCorrection {
    private int[][] hMatrix = {
            {1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0},
            {1, 0, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0},
            {0, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0},
            {0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1},
    };

    public String encode(String bitsString) {
        StringBuilder sb = new StringBuilder();
        sb.append(bitsString);
        for (int i = 0; i < 4; i++) {
            int rowResult = 0;
            for (int j = 0; j < 8; j++) {
                int originalBit = Integer.parseInt(bitsString.substring(j, j + 1));
                int matrixBit = hMatrix[i][j];
                rowResult += originalBit * matrixBit;
            }
            if (rowResult % 2 == 0) {
                sb.append(0);
            } else {
                sb.append(1);
            }
        }
        return sb.toString();
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
            if (rowResult % 2 == 0) {
                he.append(0);
            } else {
                he.append(1);
            }
        }
        int diff = -1;
        for (int i = 0; i < 12; i++) {
            if(getColumn(hMatrix,i).equals(he.toString())) {
                diff = i;
            }
        }
        System.out.println(diff);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (i == diff) {
                if(bitsString.charAt(i) == '1') {
                    sb.append(0);
                } else {
                    sb.append(1);
                }
            } else {
                sb.append(bitsString.substring(i, i + 1));
            }
        }
        return sb.toString();
    }

    String getColumn(int[][] matrix, int column) {
        int[] bitsArray = IntStream.range(0, matrix.length)
                .map(i -> matrix[i][column]).toArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(bitsArray[i]);
        }
        return sb.toString();
    }
}
