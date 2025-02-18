package token;

/**
 * Created by samy on 5/2/15.
 */
public class WordToken extends Token {

    private final String value;

    public WordToken(String value, int t) {
        super(t);
        this.value = value;

    }

    @Override
    public String toString() {
        return getValue();
    }


    public static final WordToken
            and = new WordToken("&&", TagToken.AND),
            or = new WordToken("||", TagToken.OR),
            eq = new WordToken("==", TagToken.EQ),
            ne = new WordToken("!=", TagToken.NE),
            le = new WordToken("<=", TagToken.LE),
            ge = new WordToken(">=", TagToken.GE),
            minus = new WordToken("minus ", TagToken.MINUS),
            True = new WordToken("true", TagToken.TRUE),
            False = new WordToken("false", TagToken.FALSE),
            temp = new WordToken("t", TagToken.TEMP),
            If = new WordToken("if", TagToken.IF),
            Else = new WordToken("else", TagToken.ELSE),
            While = new WordToken("while", TagToken.WHILE),
            Do = new WordToken("do", TagToken.DO),
            Break = new WordToken("break", TagToken.BREAK),
            Continue = new WordToken("continue", TagToken.CONTINUE)          ;

    public String getValue() {
        return value;
    }
}
