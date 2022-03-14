package pl.tele.backend;

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
}
