package backend;

public class BitsToInt {
    /**
     * Chnage bits string to int
     *
     * @param bitsString bits string
     * @return int value
     */
    public static int bitsToInt(String bitsString) {
        int result = 0;
        String reversed = new StringBuilder(bitsString).reverse().toString();
        for (int i = 0; i < 8; i++) {
            String s = reversed.substring(i, i + 1);
            if (s.equals("1")) {
                int temp = 1;
                for (int j = 0; j < i; j++) {
                    temp *= 2;
                }
                result += temp;
            }
        }
        return result;
    }
}
