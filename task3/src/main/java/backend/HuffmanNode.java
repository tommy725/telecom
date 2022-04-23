package backend;

public class HuffmanNode {
    int probability;
    Character sign;

    HuffmanNode left;
    HuffmanNode right;

    public HuffmanNode(int probability, Character sign) {
        this.probability = probability;
        this.sign = sign;
    }

    public int getProbability() {
        return probability;
    }

    public char getSign() {
        return sign;
    }

    public HuffmanNode getLeft() {
        return left;
    }

    public HuffmanNode getRight() {
        return right;
    }

    public void setLeft(HuffmanNode left) {
        this.left = left;
    }

    public void setRight(HuffmanNode right) {
        this.right = right;
    }
}
