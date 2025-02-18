package expression;

import token.TypeWordToken;
import token.WordToken;

/**
 * Created by samy on 5/2/15.
 */
public class IdExpression extends Expression {
    private int offset;

    public IdExpression(WordToken id, TypeWordToken type, int offset) {
        super(id, type);
        this.offset = offset;
    }

}
