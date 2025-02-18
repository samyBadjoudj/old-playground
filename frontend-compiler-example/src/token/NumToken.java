package token;

/**
 * Created by samy on 5/2/15.
 */
public class NumToken extends Token {
    public final int value;

    @Override
    public String toString() {
        return value + "";
    }

    public NumToken(int value) {
        super(TagToken.NUM);
        this.value = value;
    }
}
