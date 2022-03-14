package pl.tele.backend;

import java.util.stream.IntStream;

public class DoubleCorrection {
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

    public String encode(String bitsString) {
        StringBuilder sb = new StringBuilder();
        sb.append(bitsString);
        for (int i = 0; i < 8; i++) {
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
        for (int i = 0; i < 8; i++) {
            int rowResult = 0;
            for (int j = 0; j < 16; j++) {
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
        System.out.println(he);
        int diff1 = -1;
        int diff2 = -1;
        for (int i = 0; i < 16; i++) {
            if (he.toString().equals(getColumn(hMatrix, i))) {
                diff1 = i;
                break;
            }
        }
        if (diff1 == -1) {
            for (int i = 0; i < 16; i++) {
                for (int j = i + 1; j < 16; j++) {
                    if (he.toString().equals(getColumnSum(hMatrix, i, j))) {
                        diff1 = i;
                        diff2 = j;
                        break;
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        System.out.println(diff1);
        System.out.println(diff2);
        for (int i = 0; i < 8; i++) {
            if (i == diff1 || i == diff2) {
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
        for (int i = 0; i < 8; i++) {
            sb.append(bitsArray[i]);
        }
        return sb.toString();
    }

    String getColumnSum(int[][] matrix, int column1, int column2) {
        int[] bitsArray1 = IntStream.range(0, matrix.length)
                .map(i -> matrix[i][column1]).toArray();
        int[] bitsArray2 = IntStream.range(0, matrix.length)
                .map(i -> matrix[i][column2]).toArray();
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
