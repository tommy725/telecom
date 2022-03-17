package pl.tele.backend;

public abstract class Correction {
    protected int[][] hMatrix;
    private int columns;

    public Correction(int columns) {
        this.columns = columns;
    }

    /**
     * Encode method saves given buts to string and adds all row results at the end
     * @param bitsString bits string to encode
     * @return encoded bits string
     */
    public String encode(String bitsString) {
        StringBuilder sb = new StringBuilder();
        sb.append(bitsString); //Move given bits to result
        for (int i = 0; i < columns; i++) {
            int rowResult = 0;
            for (int j = 0; j < 8; j++) {
                int originalBit = Integer.parseInt(bitsString.substring(j, j + 1)); //Sent bit
                int matrixBit = hMatrix[i][j]; //hMatrix bit
                rowResult += originalBit * matrixBit; //Count multiply of H(hMatrix) and T(send)
            }
            sb.append(rowResult % 2); //Check the parity and add row result to final result
        }
        return sb.toString();
    }

    public String decode(String bitsString) {
        return null;
    }

    /**
     * Change bits where error occured
     * @param bitsString bits string to be corrected
     * @param diff where diff occured
     * @return bits string with changed diff bit
     */
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
