package expression.logical;

import expression.Expression;
import token.Token;

/**
 * Created by samy on 5/2/15.
 */
public class NotLogicalExpression extends LogicalExpression {

    public NotLogicalExpression(Token tok, Expression x2) {
        super(tok, x2, x2);
    }

    public void jumping(int t, int f) throws Exception {
        expression2.jumping(f, t);
    }

    public String toString() {
        return getOperator().toString() + " " + expression2.toString();
    }
}
