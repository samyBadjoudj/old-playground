package token;

/**
 * Created by samy on 5/2/15.
 */
public class Token {

    private final int tag;

    public Token(int t) {
        tag = t;
    }

    public String toString() {
        return "" + (char) getTag();
    }

    public int getTag() {
        return tag;
    }
}
