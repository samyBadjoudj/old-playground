package expression;

import token.TypeWordToken;
import token.WordToken;

/**
 * Created by samy on 5/2/15.
 */
public class Temp extends Expression {

    private static int COUNT_TEMP_VAR = 0;
    private int number = 0;

    public Temp(TypeWordToken p) {
        super(WordToken.temp, p);
        number = ++COUNT_TEMP_VAR;
    }

    public String toString() {
        return "("+ getTypeWord() +") t" + number;
    }
}
