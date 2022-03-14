package pl.tele.backend;
import java.util.Arrays;

public class DoubleCorrection extends Correction{
    private int[][] hMatrix = {
            {1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0},
            {1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
            {1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
    };

    public DoubleCorrection() {
        super(8);
        super.hMatrix = hMatrix;
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
            he.append(rowResult % 2);
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
        for (int i = 0; i < 8; i++) {
            if (i == diff1 || i == diff2) {
                if(bitsString.charAt(i) == '1') {
                    sb.append(0);
                } else {
                    sb.append(1);
                }
            } else {
                sb.append(bitsString.charAt(i));
            }
        }
        return sb.toString();
    }

    String getColumn(int[][] matrix, int column) {
        int[] bitsArray = Arrays.stream(matrix).mapToInt(ints -> ints[column]).toArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(bitsArray[i]);
        }
        return sb.toString();
    }

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
