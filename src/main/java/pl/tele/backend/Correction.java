package pl.tele.backend;

public abstract class Correction {
    protected int[][] hMatrix;
    private int columns;

    public Correction(int columns) {
        this.columns = columns;
    }

    public String encode(String bitsString) {
        StringBuilder sb = new StringBuilder();
        sb.append(bitsString);
        for (int i = 0; i < columns; i++) {
            int rowResult = 0;
            for (int j = 0; j < 8; j++) {
                int originalBit = Integer.parseInt(bitsString.substring(j, j + 1));
                int matrixBit = hMatrix[i][j];
                rowResult += originalBit * matrixBit;
            }
            sb.append(rowResult % 2);
        }
        return sb.toString();
    }

    public String decode(String bitsString) {
        return null;
    }

    public String get8BitsWithChangeOnPosition(String bitsString, int diff) {
        StringBuilder sb = new StringBuilder(bitsString.substring(0, 8));
        if (diff > -1) {
            if (bitsString.charAt(diff) == '1') {
                sb.setCharAt(diff, '0');
            } else {
                sb.setCharAt(diff, '1');
            }
        }
        return sb.toString();
    }
}
