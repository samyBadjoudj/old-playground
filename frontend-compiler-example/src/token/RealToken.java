package token;

/**
 * Created by samy on 5/2/15.
 */
public class RealToken extends Token {
    public final float value;

    @Override
    public String toString() {
        return "token.RealToken{" +
                "value=" + value +
                '}';
    }

    public RealToken(float value) {
        super(TagToken.REAL);
        this.value = value;
    }
}
